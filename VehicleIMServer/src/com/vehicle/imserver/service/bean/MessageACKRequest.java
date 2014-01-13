package com.vehicle.imserver.service.bean;

public class MessageACKRequest {
	
	private String msgId;
	
	public String getMsgId()
	{
		return this.msgId;
	}
	
	public void setMsgId(String id)
	{
		this.msgId = id;
	}
	
	@Override
	public String toString()
	{
		return String.format("message:%s received", msgId);
	}
}
