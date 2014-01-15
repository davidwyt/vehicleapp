package com.vehicle.imserver.service.bean;

public class FileFetchRequest implements IRequest{
	
	private String token;
	
	public String getToken()
	{
		return this.token;
	}
	
	public void setToken(String token)
	{
		this.token = token;
	}
}
