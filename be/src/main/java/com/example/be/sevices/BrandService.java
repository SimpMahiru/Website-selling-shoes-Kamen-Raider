package com.example.be.sevices;

import java.util.List;

import com.example.be.common.utils.Pagination;
import com.example.be.entity.Brand;
import com.example.be.model.StoreProcedureListResult;

public interface BrandService {
    void create(Brand brand);

    Brand findOne(int id);

    void update(Brand brand);

    List<Brand> getAll();

    StoreProcedureListResult<Brand> spGListBrand(
        String keySearch,
        int status,
        Pagination pagination
    ) throws Exception;

    Brand findByName(String name);

    List<Brand> findByIds(List<Integer> ids);
}
