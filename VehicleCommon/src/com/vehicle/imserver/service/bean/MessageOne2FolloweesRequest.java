package com.vehicle.imserver.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageOne2FolloweesRequest implements IRequest{
	
	private String source;
	private String content;
	
	@XmlElement
	public String getSource()
	{
		return this.source;
	}
	
	public void setSource(String source)
	{
		this.source = source;
	}
	
	@XmlElement
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
