package com.vehicle.app.bean;

public class Message {
	
	private String id;
	private String source;
	private String target;
	private long sentTime;
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
	
	public long getSentDate()
	{
		return this.sentTime;
	}
	
	public void setSentDate(long sentD)
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
