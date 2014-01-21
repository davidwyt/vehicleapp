package com.vehicle.app.bean;

import java.util.Date;

import android.graphics.Bitmap;

public class User {

	private String id;
	private String alias;
	private Bitmap icon;
	private String lastMessage;
	private Date lastMessageTime;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Bitmap getIcon() {
		return this.icon;
	}

	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}

	public String getLastMessage() {
		return this.lastMessage;
	}

	public void setLastMessage(String lastMsg) {
		this.lastMessage = lastMsg;
	}

	public Date getLastMessageDate() {
		return this.lastMessageTime;
	}

	public void setLastMessageDate(Date lastMsgDate) {
		this.lastMessageTime = lastMsgDate;
	}
}
