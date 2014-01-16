package com.vehicle.imserver.service.bean;

public class BaseResponse implements IResponse{

	private int errorCode;
	private String errorMsg;
	
	@Override
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
