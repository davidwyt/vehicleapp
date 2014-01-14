package com.vehicle.imserver.service.bean;

import java.util.List;

public class FolloweesResponse extends BaseResponse{
	
	private String follower;
	private List<String> followees;
	
	public String getFollower()
	{
		return this.follower;
	}
	
	public void setFollower(String follower)
	{
		this.follower = follower;
	}
	
	public List<String> getFollowees()
	{
		return this.followees;
	}
	
	public void setFollowees(List<String> followees)
	{
		this.followees = followees;
	}
}
