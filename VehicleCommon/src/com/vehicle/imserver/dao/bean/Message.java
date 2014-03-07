package com.vehicle.imserver.dao.bean;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Index;

@Entity
@Table(name = "MESSAGE", indexes = { @Index(columnList = "SOURCE"),
		@Index(columnList = "TARGET"), @Index(columnList = "STATUS") })
public class Message {

	public static int STATUS_SENT = 0;
	public static int STATUS_RECEIVED = 1;

	private String id;
	private String source;
	private String target;
	private long sentTime;
	private String content;
	private int messageType;
	private int status;

	public Message() {
	}

	@Id
	@Column(name = "ID")
	public String getId() {
		return this.id;
	}

	public void setId(String iid) {
		this.id = iid;
	}

	@Column(name = "STATUS")
	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
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
	
	public Map<String,Object> toMap(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("source", source);
		map.put("target", target);
		map.put("messageType", messageType);
		map.put("content", content);
		map.put("sentTime", sentTime);
		return map;
	}
}
