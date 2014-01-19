package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileTransmissionRequest implements IRequest{
	private String source;
	private String target;
	
	private String fileName;
	
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
	public String getTarget()
	{
		return this.target;
	}
	
	public void setTarget(String target)
	{
		this.target = target;
	}
	
	@XmlElement
	public String getFileName()
	{
		return this.fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	@Override
	public String toString()
	{
		return String.format("% send file: % to %", source, fileName, target);
	}
}
