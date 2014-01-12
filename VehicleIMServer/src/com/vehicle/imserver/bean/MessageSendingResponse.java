package com.vehicle.imserver.bean;

public class MessageSendingResponse {
	private String msgId;
	private MessageSendingStatus status;
	
	public void setMsgId(String id)
	{
		msgId = id;
	}
	
	public void setStatus(MessageSendingStatus status)
	{
		this.status = status;
	}
	
	public String getMsgId()
	{
		return this.msgId;
	}
	
	public MessageSendingStatus getStatus()
	{
		return this.status;
	}
}
