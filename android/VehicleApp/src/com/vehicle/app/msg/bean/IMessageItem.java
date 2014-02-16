package com.vehicle.app.msg.bean;


public interface IMessageItem {

	String getSource();

	String getTarget();

	MessageFlag getFlag();

	long getSentTime();

	public final static int MESSAGE_TYPE_TEXT = 0;
	public final static int MESSAGE_TYPE_IMAGE = 1;
	public final static int MESSAGE_TYPE_LOCATION = 2;
	
	void fromRawNotification(Object notification);
}
