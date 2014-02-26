package com.vehicle.app.msg.bean;

import java.io.Serializable;

import com.vehicle.service.bean.FollowshipInvitationAcceptNotification;
import com.vehicle.service.bean.FollowshipInvitationRejectNotification;

public class InvitationVerdictMessage implements IMessageItem, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4535451111510858983L;

	private String invitationId;

	private String source;
	private String target;
	private InvitationVerdict verdict;
	private MessageFlag flag;

	public String getInvitationId() {
		return this.invitationId;
	}

	public void setInvitationId(String id) {
		this.invitationId = id;
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

	public InvitationVerdict getVerdict() {
		return this.verdict;
	}

	public void setVerdict(InvitationVerdict verdict) {
		this.verdict = verdict;
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
		return 0;
	}

	@Override
	public void fromRawNotification(Object notification) {
		// TODO Auto-generated method stub

		if (!(notification instanceof FollowshipInvitationAcceptNotification)
				|| !(notification instanceof FollowshipInvitationRejectNotification)) {
			throw new IllegalArgumentException(
					"notificatoin not FollowshipInvitationAcceptNotification or FollowshipInvitationRejectNotification");
		}

		if (notification instanceof FollowshipInvitationAcceptNotification) {
			FollowshipInvitationAcceptNotification rawMsg = (FollowshipInvitationAcceptNotification) notification;
			this.flag = MessageFlag.UNREAD;
			this.invitationId = rawMsg.getInvitationId();
			this.source = rawMsg.getSource();
			this.target = rawMsg.getTarget();
			this.verdict = InvitationVerdict.ACCEPTED;
		} else if (notification instanceof FollowshipInvitationRejectNotification) {

			FollowshipInvitationRejectNotification rawMsg = (FollowshipInvitationRejectNotification) notification;
			this.flag = MessageFlag.UNREAD;
			this.invitationId = rawMsg.getInvitationId();
			this.source = rawMsg.getSource();
			this.target = rawMsg.getTarget();
			this.verdict = InvitationVerdict.ACCEPTED;
		}

	}

	@Override
	public int getMessageType() {
		// TODO Auto-generated method stub
		return IMessageItem.MESSAGE_TYPE_INVITATIONVERDICT;
	}
}