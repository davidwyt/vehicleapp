package com.vehicle.imserver.dao.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "OFFLINEMESSAGE")
public class OfflineMessage {
	
	private String id;
	private String source;
	private String target;
	private Date sentTime;
	private String content;
	
	public OfflineMessage(){}
	
	public OfflineMessage(Message om){
		this.id=om.getId();
		this.source=om.getSource();
		this.target=om.getTarget();
		this.sentTime=om.getSentDate();
		this.content=om.getContent();
	}
	
	@Id
	@Column(name = "ID")
	public String getId()
	{
		return this.id;
	}
	
	public void setId(String iid)
	{
		this.id = iid;
	}
	
	@Column(name = "SOURCE")
	public String getSource()
	{
		return this.source;
	}
	
	public void setSource(String s)
	{
		this.source = s;
	}
	
	@Column(name = "TARGET")
	public String getTarget()
	{
		return this.target;
	}
	
	public void setTarget(String tar)
	{
		this.target = tar;
	}
	
	@Column(name = "SENTTIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getSentDate()
	{
		return this.sentTime;
	}
	
	public void setSentDate(Date sentD)
	{
		this.sentTime = sentD;
	}
	
	@Column(name = "CONTENT")
	public String getContent()
	{
		return this.content;
	}
	
	public void setContent(String c)
	{
		this.content = c;
	}
}
