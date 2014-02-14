package com.vehicle.service.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OfflineMessageResponse extends BaseResponse {
	
	private List<RespMessage> messages;

	@XmlElement
	public List<RespMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<RespMessage> messages) {
		this.messages = messages;
	}
}
