package com.vehicle.service.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.vehicle.imserver.dao.bean.FollowshipInvitation;

@XmlRootElement
public class WakeupResponse extends BaseResponse {

	private List<FollowshipInvitation> newInvitations;

	@XmlElement
	public List<FollowshipInvitation> getNewInvitations() {
		return this.newInvitations;
	}

	public void setNewInvitations(List<FollowshipInvitation> invitations) {
		this.newInvitations = invitations;
	}
}
