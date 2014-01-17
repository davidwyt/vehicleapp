package com.vehicle.imserver.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.vehicle.imserver.dao.bean.Followship;

@XmlRootElement
public class FollowshipRequest implements IRequest{
	
	private String follower;
	private String followee;
	
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
