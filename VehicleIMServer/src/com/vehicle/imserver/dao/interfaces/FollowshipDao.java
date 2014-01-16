package com.vehicle.imserver.dao.interfaces;

import java.util.List;

import com.vehicle.imserver.dao.bean.Followship;
import com.vehicle.imserver.service.exception.FollowshipAlreadyExistException;
import com.vehicle.imserver.service.exception.FollowshipNotExistException;

public interface FollowshipDao extends BaseDao<Followship>{

	public List<String> GetFollowers(String followee);
	public List<String> GetFollowees(String follower);
	public void DropFollowship(Followship followship) throws FollowshipNotExistException;
	public void AddFollowship(Followship followship) throws FollowshipAlreadyExistException;
}
