package com.vehicle.service.bean;

public class FollowshipInvitationNotification implements INotification {

	private String source;
	private String target;

	private String invitationId;
	private long inviteTime;

	public String getInvitationId() {
		return this.invitationId;
	}

	public void setInvitationId(String invitationId) {
		this.invitationId = invitationId;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return Notifications.FollowshipInvitation.toString();
	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return String.format("%s invite you to follow him/her", this.source);
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

	public long getInviteTime() {
		return this.inviteTime;
	}

	public void setInviteTime(long time) {
		this.inviteTime = time;
	}

}
