package com.vehicle.imserver.dao.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "FILETRANSMISSION")
public class FileTransmission {
	
	private String token;
	private String path;
	private String source;
	private String target;
	private FileTransmissionStatus status;
	private Date transmissionTime;
	
	@Id
	@Column(name="TOKEN")
	public String getToken()
	{
		return this.token;
	}
	
	public void setToken(String token)
	{
		this.token = token;
	}
	
	@Column(name="PATH")
	public String getPath()
	{
		return this.path;
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
	@Column(name="SOURCE")
	public String getSource()
	{
		return this.source;
	}
	
	public void setSource(String source)
	{
		this.source = source;
	}
	
	@Column(name="TARGET")
	public String getTarget()
	{
		return this.target;
	}
	
	public void setTarget(String target)
	{
		this.target = target;
	}
	
	@Column(name="STATUS")
	public FileTransmissionStatus getStatus()
	{
		return this.status;
	}
	
	public void setStatus(FileTransmissionStatus status)
	{
		this.status = status;
	}
	
	@Column(name="TRANSMISSIONTIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getTransmissionTime()
	{
		return this.transmissionTime;
	}
	
	public void setTransmissionTime(Date time)
	{
		this.transmissionTime = time;
	}
}
