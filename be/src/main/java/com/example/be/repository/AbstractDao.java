package com.example.be.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.hibernate.Session;

import com.example.be.entity.BaseEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;


public abstract class AbstractDao<PK extends Serializable, T> {
//	@Autowired(required=true)
//	@Qualifier("entityManager")
	@PersistenceContext
	private EntityManager entityManager;

	private final Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	protected AbstractDao() {
		this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[1];
	}

	protected Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	protected CriteriaBuilder getBuilder() {
		return entityManager.getCriteriaBuilder();
	}

	public T getByKey(PK key) {
		return (T) getSession().get(persistentClass, key);
	}

	public void persist(T entity) {
		getSession().persist(entity);
	}

	public void delete(T entity) {
		getSession().delete(entity);
	}
//
//	@SuppressWarnings("deprecation")
//	protected Criteria createEntityCriteria() {
//		return getSession().createCriteria(persistentClass);
//	}

	public void update(BaseEntity entity) {
		this.getSession().update(entity);
	}

	public void merge(BaseEntity entity) {
		this.getSession().merge(entity);
	}

	public void delete(BaseEntity entity) {
		this.getSession().delete(entity);
	}

	public void refresh(BaseEntity entity) {
		this.getSession().refresh(entity);
	}

	public void flush() {
		this.getSession().flush();
	}

}