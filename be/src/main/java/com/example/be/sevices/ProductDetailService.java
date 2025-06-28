package com.example.be.sevices;

import java.util.List;

import com.example.be.common.utils.Pagination;
import com.example.be.entity.ProductDetail;
import com.example.be.model.StoreProcedureListResult;

public interface ProductDetailService {
    void create(ProductDetail productDetail);

    ProductDetail findOne(int id);

    void update(ProductDetail productDetail);

    List<ProductDetail> getAll();

    StoreProcedureListResult<ProductDetail> spGListProductDetail(
        int productId, 
        int colorId, 
        int sizeId, 
        int materialId,
        int brandId,
        int categoryId, 
        String keySearch,
        int status,
        Pagination pagination
    ) throws Exception;

    ProductDetail findByName(String name);
    
    List<ProductDetail> findByIds(List<Integer> ids);

    ProductDetail findBySku(String sku);

    ProductDetail findByBarcode(String barcode);
}
