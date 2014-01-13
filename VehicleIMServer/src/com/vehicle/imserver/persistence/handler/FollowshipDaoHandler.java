package com.vehicle.imserver.persistence.handler;

import org.hibernate.Session;

import com.vehicle.imserver.persistence.dao.Followship;
import com.vehicle.imserver.service.exception.FollowshipAlreadyExistException;
import com.vehicle.imserver.service.exception.FollowshipNotExistException;
import com.vehicle.imserver.utils.HibernateUtil;

public class FollowshipDaoHandler {
	
	private FollowshipDaoHandler()
	{
		
	}
	
	public static void AddFollowship(Followship followship) throws FollowshipAlreadyExistException
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
	
	public static void DropFollowship(Followship followship) throws FollowshipNotExistException
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
	
	public static void GetFollowees(String Follower)
	{
		
	}
	
	public static void GetFollowers(String Followee)
	{
		
	}
}
