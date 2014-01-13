package com.vehicle.imserver.persistence.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FOLLOWSHIP" )
public class Followship implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8456290910176799662L;
	private String follower;
	private String followee;
	
	@Id
	@Column(name = "FOLLOWER")
	public String getFollower()
	{
		return this.follower;
	}
	
	public void setFollower(String follower)
	{
		this.follower = follower;
	}
	
	@Id
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
