package com.vehicle.imserver.service.bean;

import com.vehicle.imserver.persistence.dao.Followship;

public class FollowshipRequest implements IRequest{
	
	private String follower;
	private String followee;
	
	public String getFollower()
	{
		return this.follower;
	}
	
	public void setFollower(String follower)
	{
		this.follower = follower;
	}
	
	public String getFollowee()
	{
		return this.followee;
	}
	
	public void setFollowee(String followee)
	{
		this.followee = followee;
	}
	
	public Followship toRawFollowship()
	{
		Followship ship = new Followship();
		ship.setFollower(follower);
		ship.setFollowee(followee);
		return ship;
	}
}
