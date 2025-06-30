package com.example.be.repository.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.example.be.common.enums.StoreProcedureStatusCodeEnum;
import com.example.be.common.exception.TechresHttpException;
import com.example.be.common.utils.Pagination;
import com.example.be.entity.Category;
import com.example.be.model.StoreProcedureListResult;
import com.example.be.repository.AbstractDao;
import com.example.be.repository.CategoryDao;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository("CategoryDao")
@Transactional
public class CategoryDaoImpl extends AbstractDao<Integer, Category> implements CategoryDao{
    @Override
	public void create(Category category) {
		this.getSession().save(category);
		
	}

	@Override
	public Category findOne(int id) {
		return this.getSession().find(Category.class,id);
	}

	@Override
	public void update(Category category) {
		this.getSession().update(category);		
		
	}

	@Override
	public Category findByName(String name) {
		CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Category> query = builder.createQuery(Category.class);
		Root<Category> root = query.from(Category.class);
		query.where(builder.equal(root.get("name"), name));

		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
	}

	@Override
	public List<Category> getAll() {
		CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Category> query = builder.createQuery(Category.class);
		Root<Category> root = query.from(Category.class);
//		query.where(builder.equal(root.get("name"), name));

		return this.getSession().createQuery(query).getResultList();
	}

    @Override
    public List<Category> findByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<Category> query = builder.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);
        query.select(root).where(root.get("id").in(ids));
        return this.getSession().createQuery(query).getResultList();
    }

	@SuppressWarnings("unchecked")
	@Override
	public StoreProcedureListResult<Category> spGListCategory(
			int parentId,
			String keySearch,
			int status,
			Pagination pagination) throws Exception {
		StoredProcedureQuery query = this.getSession()
				.createStoredProcedureQuery("sp_g_list_category", Category.class)
				.registerStoredProcedureParameter("parentId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("keySearch", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("status", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_limit", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_offset", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("total_record", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

		query.setParameter("parentId", parentId);
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
