package com.vehicle.imserver.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FolloweesRequest implements IRequest{
	
	private String follower;
	
	@XmlElement
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
