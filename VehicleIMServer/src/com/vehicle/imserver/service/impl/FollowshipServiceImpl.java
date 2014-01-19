package com.vehicle.imserver.service.impl;

import java.util.List;

import com.vehicle.imserver.dao.interfaces.FollowshipDao;
import com.vehicle.imserver.service.exception.FollowshipAlreadyExistException;
import com.vehicle.imserver.service.exception.FollowshipNotExistException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.interfaces.FollowshipService;
import com.vehicle.imserver.utils.RequestDaoUtil;
import com.vehicle.service.bean.FolloweesRequest;
import com.vehicle.service.bean.FollowersRequest;
import com.vehicle.service.bean.FollowshipRequest;

public class FollowshipServiceImpl implements FollowshipService {
	
	FollowshipDao followshipDao;
	
	public FollowshipDao getFollowshipDao(){
		return this.followshipDao;
	}
	
	public void setFollowshipDao(FollowshipDao followshipDao){
		this.followshipDao=followshipDao;
	}
	
	public void AddFollowship(FollowshipRequest followshipReq) throws FollowshipAlreadyExistException, PersistenceException
	{
		try {
			followshipDao.AddFollowship(RequestDaoUtil.toRawFollowship(followshipReq));
		} catch (FollowshipAlreadyExistException e) {
			// TODO Auto-generated catch block
			throw e;
		}catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}
	
	public void DropFollowship(FollowshipRequest followshipReq) throws FollowshipNotExistException, PersistenceException
	{
		try {
			followshipDao.DropFollowship(RequestDaoUtil.toRawFollowship(followshipReq));
		} catch (FollowshipNotExistException e) {
			// TODO Auto-generated catch block
			throw e;
		}catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}
	
	public List<String> GetFollowers(FollowersRequest followersReq) throws PersistenceException
	{
		try{
			return followshipDao.GetFollowers(followersReq.getFollowee());
		}catch(Exception e)
		{
			throw new PersistenceException(e.getMessage(), e);
		}
	}
	
	public List<String> GetFollowees(FolloweesRequest followeesReq) throws PersistenceException, FollowshipAlreadyExistException
	{
			return followshipDao.GetFollowees(followeesReq.getFollower());
		
	}
	
}
