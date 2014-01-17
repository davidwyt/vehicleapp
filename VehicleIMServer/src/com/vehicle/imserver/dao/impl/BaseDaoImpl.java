package com.vehicle.imserver.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.vehicle.imserver.dao.interfaces.BaseDao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public class BaseDaoImpl<T> implements BaseDao<T> {
	
	@SuppressWarnings("unchecked")
	protected Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass()
			.getGenericSuperclass()).getActualTypeArguments()[0];
	
	private SessionFactory sessionFactory;
	
	public Session getSession() {  
        return sessionFactory.getCurrentSession();  
    }  
	
	public void delete(Serializable id) {
		T obj = this.load(id);
		getSession().delete(obj);
	}

	public T get(Serializable id) {
		return (T) getSession().get(entityClass, id);
	}

	public Serializable save(T obj) {
		return (Serializable) getSession().save(obj);
	}

	public void saveOrUpdate(T obj) {
		getSession().saveOrUpdate(obj);
	}

	public void update(T obj) {
		getSession().update(obj);
	}

	public void delete(T obj) {
		getSession().delete(obj);
	}

	public void merge(T obj) {
		getSession().merge(obj);
	}

	public T load(Serializable id) {
		return (T) getSession().load(entityClass, id);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
