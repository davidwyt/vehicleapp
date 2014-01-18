package com.vehicle.imserver.dao.interfaces;

import java.io.Serializable;

public interface BaseDao<T> {

	/**
	 * Description: Save the bean to database.
	 * 
	 * @param obj
	 *            to be saved bean
	 * @return Long identity id
	 */
	public Serializable save(T obj);
	
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
	public T get(Serializable id);

	/**
	 * Description: Load the bean by its identity id.
	 * 
	 * @param id
	 *            identity id
	 * @return Bean
	 */
	public T load(Serializable id);

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
	public void delete(Serializable id);

	/**
	 * Description: Delete the bean.
	 * 
	 * @param id
	 *            identity id
	 */
	public void delete(T obj);
	

}

