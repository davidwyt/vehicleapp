package com.vehicle.imserver.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FollowersRequest implements IRequest{
	
	private String followee;
	
	@XmlElement
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
