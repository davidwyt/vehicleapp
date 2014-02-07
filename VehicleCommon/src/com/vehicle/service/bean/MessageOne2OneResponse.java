package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageOne2OneResponse extends BaseResponse{
	
	private String msgId;
	private long msgSentTime;
	
	public void setMsgId(String id)
	{
		msgId = id;
	}
	
	@XmlElement
	public String getMsgId()
	{
		return this.msgId;
	}
	
	public void setMsgSentTime(long date)
	{
		this.msgSentTime = date;
	}
	
	@XmlElement
	public long getMsgSentTime()
	{
		return this.msgSentTime;
	}
}
