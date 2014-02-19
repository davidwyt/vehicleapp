package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FollowshipInvitationResponse extends BaseResponse {

	private String invitationId;
	private long reqTime;

	@XmlElement
	public String getInvitationId() {
		return this.invitationId;
	}

	public void setInvitationId(String id) {
		this.invitationId = id;
	}

	@XmlElement
	public long getReqTime() {
		return this.reqTime;
	}

	public void setReqTime(long time) {
		this.reqTime = time;
	}
}
