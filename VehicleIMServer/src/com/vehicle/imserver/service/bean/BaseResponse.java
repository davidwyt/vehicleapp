package com.vehicle.imserver.service.bean;

import javax.xml.bind.annotation.XmlElement;

public class BaseResponse implements IResponse{

	private int errorCode;
	private String errorMsg;
	
	@Override
	@XmlElement
	public int getErrorCode()
	{
		return this.errorCode;
	}
	
	@Override
	public void setErrorCode(int errorCode)
	{
		this.errorCode = errorCode;
	}
	
	@Override
	@XmlElement
	public String getErrorMsg()
	{
		return this.errorMsg;
	}
	
	@Override
	public void setErrorMsg(String errMsg)
	{
		this.errorMsg = errMsg;
	}
	
	@Override
	public String toString()
	{
		return String.format("errorcode:%s errormsg:%s", this.errorCode, this.errorMsg);
	}

}
