package com.example.be.sevices;

import java.util.List;

import com.example.be.common.utils.Pagination;
import com.example.be.entity.Category;
import com.example.be.model.StoreProcedureListResult;

public interface CategoryService {
	void create(Category category);

	Category findOne(int id);
	
	List<Category> getAll();

	void update(Category category);
	
	Category findByName(String name);

	List<Category> findByIds(List<Integer> ids);

	StoreProcedureListResult<Category> spGListCategory(
		int parentId,
		String keySearch,
		int status,
		Pagination pagination
	) throws Exception;
}
