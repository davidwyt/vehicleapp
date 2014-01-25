package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FollowshipInvitationResultRequest implements IRequest {

	private String invitationId;
	private boolean isAccepted;

	@XmlElement
	public String getInvitationId() {
		return this.invitationId;
	}

	public void setInvitationId(String invitationId) {
		this.invitationId = invitationId;
	}

	@XmlElement
	public boolean getIsAccepted() {
		return this.isAccepted;
	}

	public void setIsAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	@Override
	public String toString() {
		return String.format("the result of invitationid:%s is %s",
				this.invitationId, this.isAccepted);
	}
}
