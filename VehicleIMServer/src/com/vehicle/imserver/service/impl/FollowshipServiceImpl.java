package com.vehicle.imserver.service.impl;

import java.util.Date;
import java.util.List;

import com.vehicle.imserver.dao.bean.FollowshipInvitation;
import com.vehicle.imserver.dao.interfaces.FollowshipDao;
import com.vehicle.imserver.dao.interfaces.FollowshipInvitationDao;
import com.vehicle.imserver.service.exception.FollowshipAlreadyExistException;
import com.vehicle.imserver.service.exception.FollowshipInvitationNotExistException;
import com.vehicle.imserver.service.exception.FollowshipInvitationProcessedAlreadyException;
import com.vehicle.imserver.service.exception.FollowshipNotExistException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.exception.PushNotificationFailedException;
import com.vehicle.imserver.service.interfaces.FollowshipService;
import com.vehicle.imserver.utils.GUIDUtil;
import com.vehicle.imserver.utils.JPushUtil;
import com.vehicle.imserver.utils.JsonUtil;
import com.vehicle.imserver.utils.RequestDaoUtil;
import com.vehicle.service.bean.FolloweesRequest;
import com.vehicle.service.bean.FollowersRequest;
import com.vehicle.service.bean.FollowshipAddedNotification;
import com.vehicle.service.bean.FollowshipAddedRequest;
import com.vehicle.service.bean.FollowshipDroppedNotification;
import com.vehicle.service.bean.FollowshipDroppedRequest;
import com.vehicle.service.bean.FollowshipInvitationAcceptNotification;
import com.vehicle.service.bean.FollowshipInvitationNotification;
import com.vehicle.service.bean.FollowshipInvitationRejectNotification;
import com.vehicle.service.bean.FollowshipInvitationRequest;
import com.vehicle.service.bean.FollowshipInvitationResultRequest;
import com.vehicle.service.bean.FollowshipRequest;
import com.vehicle.service.bean.INotification;

public class FollowshipServiceImpl implements FollowshipService {

	FollowshipDao followshipDao;

	FollowshipInvitationDao followshipInvitationDao;

	public FollowshipDao getFollowshipDao() {
		return this.followshipDao;
	}

	public void setFollowshipDao(FollowshipDao followshipDao) {
		this.followshipDao = followshipDao;
	}

	public FollowshipInvitationDao getFollowshipInvitationDao() {
		return this.followshipInvitationDao;
	}

	public void setFollowshipInvitationDao(FollowshipInvitationDao invitationDao) {
		this.followshipInvitationDao = invitationDao;
	}

	public void AddFollowship(FollowshipRequest followshipReq)
			throws FollowshipAlreadyExistException, PersistenceException {
		try {
			followshipDao.AddFollowship(RequestDaoUtil
					.toRawFollowship(followshipReq));
		} catch (FollowshipAlreadyExistException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	public void DropFollowship(FollowshipRequest followshipReq)
			throws FollowshipNotExistException, PersistenceException {
		try {
			followshipDao.DropFollowship(RequestDaoUtil
					.toRawFollowship(followshipReq));
		} catch (FollowshipNotExistException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	public List<String> GetFollowers(FollowersRequest followersReq)
			throws PersistenceException {
		try {
			return followshipDao.GetFollowers(followersReq.getFollowee());
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	public List<String> GetFollowees(FolloweesRequest followeesReq)
			throws PersistenceException, FollowshipAlreadyExistException {
		return followshipDao.GetFollowees(followeesReq.getFollower());
	}

	@Override
	public void FollowshipAdded(FollowshipAddedRequest followshipAddedRequest)
			throws PushNotificationFailedException {

		FollowshipAddedNotification notification = new FollowshipAddedNotification();
		notification.setSource(followshipAddedRequest.getMemberId());
		notification.setTarget(followshipAddedRequest.getShopId());

		JPushUtil.getInstance().SendNotification(notification.getTarget(),
				notification.getTitle(), JsonUtil.toJsonString(notification));
	}

	@Override
	public void followshipDropped(
			FollowshipDroppedRequest followshipDroppedRequest)
			throws PushNotificationFailedException {

		FollowshipDroppedNotification notification = new FollowshipDroppedNotification();
		notification.setSource(followshipDroppedRequest.getMemberId());
		notification.setTarget(followshipDroppedRequest.getShopId());

		JPushUtil.getInstance().SendNotification(notification.getTarget(),
				notification.getTitle(), JsonUtil.toJsonString(notification));
	}

	@Override
	public FollowshipInvitation InviteFollowship(FollowshipInvitationRequest invitationRequest)
			throws PushNotificationFailedException, PersistenceException {

		// should verify if the member has followed the shop first
		// //

		String invitationId = GUIDUtil.genNewGuid();
		FollowshipInvitation invitation = new FollowshipInvitation();
		invitation.setID(invitationId);
		invitation.setReqTime(new Date().getTime());
		invitation.setSource(invitationRequest.getShopId());
		invitation.setTarget(invitationRequest.getMemberId());
		invitation.setStatus(FollowshipInvitation.STATUS_REQUESTED);

		try {
			this.followshipInvitationDao.AddFollowshipInvitation(invitation);
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}

		FollowshipInvitationNotification notification = new FollowshipInvitationNotification();
		notification.setSource(invitationRequest.getMemberId());
		notification.setTarget(invitationRequest.getShopId());
		notification.setInvitationId(invitationId);
		notification.setInviteTime(invitation.getReqTime());

		JPushUtil.getInstance().SendNotification(notification.getTarget(),
				notification.getTitle(), JsonUtil.toJsonString(notification));
		
		return invitation;
	}

	@Override
	public void InvitedFollowshipResult(
			FollowshipInvitationResultRequest invitationResult)
			throws PushNotificationFailedException,
			FollowshipInvitationNotExistException,
			FollowshipInvitationProcessedAlreadyException, PersistenceException {

		String invitationId = invitationResult.getInvitationId();
		FollowshipInvitation invitation = this.followshipInvitationDao
				.GetFollowshipInvitation(invitationId);

		if (null == invitation) {
			throw new FollowshipInvitationNotExistException(String.format(
					"the invitation:%s not exist", invitationId));
		}

		if (FollowshipInvitation.STATUS_ACCEPTED == invitation.getStatus()
				|| FollowshipInvitation.STATUS_REJECTED == invitation
						.getStatus()) {
			throw new FollowshipInvitationProcessedAlreadyException(
					String.format("the invitation:%s has been processed",
							invitationId));
		}

		INotification notification = null;
		if (invitationResult.getIsAccepted()) {
			notification = new FollowshipInvitationAcceptNotification();
			((FollowshipInvitationAcceptNotification) notification)
					.setSource(invitation.getTarget());
			((FollowshipInvitationAcceptNotification) notification)
					.setTarget(invitation.getSource());
			invitation.setStatus(FollowshipInvitation.STATUS_ACCEPTED);

		} else {
			notification = new FollowshipInvitationRejectNotification();
			((FollowshipInvitationRejectNotification) notification)
					.setSource(invitation.getTarget());
			((FollowshipInvitationRejectNotification) notification)
					.setTarget(invitation.getSource());
			invitation.setStatus(FollowshipInvitation.STATUS_REJECTED);
		}

		try {
			this.followshipInvitationDao.UpdateFollowshipInvitation(invitation);
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}

		JPushUtil.getInstance().SendNotification(notification.getTarget(),
				notification.getTitle(), JsonUtil.toJsonString(notification));
	}
}
