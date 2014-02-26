package com.vehicle.imserver.dao.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Index;

import org.junit.Assert;

@Entity
@Table(name = "FILETRANSMISSION", indexes = { @Index(columnList = "SOURCE"),
		@Index(columnList = "TARGET"), @Index(columnList = "STATUS") })
public class FileTransmission {

	public static final int STATUS_SENT = 0;
	public static final int STATUS_RECEIVED = 1;
	public static final int STATUS_DONE = 2;

	private String token;
	private String path;
	private String source;
	private String target;
	private int status;
	private long transmissionTime;
	private int msgType;

	@Column(name = "MSGTYPE")
	public int getMsgType() {
		return this.msgType;
	}

	public void setMsgType(int msgType) {
		Assert.assertEquals(true,
				(MessageType.AUDIO.ordinal() == msgType || MessageType.IMAGE
						.ordinal() == msgType));
		this.msgType = msgType;
	}

	@Id
	@Column(name = "TOKEN")
	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Column(name = "PATH")
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(name = "SOURCE")
	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "TARGET")
	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Column(name = "STATUS")
	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name = "TRANSMISSIONTIME")
	public long getTransmissionTime() {
		return this.transmissionTime;
	}

	public void setTransmissionTime(long time) {
		this.transmissionTime = time;
	}
}
