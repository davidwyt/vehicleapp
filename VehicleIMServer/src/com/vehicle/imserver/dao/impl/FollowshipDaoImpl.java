package com.vehicle.imserver.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vehicle.imserver.dao.bean.FileTransmission;
import com.vehicle.imserver.dao.bean.Followship;
import com.vehicle.imserver.dao.interfaces.FollowshipDao;
import com.vehicle.imserver.service.exception.FollowshipAlreadyExistException;
import com.vehicle.imserver.service.exception.FollowshipNotExistException;
import com.vehicle.imserver.utils.Contants;
import com.vehicle.imserver.utils.HibernateUtil;

public class FollowshipDaoImpl extends BaseDaoImpl<Followship> implements FollowshipDao {
	
	public void AddFollowship(Followship followship) throws FollowshipAlreadyExistException
	{
		Session session = HibernateUtil.getInstance().OpenSession();
		
		session.beginTransaction();
		
		Followship oldship = (Followship)session.get(Followship.class, followship);
		
		if(null != oldship)
		{
			session.getTransaction().commit();
			session.close();
			
			throw new FollowshipAlreadyExistException(); 
		}
		
		session.save(followship);
		
		session.getTransaction().commit();
		session.close();
	}
	
	public void DropFollowship(Followship followship) throws FollowshipNotExistException
	{
		Session session = HibernateUtil.getInstance().OpenSession();
		
		session.beginTransaction();
		
		Followship oldship = (Followship)session.get(Followship.class, followship);
		
		if(null == oldship)
		{
			session.getTransaction().commit();
			session.close();
			
			throw new FollowshipNotExistException(); 
		}
		
		session.delete(followship);
		
		session.getTransaction().commit();
		session.close();
	}
	
	public List<String> GetFollowees(String follower)
	{
		Session session = HibernateUtil.getInstance().OpenSession();
		
		session.beginTransaction();
		
		Query query = session.createQuery(Contants.HQL_SELECT_FOLLOWEES);
		query.setString("follower", follower);
		
		List<String> followees = query.list();
		
		session.getTransaction().commit();
		session.close();
		
		return followees;
	}
	
	public List<String> GetFollowers(String followee)
	{
		Session session = HibernateUtil.getInstance().OpenSession();
		
		session.beginTransaction();
		
		Query query = session.createQuery(Contants.HQL_SELECT_FOLLOWERS);
		query.setString("followee", followee);
		
		List<String> followers = query.list();
		
		session.getTransaction().commit();
		session.close();
		
		return followers;
	}
}
