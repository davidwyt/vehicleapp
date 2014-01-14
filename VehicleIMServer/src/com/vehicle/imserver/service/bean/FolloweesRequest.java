package com.vehicle.imserver.service.bean;

public class FolloweesRequest implements IRequest{
	
	private String follower;
	
	public String getFollower()
	{
		return this.follower;
	}
	
	public void setFollower(String follower)
	{
		this.follower = follower;
	}
	
	@Override
	public String toString()
	{
		return String.format("request followees of %s", follower);
	}
}
