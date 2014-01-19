package com.vehicle.service.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FolloweesResponse extends BaseResponse{
	
	private String follower;
	private List<String> followees;
	
	@XmlElement
	public String getFollower()
	{
		return this.follower;
	}
	
	public void setFollower(String follower)
	{
		this.follower = follower;
	}
	
	@XmlElement
	public List<String> getFollowees()
	{
		return this.followees;
	}
	
	public void setFollowees(List<String> followees)
	{
		this.followees = followees;
	}
}
