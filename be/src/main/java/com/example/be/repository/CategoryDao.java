package com.example.be.repository;

import java.util.List;

import com.example.be.common.utils.Pagination;
import com.example.be.entity.Category;
import com.example.be.model.StoreProcedureListResult;

public interface CategoryDao {
    void create(Category category);

	Category findOne(int id);
	
	void update(Category category);

	List<Category> getAll();
	
	StoreProcedureListResult<Category> spGListCategory(
        int parentId,
        String keySearch,
        int status,
        Pagination pagination
    ) throws Exception;

	Category findByName(String name);
    
    List<Category> findByIds(List<Integer> ids);
}
