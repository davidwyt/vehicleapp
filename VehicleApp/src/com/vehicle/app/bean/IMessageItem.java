package com.vehicle.app.bean;

public interface IMessageItem {
	
	String getSource();
	String getTarget();
	MessageFlag getFlag();
	long getSentTime();
}
