package com.vehicle.imserver.service.bean;

public class MessageACKResponse extends BaseResponse{
	private String msgId;
	
	public String getMsgId()
	{
		return this.msgId;
	}
	public void setMsgId(String id)
	{
		msgId = id;
	}
}