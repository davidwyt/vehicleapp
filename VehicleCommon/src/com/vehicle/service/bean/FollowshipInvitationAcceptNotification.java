package com.vehicle.service.bean;

import java.util.HashMap;
import java.util.Map;

public class FollowshipInvitationAcceptNotification implements INotification {

	private String source;
	private String target;
	private String invitationId;

	public String getInvitationId() {
		return this.invitationId;
	}

	public void setInvitationId(String invitationId) {
		this.invitationId = invitationId;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return Notifications.FollowshipInvitationAccepted.toString();
	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return String.format("%s accepted your followship invitation",
				this.source);
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

	@Override
	public Map<String, Object> getExtras() {
		// TODO Auto-generated method stub
		Map<String, Object> extras = new HashMap<String, Object>();
		extras.put("InvitationId", this.invitationId);

		return extras;
	}

}
