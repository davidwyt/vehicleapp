package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Assert;

import com.vehicle.imserver.dao.bean.MessageType;

@XmlRootElement
public class FileTransmissionRequest implements IRequest {
	private String source;
	private String target;

	private String fileName;

	private int msgType;

	@XmlElement
	public int getMsgType() {
		return this.msgType;
	}

	public void setMsgType(int type) {
		Assert.assertEquals(true,
				(MessageType.AUDIO.ordinal() == type || MessageType.IMAGE
						.ordinal() == type));

		this.msgType = type;
	}

	@XmlElement
	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@XmlElement
	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@XmlElement
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return String.format("% send file: % to %", source, fileName, target);
	}
}
