package com.vehicle.imserver.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageACKResponse extends BaseResponse{
	private String msgId;
	
	@XmlElement	
	public String getMsgId()
	{
		return this.msgId;
	}
	public void setMsgId(String id)
	{
		msgId = id;
	}
}
