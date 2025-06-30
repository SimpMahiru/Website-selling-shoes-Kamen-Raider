package com.example.be.repository.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.example.be.common.enums.StoreProcedureStatusCodeEnum;
import com.example.be.common.exception.TechresHttpException;
import com.example.be.common.utils.Pagination;
import com.example.be.entity.Brand;
import com.example.be.model.StoreProcedureListResult;
import com.example.be.repository.AbstractDao;
import com.example.be.repository.BrandDao;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository("BrandDao")
@Transactional
public class BrandDaoImpl extends AbstractDao<Integer, Brand> implements BrandDao {
    @Override
    public void create(Brand brand) {
        this.getSession().save(brand);
    }

    @Override
    public Brand findOne(int id) {
        return this.getSession().find(Brand.class, id);
    }

    @Override
    public void update(Brand brand) {
        this.getSession().update(brand);
    }

    @Override
    public List<Brand> getAll() {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<Brand> query = builder.createQuery(Brand.class);
        Root<Brand> root = query.from(Brand.class);
        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public Brand findByName(String name) {
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<Brand> query = builder.createQuery(Brand.class);
        Root<Brand> root = query.from(Brand.class);
        query.where(builder.equal(root.get("name"), name));
        return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<Brand> findByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<Brand> query = builder.createQuery(Brand.class);
        Root<Brand> root = query.from(Brand.class);
        query.select(root).where(root.get("id").in(ids));
        return this.getSession().createQuery(query).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public StoreProcedureListResult<Brand> spGListBrand(
            String keySearch,
            int status,
            Pagination pagination) throws Exception {
        StoredProcedureQuery query = this.getSession()
                .createStoredProcedureQuery("sp_g_list_brand", Brand.class)
                .registerStoredProcedureParameter("keySearch", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("status", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("_limit", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("_offset", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("total_record", Integer.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

        query.setParameter("keySearch", keySearch);
        query.setParameter("status", status);
        query.setParameter("_limit", pagination.getLimit());
        query.setParameter("_offset", pagination.getOffset());

        int statusCode = (int) query.getOutputParameterValue("status_code");
        String messageError = query.getOutputParameterValue("message_error").toString();

        switch (StoreProcedureStatusCodeEnum.valueOf(statusCode)) {
            case SUCCESS:
                int totalRecord = (int) query.getOutputParameterValue("total_record");
                return new StoreProcedureListResult<>(statusCode, messageError, totalRecord, query.getResultList());
            case INPUT_INVALID:
                throw new TechresHttpException(HttpStatus.BAD_REQUEST, messageError);
            default:
                throw new Exception(messageError);
        }
    }
}
