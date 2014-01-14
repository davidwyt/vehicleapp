package com.vehicle.imserver.service.bean;

public class FollowersRequest implements IRequest{
	
	private String followee;
	
	public String getFollowee()
	{
		return this.followee;
	}
	
	public void setFollowee(String followee)
	{
		this.followee = followee;
	}
	
	@Override
	public String toString()
	{
		return String.format("request followers of %s", followee);
	}
}
