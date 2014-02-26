package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Assert;

import com.vehicle.imserver.dao.bean.MessageType;

@XmlRootElement
public class FileMultiTransmissionRequest implements IRequest {

	private String source;
	private String targets;

	private String fileName;
	private int msgType;

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
	public String getTargets() {
		return this.targets;
	}

	public void setTargets(String targets) {
		this.targets = targets;
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
		return String.format("% send file: % to %", source, fileName, targets);
	}
}
