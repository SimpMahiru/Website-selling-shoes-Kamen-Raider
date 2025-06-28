package com.example.be.sevices.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.be.common.utils.Pagination;
import com.example.be.entity.Category;
import com.example.be.model.StoreProcedureListResult;
import com.example.be.repository.CategoryDao;
import com.example.be.sevices.CategoryService;

/**
 * 
 * @author 
 *
 */
@Service("CategoryService")
@Transactional(rollbackFor = Error.class)
public class CategoryServiceImpl implements CategoryService {
    @Autowired
	CategoryDao categoryDao;

	@Override
	public void create(Category category) {
		categoryDao.create(category);
	}

	@Override
	public Category findOne(int id) {
		return categoryDao.findOne(id);
	}

	@Override
	public void update(Category category) {
		categoryDao.update(category);
	}

	@Override
	public Category findByName(String name) {
		return categoryDao.findByName(name);
	}

	@Override
	public List<Category> getAll() {
		return categoryDao.getAll();
	}

	@Override
	public StoreProcedureListResult<Category> spGListCategory(
			int parentId,
			String keySearch,
			int status,
			Pagination pagination) throws Exception {
		return categoryDao.spGListCategory(parentId, keySearch, status, pagination);
	}

    @Override
    public List<Category> findByIds(List<Integer> ids) {
        return categoryDao.findByIds(ids);
    }
}
