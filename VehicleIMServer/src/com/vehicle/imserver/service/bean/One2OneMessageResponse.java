package com.vehicle.imserver.service.bean;

public class One2OneMessageResponse extends BaseResponse{
	private String msgId;
	
	public void setMsgId(String id)
	{
		msgId = id;
	}
	
	public String getMsgId()
	{
		return this.msgId;
	}
}
