package com.example.be.repository;

import java.util.List;

import com.example.be.common.utils.Pagination;
import com.example.be.entity.Product;
import com.example.be.model.StoreProcedureListResult;

public interface ProductDao {
    void create(Product product);

    Product findOne(int id);

    void update(Product product);

    List<Product> getAll();

    StoreProcedureListResult<Product> spGListProduct(String keySearch,int status,Pagination pagination) throws Exception;

    Product findByName(String name);

    List<Product> findByIds(List<Integer> ids);
}