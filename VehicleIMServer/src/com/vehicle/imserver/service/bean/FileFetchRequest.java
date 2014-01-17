package com.vehicle.imserver.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileFetchRequest implements IRequest{
	
	private String token;
	
	@XmlElement
	public String getToken()
	{
		return this.token;
	}
	
	public void setToken(String token)
	{
		this.token = token;
	}
}
