package com.vehicle.imserver.service.bean;

public class MessageACKResponse {
	private String msgId;
	private MessageACKStatus status;
	
	private int errorCode;
	private String errorMsg;
	
	public String getMsgId()
	{
		return this.msgId;
	}
	public void setMsgId(String id)
	{
		msgId = id;
	}
	
	public MessageACKStatus getStatus()
	{
		return this.status;
	}
	public void setStatus(MessageACKStatus status)
	{
		this.status = status;
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
