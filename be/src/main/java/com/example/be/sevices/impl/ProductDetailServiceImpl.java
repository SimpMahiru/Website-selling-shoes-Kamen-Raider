package com.example.be.sevices.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.be.common.utils.Pagination;
import com.example.be.entity.ProductDetail;
import com.example.be.model.StoreProcedureListResult;
import com.example.be.repository.ProductDetailDao;
import com.example.be.sevices.ProductDetailService;

@Service("ProductDetailService")
@Transactional(rollbackFor = Error.class)
public class ProductDetailServiceImpl implements ProductDetailService {
    @Autowired
    private ProductDetailDao productDetailDao;

    @Override
    public void create(ProductDetail productDetail) {
        productDetailDao.create(productDetail);
    }

    @Override
    public ProductDetail findOne(int id) {
        return productDetailDao.findOne(id);
    }

    @Override
    public void update(ProductDetail productDetail) {
        productDetailDao.update(productDetail);
    }

    @Override
    public List<ProductDetail> getAll() {
        return productDetailDao.getAll();
    }

    @Override
    public ProductDetail findByName(String name) {
        return productDetailDao.findByName(name);
    }

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
        return productDetailDao.spGListProductDetail(
            productId, colorId, sizeId, materialId, 
            brandId, categoryId, keySearch, status, pagination
        );
    }

    @Override
    public List<ProductDetail> findByIds(List<Integer> ids) {
        return productDetailDao.findByIds(ids);
    }

    @Override
    public ProductDetail findBySku(String sku) {
        return productDetailDao.findBySku(sku);
    }

    @Override
    public ProductDetail findByBarcode(String barcode) {
        return productDetailDao.findByBarcode(barcode);
    }
}
