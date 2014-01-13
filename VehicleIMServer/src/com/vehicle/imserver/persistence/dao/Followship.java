package com.vehicle.imserver.persistence.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FOLLOWSHIP" )
public class Followship {
	
	private String follower;
	private String followee;
	
	@Column(name = "FOLLOWER")
	public String getFollower()
	{
		return this.follower;
	}
	
	public void setFollower(String follower)
	{
		this.follower = follower;
	}
	
	@Column(name = "FOLLOWEE")
	public String getFollowee()
	{
		return this.followee;
	}
	
	public void setFollowee(String followee)
	{
		this.followee = followee;
	}
}
