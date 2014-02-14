package com.vehicle.imserver.dao.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Index;

@Entity
@Table(name = "MESSAGE", indexes = { @Index(columnList = "SOURCE"),
		@Index(columnList = "TARGET") })
public class Message {

	private String id;
	private String source;
	private String target;
	private long sentTime;
	private String content;
	private int messageType;

	public Message() {
	}

	public Message(OfflineMessage om) {
		this.id = om.getId();
		this.source = om.getSource();
		this.target = om.getTarget();
		this.sentTime = om.getSentTime();
		this.content = om.getContent();
	}

	@Id
	@Column(name = "ID")
	public String getId() {
		return this.id;
	}

	public void setId(String iid) {
		this.id = iid;
	}

	@Column(name = "SOURCE")
	public String getSource() {
		return this.source;
	}

	public void setSource(String s) {
		this.source = s;
	}

	@Column(name = "TARGET")
	public String getTarget() {
		return this.target;
	}

	public void setTarget(String tar) {
		this.target = tar;
	}

	@Column(name = "SENTTIME")
	public long getSentTime() {
		return this.sentTime;
	}

	public void setSentTime(long time) {
		this.sentTime = time;
	}

	@Column(name = "CONTENT")
	public String getContent() {
		return this.content;
	}

	public void setContent(String c) {
		this.content = c;
	}

	@Column(name = "TYPE")
	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
}
