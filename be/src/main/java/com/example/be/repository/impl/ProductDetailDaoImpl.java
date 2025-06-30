package com.example.be.repository.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.example.be.common.enums.StoreProcedureStatusCodeEnum;
import com.example.be.common.exception.TechresHttpException;
import com.example.be.common.utils.Pagination;
import com.example.be.entity.ProductDetail;
import com.example.be.model.StoreProcedureListResult;
import com.example.be.repository.AbstractDao;
import com.example.be.repository.ProductDetailDao;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository("ProductDetailDao")
@Transactional
public class ProductDetailDaoImpl extends AbstractDao<Integer, ProductDetail> implements ProductDetailDao {
    @Override
    public void create(ProductDetail productDetail) {
       this.getSession().save(productDetail);
    }

    @Override
    public ProductDetail findOne(int id) {
       return this.getSession().find(ProductDetail.class,id);
    }

    @Override
    public void update(ProductDetail productDetail) {
       this.getSession().update(productDetail);	
    }

    @Override
    public List<ProductDetail> getAll() {
       CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<ProductDetail> query = builder.createQuery(ProductDetail.class);
		Root<ProductDetail> root = query.from(ProductDetail.class);

		return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public ProductDetail findByName(String name) {
        CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<ProductDetail> query = builder.createQuery(ProductDetail.class);
		Root<ProductDetail> root = query.from(ProductDetail.class);
		query.where(builder.equal(root.get("name"), name));

		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
	@Override
	public StoreProcedureListResult<ProductDetail> spGListProductDetail(
			int productId, 
			int colorId, 
			int sizeId, 
			int materialId,
			int brandId,
			int categoryId, 
			String keySearch,
			int status,
			Pagination pagination) throws Exception {
		StoredProcedureQuery query = this.getSession()
				.createStoredProcedureQuery("sp_g_list_product_detail", ProductDetail.class)
				.registerStoredProcedureParameter("productId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("colorId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("sizeId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("materialId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("brandId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("categoryId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("keySearch", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("status", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_limit", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_offset", Integer.class, ParameterMode.IN)

				.registerStoredProcedureParameter("total_record", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

		query.setParameter("productId", productId);
		query.setParameter("colorId", colorId);
		query.setParameter("sizeId", sizeId);
		query.setParameter("materialId", materialId);
		query.setParameter("brandId", brandId);
		query.setParameter("categoryId", categoryId);
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
    
    @Override
    public List<ProductDetail> findByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        CriteriaBuilder builder = this.getBuilder();
        CriteriaQuery<ProductDetail> query = builder.createQuery(ProductDetail.class);
        Root<ProductDetail> root = query.from(ProductDetail.class);

        query.select(root).where(root.get("id").in(ids));

        return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public ProductDetail findBySku(String sku) {
        try {
            return this.getSession()
                .createQuery("FROM ProductDetail WHERE sku = :sku", ProductDetail.class)
                .setParameter("sku", sku)
                .uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ProductDetail findByBarcode(String barcode) {
        try {
            return this.getSession()
                .createQuery("FROM ProductDetail WHERE barcode = :barcode", ProductDetail.class)
                .setParameter("barcode", barcode)
                .uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }
}
