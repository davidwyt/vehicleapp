package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FollowshipInvitationACKRequest implements IRequest {

	private String invId;

	@XmlElement
	public String getInvId() {
		return this.invId;
	}

	public void setInvId(String id) {
		this.invId = id;
	}

	@Override
	public String toString() {
		return String.format("inv:%s received", invId);
	}
}
