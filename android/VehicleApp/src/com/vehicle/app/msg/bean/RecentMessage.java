package com.vehicle.app.msg.bean;

import java.io.Serializable;

public class RecentMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5624629610889312856L;
	
	private String selfId;
	private String fellowId;
	private String messageId;
	private int messageType;
	private String content;
	private long sentTime;

	public String getSelfId() {
		return this.selfId;
	}

	public void setSelfId(String id) {
		this.selfId = id;
	}

	public String getFellowId() {
		return this.fellowId;
	}

	public void setFellowId(String id) {
		this.fellowId = id;
	}

	public String getMessageId() {
		return this.messageId;
	}

	public void setMessageId(String id) {
		this.messageId = id;
	}

	public int getMessageType() {
		return this.messageType;
	}

	public void setMessageType(int type) {
		this.messageType = type;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getSentTime() {
		return this.sentTime;
	}

	public void setSentTime(long time) {
		this.sentTime = time;
	}
}
