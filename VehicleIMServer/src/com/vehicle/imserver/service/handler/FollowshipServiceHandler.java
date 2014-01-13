package com.vehicle.imserver.service.handler;

import com.vehicle.imserver.persistence.handler.FollowshipDaoHandler;
import com.vehicle.imserver.service.bean.FollowshipRequest;
import com.vehicle.imserver.service.exception.FollowshipAlreadyExistException;
import com.vehicle.imserver.service.exception.FollowshipNotExistException;
import com.vehicle.imserver.service.exception.PersistenceException;

public class FollowshipServiceHandler {
	
	private FollowshipServiceHandler()
	{
		
	}
	
	public static void AddFollowship(FollowshipRequest followshipReq) throws FollowshipAlreadyExistException, PersistenceException
	{
		try {
			FollowshipDaoHandler.AddFollowship(followshipReq.toRawFollowship());
		} catch (FollowshipAlreadyExistException e) {
			// TODO Auto-generated catch block
			throw e;
		}catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}
	
	public static void DropFollowship(FollowshipRequest followshipReq) throws FollowshipNotExistException, PersistenceException
	{
		try {
			FollowshipDaoHandler.DropFollowship(followshipReq.toRawFollowship());
		} catch (FollowshipNotExistException e) {
			// TODO Auto-generated catch block
			throw e;
		}catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}
}
