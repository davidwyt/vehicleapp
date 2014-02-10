package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageOne2OneRequest implements IRequest{
	private String source;
	private String target;
	private String content;
	private int messageType;

	public void setSource(String source) {
		this.source = source;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@XmlElement
	public String getSource() {
		return this.source;
	}

	@XmlElement
	public String getTarget() {
		return this.target;
	}

	@XmlElement
	public String getContent() {
		return this.content;
	}

	@Override
	public String toString() {
		return String.format("Message#### %s, source:%s, target:%s", content,
				source, target);
	}

	@XmlElement
	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
}
