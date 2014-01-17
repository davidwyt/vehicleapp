package com.vehicle.imserver.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.vehicle.imserver.dao.bean.Followship;
import com.vehicle.imserver.dao.interfaces.FollowshipDao;
import com.vehicle.imserver.service.exception.FollowshipAlreadyExistException;
import com.vehicle.imserver.service.exception.FollowshipNotExistException;

public class FollowshipDaoImpl extends BaseDaoImpl<Followship> implements FollowshipDao {
	
	public void AddFollowship(Followship followship) throws FollowshipAlreadyExistException
	{
		
		Followship oldship = this.get(followship);
		
		if(null != oldship)
		{
			throw new FollowshipAlreadyExistException(); 
		} else {
			this.save(followship);
		}
	}
	
	public void DropFollowship(Followship followship) throws FollowshipNotExistException
	{
		Followship oldship = this.get(followship);
		
		if(null == oldship)
		{
			throw new FollowshipNotExistException(); 
		} else {
			this.delete(followship);
		}
	}
	
	public List<String> GetFollowees(String follower)
	{
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Followship.class);
		detachedCriteria.add(Restrictions.eq("follower",follower));
		List<Followship> list=this.findByCriteria(detachedCriteria);
		List<String> followees =new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			followees.add(list.get(i).getFollowee());
		}
		return followees;
	}
	
	public List<String> GetFollowers(String followee)
	{
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Followship.class);
		detachedCriteria.add(Restrictions.eq("followee",followee));
		List<Followship> list=this.findByCriteria(detachedCriteria);
		List<String> followers =new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			followers.add(list.get(i).getFollower());
		}
		return followers;
	}
}
