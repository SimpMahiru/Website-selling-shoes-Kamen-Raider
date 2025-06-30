package com.example.be.sevices.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.be.common.utils.Pagination;
import com.example.be.entity.Brand;
import com.example.be.model.StoreProcedureListResult;
import com.example.be.repository.BrandDao;
import com.example.be.sevices.BrandService;

@Service("BrandService")
@Transactional(rollbackFor = Error.class)
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandDao brandDao;

    @Override
    public void create(Brand brand) {
        brandDao.create(brand);
    }

    @Override
    public Brand findOne(int id) {
        return brandDao.findOne(id);
    }

    @Override
    public void update(Brand brand) {
        brandDao.update(brand);
    }

    @Override
    public List<Brand> getAll() {
        return brandDao.getAll();
    }

    @Override
    public Brand findByName(String name) {
        return brandDao.findByName(name);
    }

    @Override
    public StoreProcedureListResult<Brand> spGListBrand(
            String keySearch,
            int status,
            Pagination pagination) throws Exception {
        return brandDao.spGListBrand(keySearch, status, pagination);
    }

    @Override
    public List<Brand> findByIds(List<Integer> ids) {
        return brandDao.findByIds(ids);
    }
}
