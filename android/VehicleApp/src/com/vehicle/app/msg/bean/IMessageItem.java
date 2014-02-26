package com.vehicle.app.msg.bean;

public interface IMessageItem {

	String getSource();

	String getTarget();

	MessageFlag getFlag();

	long getSentTime();

	int getMessageType();

	public final static int MESSAGE_TYPE_TEXT = 0;
	public final static int MESSAGE_TYPE_IMAGE = 1;
	public final static int MESSAGE_TYPE_LOCATION = 2;
	public final static int MESSAGE_TYPE_AUDIO = 3;
	public final static int MESSAGE_TYPE_FELLOWSHIPINVITATION = 4;
	public final static int MESSAGE_TYPE_INVITATIONVERDICT = 5;
	public final static int MESSAGE_TYPE_FOLLOW = 6;

	void fromRawNotification(Object notification);
}
