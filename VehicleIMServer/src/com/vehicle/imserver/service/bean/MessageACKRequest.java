package com.vehicle.imserver.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageACKRequest implements IRequest{
	
	private String msgId;
	
	@XmlElement
	public String getMsgId()
	{
		return this.msgId;
	}
	
	public void setMsgId(String id)
	{
		this.msgId = id;
	}
	
	@Override
	public String toString()
	{
		return String.format("message:%s received", msgId);
	}
}
