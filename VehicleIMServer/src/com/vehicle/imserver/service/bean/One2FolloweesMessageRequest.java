package com.vehicle.imserver.service.bean;

public class One2FolloweesMessageRequest implements IRequest{
	
	private String source;
	private String content;
	
	public String getSource()
	{
		return this.source;
	}
	
	public void setSource(String source)
	{
		this.source = source;
	}
	
	public String getContent()
	{
		return this.content;
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s send message:%s to his/her fellowees", this.source, this.content);
	}
}
