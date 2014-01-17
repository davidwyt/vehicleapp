package com.vehicle.imserver.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vehicle.imserver.dao.interfaces.BaseDao;

@SuppressWarnings("unchecked")
public class BaseDaoImpl<T> extends HibernateDaoSupport implements BaseDao<T> {

	protected Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass()
			.getGenericSuperclass()).getActualTypeArguments()[0];

	public void delete(Serializable id) {
		T obj = this.load(id);
		getHibernateTemplate().delete(obj);
	}

	public T get(Serializable id) {
		return (T) getHibernateTemplate().get(entityClass, id);
	}

	public Serializable save(T obj) {
		return getHibernateTemplate().save(obj);
	}

	public void saveOrUpdate(T obj) {
		getHibernateTemplate().saveOrUpdate(obj);
	}

	public void update(T obj) {
		getHibernateTemplate().update(obj);
	}

	public void delete(T obj) {
		getHibernateTemplate().delete(obj);
	}

	public void merge(T obj) {
		getHibernateTemplate().merge(obj);
	}

	public T load(Serializable id) {
		return (T) getHibernateTemplate().load(entityClass, id);
	}

	public List<T> loadAll() {
		return getHibernateTemplate().loadAll(entityClass);
	}

	public int countByCriteria(final DetachedCriteria detachedCriteria) {
		Long count = getHibernateTemplate().execute(
				new HibernateCallback<Long>() {
					public Long doInHibernate(Session session)
							throws HibernateException {
						Criteria criteria = detachedCriteria
								.getExecutableCriteria(session);
						return (Long) criteria.setProjection(
								Projections.rowCount()).uniqueResult();
					}
				});
		return count.intValue();
	}

	public List<T> findByCriteria(DetachedCriteria detachedCriteria) {
		return getHibernateTemplate().findByCriteria(detachedCriteria);
	}

	public List<T> findByCriteria(DetachedCriteria detachedCriteria, int first,
			int max) {
		return getHibernateTemplate().findByCriteria(detachedCriteria, first,
				max);
	}

	public List<T> findByExample(T obj) {
		return getHibernateTemplate().findByExample(obj);
	}

	@Override
	public List<T> findByHql(String hql, Object[] params, int start, Integer max) {
		Query query = getHibernateSession().createQuery(hql);
		if (null != params && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		query.setFirstResult(0);
		if (max != null) {
			query.setFetchSize(max);
		}
		return query.list();
	}

	public Session getHibernateSession() {
		return SessionFactoryUtils.getSession(getHibernateTemplate()
				.getSessionFactory(), false);
	}

	@Override
	public int updateByHql(String hql, Object[] params) {
		Query query = getHibernateSession().createQuery(hql);
		if (null != params && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		return query.executeUpdate();
	}

	@Override
	public int updateBySql(String sql, Object[] params) {
		Query query = getHibernateSession().createSQLQuery(sql);
		if (null != params && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
		}
		return query.executeUpdate();
	}

	@Override
	public int updateColumns(Serializable id, String[] columns, Object[] params) {
		List<Integer> abandonList = new ArrayList<Integer>();
		String property;
		int escape=0;
		if (null != columns && columns.length > 0 && null != params
				&& columns.length == params.length) {
			StringBuilder sb = new StringBuilder();
			sb.append("update ").append(entityClass.getName()).append(" set ");
			
			for (int i = 0; i < columns.length; i++) {
				try {
					if(params[i] instanceof Boolean){
						property = columns[i].indexOf('.')==-1?columns[i]:columns[i].substring(0,columns[i].indexOf('.'));
						entityClass.getMethod("is"
								+ property.substring(0, 1).toUpperCase()
								+ property.substring(1));
					}else{
						property = columns[i].indexOf('.')==-1?columns[i]:columns[i].substring(0,columns[i].indexOf('.'));
						entityClass.getMethod("get"
								+ property.substring(0, 1).toUpperCase()
								+ property.substring(1));
					}					
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
					abandonList.add(new Integer(i));
					escape++;
					continue;
				} catch (Exception e) {
				}
				//Liuchang: Remove all the invalid parameters
				if ((i-escape)!=0) {
					sb.append(",");
				}
				sb.append(columns[i]).append("=?");
			}
			sb.append(" where id=?");
			Query query = getHibernateSession().createQuery(sb.toString());
			int j=0;
			for (int i = 0; i < params.length; i++) {
				if (!abandonList.contains(i)) {
					query.setParameter(j++, params[i]);
				}
			}
			query.setParameter(columns.length-abandonList.size(), id);
			return query.executeUpdate();
		}
		return -1;
	}
	
}

