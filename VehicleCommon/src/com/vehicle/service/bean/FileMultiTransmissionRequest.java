package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileMultiTransmissionRequest implements IRequest {

	private String source;
	private String targets;
	
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
	public String getTargets()
	{
		return this.targets;
	}
	
	public void setTargets(String targets)
	{
		this.targets = targets;
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
		return String.format("% send file: % to %", source, fileName, targets);
	}
}
