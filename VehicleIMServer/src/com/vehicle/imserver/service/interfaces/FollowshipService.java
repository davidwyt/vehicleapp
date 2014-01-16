package com.vehicle.imserver.service.interfaces;

import java.util.List;

import com.vehicle.imserver.service.bean.FolloweesRequest;
import com.vehicle.imserver.service.bean.FollowersRequest;
import com.vehicle.imserver.service.bean.FollowshipRequest;
import com.vehicle.imserver.service.exception.FollowshipAlreadyExistException;
import com.vehicle.imserver.service.exception.FollowshipNotExistException;
import com.vehicle.imserver.service.exception.PersistenceException;

public interface FollowshipService {
	
	public void AddFollowship(FollowshipRequest followshipReq) throws FollowshipAlreadyExistException, PersistenceException;
	public void DropFollowship(FollowshipRequest followshipReq) throws FollowshipNotExistException, PersistenceException;
	public List<String> GetFollowers(FollowersRequest followersReq) throws PersistenceException;
	public List<String> GetFollowees(FolloweesRequest followeesReq) throws PersistenceException;

}
