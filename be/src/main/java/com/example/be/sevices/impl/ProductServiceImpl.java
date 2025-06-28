package com.example.be.sevices.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.be.common.utils.Pagination;
import com.example.be.entity.Product;
import com.example.be.model.StoreProcedureListResult;
import com.example.be.repository.ProductDao;
import com.example.be.sevices.ProductService;

@Service("ProductService")
@Transactional(rollbackFor = Error.class)
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

    @Override
    public void create(Product product) {
        productDao.create(product);
    }

    @Override
    public Product findOne(int id) {
        return productDao.findOne(id);
    }

    @Override
    public void update(Product product) {
        productDao.update(product);
    }

    @Override
    public List<Product> getAll() {
        return productDao.getAll();
    }

    @Override
    public Product findByName(String name) {
        return productDao.findByName(name);
    }

    @Override
	public StoreProcedureListResult<Product> spGListProduct(String keySearch, int status,
			Pagination pagination) throws Exception {
		return productDao.spGListProduct(keySearch, status, pagination);
	}

    @Override
    public List<Product> findByIds(List<Integer> ids) {
        return productDao.findByIds(ids);
    }
}
