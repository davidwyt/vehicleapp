package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlRootElement
public class FollowshipDroppedRequest implements IRequest{
	
	private String memberId;
	private String shopId;
	
	@XmlElement
	public String getMemberId()
	{
		return this.memberId;
	}
	
	public void setMemberId(String memberId)
	{
		this.memberId = memberId;
	}
	
	@XmlElement
	public String getShopId()
	{
		return this.shopId;
	}
	
	public void setShopId(String shopId)
	{
		this.shopId = shopId;
	}
}
