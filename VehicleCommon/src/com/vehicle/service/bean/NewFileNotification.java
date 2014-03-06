package com.vehicle.service.bean;

import com.vehicle.imserver.dao.bean.MessageType;

public class NewFileNotification implements INotification {

	private String id;
	private String source;
	private String target;
	private String fileName;
	private long sentTime;

	private int msgType = MessageType.IMAGE.ordinal();

	public NewFileNotification(String source, String target, String token,
			String fileName, long sentTime, int msgType) {
		this.source = source;
		this.target = target;
		this.id = token;
		this.fileName = fileName;
		this.sentTime = sentTime;
		this.msgType = msgType;
	}

	public NewFileNotification() {

	}

	public int getMsgType() {
		return this.msgType;
	}

	public void setMsgType(int type) {
		this.msgType = type;
	}

	public long getSentTime() {
		return this.sentTime;
	}

	public void setSentTime(long time) {
		this.sentTime = time;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return Notifications.NewFile.toString();
	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return String.format("%s sent you a file", source);
	}

	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String getTarget() {
		// TODO Auto-generated method stub
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String token) {
		this.id = token;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String name) {
		this.fileName = name;
	}

}
