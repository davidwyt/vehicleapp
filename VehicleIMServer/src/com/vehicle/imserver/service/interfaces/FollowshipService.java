package com.vehicle.imserver.service.interfaces;

import java.util.List;

import com.vehicle.imserver.dao.bean.FollowshipInvitation;
import com.vehicle.imserver.service.exception.FollowshipAlreadyExistException;
import com.vehicle.imserver.service.exception.FollowshipInvitationNotExistException;
import com.vehicle.imserver.service.exception.FollowshipInvitationProcessedAlreadyException;
import com.vehicle.imserver.service.exception.FollowshipNotExistException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.exception.PushNotificationFailedException;
import com.vehicle.service.bean.FolloweesRequest;
import com.vehicle.service.bean.FollowersRequest;
import com.vehicle.service.bean.FollowshipAddedRequest;
import com.vehicle.service.bean.FollowshipDroppedRequest;
import com.vehicle.service.bean.FollowshipInvitationRequest;
import com.vehicle.service.bean.FollowshipInvitationResultRequest;
import com.vehicle.service.bean.FollowshipRequest;

public interface FollowshipService {

	public void AddFollowship(FollowshipRequest followshipReq)
			throws FollowshipAlreadyExistException, PersistenceException;

	public void DropFollowship(FollowshipRequest followshipReq)
			throws FollowshipNotExistException, PersistenceException;

	public List<String> GetFollowers(FollowersRequest followersReq)
			throws PersistenceException;

	public List<String> GetFollowees(FolloweesRequest followeesReq)
			throws PersistenceException, FollowshipAlreadyExistException;

	public void FollowshipAdded(FollowshipAddedRequest followshipAddedRequest)
			throws PushNotificationFailedException;

	public void followshipDropped(
			FollowshipDroppedRequest followshipDroppedRequest)
			throws PushNotificationFailedException;

	public FollowshipInvitation InviteFollowship(FollowshipInvitationRequest invitationRequest)
			throws PushNotificationFailedException, PersistenceException;

	public void InvitedFollowshipResult(
			FollowshipInvitationResultRequest invitationResult)
			throws PushNotificationFailedException,
			FollowshipInvitationNotExistException,
			FollowshipInvitationProcessedAlreadyException, PersistenceException;
}
