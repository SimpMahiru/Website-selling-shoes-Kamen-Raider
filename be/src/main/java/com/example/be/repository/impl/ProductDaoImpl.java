package com.example.be.repository.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.example.be.common.enums.StoreProcedureStatusCodeEnum;
import com.example.be.common.exception.TechresHttpException;
import com.example.be.common.utils.Pagination;
import com.example.be.entity.Product;
import com.example.be.model.StoreProcedureListResult;
import com.example.be.repository.AbstractDao;
import com.example.be.repository.ProductDao;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository("ProductDao")
@Transactional
public class ProductDaoImpl extends AbstractDao<Integer, Product> implements ProductDao {
    @Override
    public void create(Product product) {
       this.getSession().save(product);
    }

    @Override
    public Product findOne(int id) {
       return this.getSession().find(Product.class,id);
    }

    @Override
    public void update(Product product) {
       this.getSession().update(product);	
    }

    @Override
    public List<Product> getAll() {
       CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Product> query = builder.createQuery(Product.class);
		Root<Product> root = query.from(Product.class);

		return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public Product findByName(String name) {
        CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Product> query = builder.createQuery(Product.class);
		Root<Product> root = query.from(Product.class);
		query.where(builder.equal(root.get("name"), name));

		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
	@Override
	public StoreProcedureListResult<Product> spGListProduct(String keySearch, int status, Pagination pagination)
			throws Exception {
		StoredProcedureQuery query = this.getSession().createStoredProcedureQuery("sp_g_list_product", Product.class)
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

		
		List<Product> results = query.getResultList() != null ? query.getResultList() : Collections.emptyList();

	    // Truy cập tham số đầu ra sau
	    int statusCode = (int) query.getOutputParameterValue("status_code") ; 
	    String messageError = (String) query.getOutputParameterValue("message_error");
	    int totalRecord = (Integer) query.getOutputParameterValue("total_record");

		switch (StoreProcedureStatusCodeEnum.valueOf(statusCode)) {
		case SUCCESS:
			return new StoreProcedureListResult<>(statusCode, messageError, totalRecord, results);
		case INPUT_INVALID:
			throw new TechresHttpException(HttpStatus.BAD_REQUEST, messageError);
		default:
			throw new Exception(messageError);
		}
	}

	@Override
	public List<Product> findByIds(List<Integer> ids) {
		if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        query.select(root).where(root.get("id").in(ids));

        return this.getSession().createQuery(query).getResultList();
	}
}
