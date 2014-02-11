package com.vehicle.app.msg;

public interface IMessageItem {
	
	String getSource();
	String getTarget();
	MessageFlag getFlag();
	long getSentTime();
}
