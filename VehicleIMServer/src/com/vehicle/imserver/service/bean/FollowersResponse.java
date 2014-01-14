package com.vehicle.imserver.service.bean;

import java.util.List;

public class FollowersResponse extends BaseResponse{
	
	private String followee;
	private List<String> followers;
	
	public String getFollowee()
	{
		return this.followee;
	}
	
	public void setFollowee(String followee)
	{
		this.followee = followee;
	}
	
	public List<String> getFollowers()
	{
		return this.followers;
	}
	
	public void setFollowers(List<String> followers)
	{
		this.followers = followers;
	}
}
