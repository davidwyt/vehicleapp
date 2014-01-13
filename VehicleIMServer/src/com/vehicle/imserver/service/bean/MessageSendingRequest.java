package com.vehicle.imserver.service.bean;

import java.util.Date;

import com.vehicle.imserver.persistence.dao.Message;
import com.vehicle.imserver.persistence.dao.MessageStatus;
import com.vehicle.imserver.utils.GUIDUtil;

public class MessageSendingRequest {
	private String source;
	private String target;
	private String content;

	private Date sendTime;

	public void setSource(String source) {
		this.source = source;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setSendTime(Date time) {
		this.sendTime = time;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSource() {
		return this.source;
	}

	public String getTarget() {
		return this.target;
	}

	public Date getSendTime() {
		return this.sendTime;
	}

	public String getContent() {
		return this.content;
	}

	@Override
	public String toString() {
		return String.format("Message#### %s, source:%s, target:%s, time:%s",
				content, source, target, sendTime.toGMTString());
	}
	
	public Message toRawMessage()
	{
		Message msg = new Message();
		msg.setId(GUIDUtil.genNewGuid());
		msg.setSource(this.source);
		msg.setTarget(this.target);
		msg.setContent(this.content);
		msg.setSentDate(new Date());
		msg.setStatus(MessageStatus.SENT);
		
		return msg;
	}
}
