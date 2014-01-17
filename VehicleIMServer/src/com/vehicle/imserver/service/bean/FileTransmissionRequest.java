package com.vehicle.imserver.service.bean;

import java.util.Date;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.vehicle.imserver.dao.bean.FileTransmission;
import com.vehicle.imserver.dao.bean.FileTransmissionStatus;

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
	
	public FileTransmission toRawDao(String filePath)
	{
		FileTransmission fileTran = new FileTransmission();
		fileTran.setSource(source);
		fileTran.setTarget(target);
		fileTran.setStatus(FileTransmissionStatus.SENT);
		fileTran.setToken(UUID.randomUUID().toString());
		fileTran.setTransmissionTime(new Date());
		fileTran.setPath(filePath);
		
		return fileTran;
	}
}
