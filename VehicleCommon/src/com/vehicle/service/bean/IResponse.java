package com.vehicle.service.bean;

public interface IResponse {
	
	public void setErrorCode(int errCode);
	public int getErrorCode();
	
	public void setErrorMsg(String errMsg);
	public String getErrorMsg();
}
