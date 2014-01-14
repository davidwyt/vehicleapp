package com.vehicle.imserver.service.bean;

import java.util.Date;

import com.vehicle.imserver.persistence.dao.Message;
import com.vehicle.imserver.persistence.dao.MessageStatus;
import com.vehicle.imserver.utils.GUIDUtil;

public class One2OneMessageRequest implements IRequest{
	private String source;
	private String target;
	private String content;

	public void setSource(String source) {
		this.source = source;
	}

	public void setTarget(String target) {
		this.target = target;
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

	public String getContent() {
		return this.content;
	}

	@Override
	public String toString() {
		return String.format("Message#### %s, source:%s, target:%s", content,
				source, target);
	}

	public Message toRawMessage() {
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
