package com.vehicle.imserver.dao.interfaces;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

public interface BaseDao<T> {

	/**
	 * Description: Save the bean to database.
	 * 
	 * @param obj
	 *            to be saved bean
	 * @return Long identity id
	 */
	public Long save(T obj);
	
	/**
	 * Description: Save the bean to database. If this record exists, update it.
	 * 
	 * @param obj
	 *            to be saved bean
	 * @return Long identity id
	 */
	public void saveOrUpdate(T obj);

	/**
	 * Description: Get the bean by its identity id.
	 * 
	 * @param id
	 *            identity id
	 * @return Bean
	 */
	public T get(Long id);

	/**
	 * Description: Load the bean by its identity id.
	 * 
	 * @param id
	 *            identity id
	 * @return Bean
	 */
	public T load(Long id);

	/**
	 * Description: Update the bean.
	 * 
	 * @param obj
	 *            to be updated bean
	 */
	public void update(T obj);

	/**
	 * Description: Merge the bean.
	 * 
	 * @param obj
	 *            to be merged bean
	 */
	public void merge(T obj);

	/**
	 * Description: Delete the bean by its identity id.
	 * 
	 * @param id
	 *            identity id
	 */
	public void delete(Long id);

	/**
	 * Description: Delete the bean.
	 * 
	 * @param id
	 *            identity id
	 */
	public void delete(T obj);

	/**
	 * Description: Load all beans.
	 * 
	 * @return List
	 * 
	 */
	public List<T> loadAll();

	/**
	 * Description: find all the entries according to query criteria.
	 * 
	 * @return List
	 * 
	 */
	public List<T> findByCriteria(DetachedCriteria detachedCriteria);

	/**
	 * Description: find all the entries according to query criteria.
	 * 
	 * @return List
	 * 
	 */
	public List<T> findByCriteria(DetachedCriteria detachedCriteria, int first,
			int max);

	/**
	 * Description: find all the entries according to example.
	 * 
	 * @return List
	 * 
	 */
	public List<T> findByExample(T obj);

	/**
	 * Description: find all the entries according to hql.
	 * 
	 * @return List
	 * 
	 */
	public List<T> findByHql(String hql, Object[] params, int start, Integer max);

	/**
	 * Description: update by hql
	 * 
	 * @return
	 * 
	 */
	public int updateByHql(String hql, Object[] params);

	/**
	 * Description: update by sql
	 * 
	 * @return
	 * 
	 */
	public int updateBySql(String sql, Object[] params);

	/**
	 * Description: update the entry's columns by id.
	 * 
	 * @return
	 * 
	 */
	public int updateColumns(Long id, String[] columns, Object[] params);
	
	/**
	 * Description: count all the entries according to query criteria.
	 * 
	 * @return count
	 * 
	 */
	public int countByCriteria(DetachedCriteria detachedCriteria);

	/**
	 * Description: get hibernate session.
	 * 
	 * @return count
	 * 
	 */
	public Session getHibernateSession();
	

}
