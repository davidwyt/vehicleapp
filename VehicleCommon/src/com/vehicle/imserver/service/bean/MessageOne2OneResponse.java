package com.vehicle.imserver.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageOne2OneResponse extends BaseResponse{
	
	private String msgId;
	
	public void setMsgId(String id)
	{
		msgId = id;
	}
	
	@XmlElement
	public String getMsgId()
	{
		return this.msgId;
	}
}
