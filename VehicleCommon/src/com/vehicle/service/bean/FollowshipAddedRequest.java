package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FollowshipAddedRequest implements IRequest{
	
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
	
	@Override
	public String toString()
	{
		return String.format("%s followed %s", this.memberId, this.shopId);
	}
}
