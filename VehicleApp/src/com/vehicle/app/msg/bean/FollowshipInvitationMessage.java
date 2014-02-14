package com.vehicle.app.msg.bean;

import java.io.Serializable;

import com.vehicle.service.bean.FollowshipInvitationNotification;

public class FollowshipInvitationMessage implements IMessageItem, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7628128119286526427L;
	
	private String id;
	private String source;
	private String target;
	private long sentTime;
	private MessageFlag flag;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return this.source;
	}

	public void setSource(String src) {
		this.source = src;
	}

	@Override
	public String getTarget() {
		// TODO Auto-generated method stub
		return this.target;
	}

	public void setTarget(String tar) {
		this.target = tar;
	}

	@Override
	public MessageFlag getFlag() {
		// TODO Auto-generated method stub
		return this.flag;
	}

	public void setFlag(MessageFlag flag) {
		this.flag = flag;
	}

	@Override
	public long getSentTime() {
		// TODO Auto-generated method stub
		return this.sentTime;
	}

	public void setSentTime(long time) {
		this.sentTime = time;
	}

	@Override
	public void fromRawNotification(Object notification) {
		// TODO Auto-generated method stub
		if (!(notification instanceof FollowshipInvitationNotification))
			throw new IllegalArgumentException("notification is not FollowshipInvitationNotification");

		FollowshipInvitationNotification rawMsg = (FollowshipInvitationNotification) notification;

		this.id = rawMsg.getInvitationId();
		this.flag = MessageFlag.UNREAD;
		this.sentTime = rawMsg.getInviteTime();
		this.source = rawMsg.getSource();
		this.target = rawMsg.getTarget();
	}
}
