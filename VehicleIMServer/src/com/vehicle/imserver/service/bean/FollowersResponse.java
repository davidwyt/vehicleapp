package com.vehicle.imserver.service.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FollowersResponse extends BaseResponse{
	
	private String followee;
	private List<String> followers;
	
	@XmlElement
	public String getFollowee()
	{
		return this.followee;
	}
	
	public void setFollowee(String followee)
	{
		this.followee = followee;
	}
	
	@XmlElement
	public List<String> getFollowers()
	{
		return this.followers;
	}
	
	public void setFollowers(List<String> followers)
	{
		this.followers = followers;
	}
}
