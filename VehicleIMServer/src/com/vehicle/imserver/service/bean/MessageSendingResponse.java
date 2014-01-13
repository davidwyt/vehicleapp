package com.vehicle.imserver.service.bean;

public class MessageSendingResponse {
	private String msgId;
	
	private int errorCode;
	private String errorMsg;
	
	public void setMsgId(String id)
	{
		msgId = id;
	}
	
	public String getMsgId()
	{
		return this.msgId;
	}
	
	public int getErrorCode()
	{
		return this.errorCode;
	}
	public void setErrorCode(int errorCode)
	{
		this.errorCode = errorCode;
	}
	
	public String getErrorMsg()
	{
		return this.errorMsg;
	}
	public void setErrorMsg(String errMsg)
	{
		this.errorMsg = errMsg;
	}
	
}
