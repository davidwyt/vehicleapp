package com.vehicle.app.bean;

import java.util.Date;

public class Message {
	
	private String id;
	private String source;
	private String target;
	private Date sentTime;
	private String content;
	private MessageStatus status;
	
	public String getId()
	{
		return this.id;
	}
	
	public void setId(String iid)
	{
		this.id = iid;
	}
	
	public String getSource()
	{
		return this.source;
	}
	
	public void setSource(String s)
	{
		this.source = s;
	}
	
	public String getTarget()
	{
		return this.target;
	}
	
	public void setTarget(String tar)
	{
		this.target = tar;
	}
	
	public Date getSentDate()
	{
		return this.sentTime;
	}
	
	public void setSentDate(Date sentD)
	{
		this.sentTime = sentD;
	}
	
	public String getContent()
	{
		return this.content;
	}
	
	public void setContent(String c)
	{
		this.content = c;
	}
	
	public MessageStatus getStatus()
	{
		return this.status;
	}
	
	public void setStatus(MessageStatus s)
	{
		this.status = s;
	}
}
