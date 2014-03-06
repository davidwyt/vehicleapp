package com.vehicle.service.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.vehicle.imserver.dao.bean.FollowshipInvitation;
import com.vehicle.imserver.dao.bean.Message;

@XmlRootElement
public class WakeupResponse extends BaseResponse {

	private List<FollowshipInvitation> newInvitations;
	private List<Message> newMessages;
	private List<NewFileNotification> newFiles;

	@XmlElement
	public List<FollowshipInvitation> getNewInvitations() {
		return this.newInvitations;
	}

	public void setNewInvitations(List<FollowshipInvitation> invitations) {
		this.newInvitations = invitations;
	}

	@XmlElement
	public List<Message> getNewMessages() {
		return this.newMessages;
	}

	public void setNewMessages(List<Message> msgs) {
		this.newMessages = msgs;
	}

	@XmlElement
	public List<NewFileNotification> getNewFiles() {
		return this.newFiles;
	}

	public void setNewFiles(List<NewFileNotification> files) {
		this.newFiles = files;
	}

}
