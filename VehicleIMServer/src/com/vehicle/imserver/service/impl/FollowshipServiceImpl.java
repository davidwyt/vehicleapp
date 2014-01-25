package com.vehicle.imserver.service.impl;

import java.util.List;

import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.MessageResult;

import com.vehicle.imserver.dao.interfaces.FollowshipDao;
import com.vehicle.imserver.service.exception.FollowshipAlreadyExistException;
import com.vehicle.imserver.service.exception.FollowshipNotExistException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.exception.PushNotificationFailedException;
import com.vehicle.imserver.service.interfaces.FollowshipService;
import com.vehicle.imserver.utils.JPushUtil;
import com.vehicle.imserver.utils.RequestDaoUtil;
import com.vehicle.service.bean.FolloweesRequest;
import com.vehicle.service.bean.FollowersRequest;
import com.vehicle.service.bean.FollowshipAddedNotification;
import com.vehicle.service.bean.FollowshipAddedRequest;
import com.vehicle.service.bean.FollowshipDroppedNotification;
import com.vehicle.service.bean.FollowshipDroppedRequest;
import com.vehicle.service.bean.FollowshipRequest;

public class FollowshipServiceImpl implements FollowshipService {

	FollowshipDao followshipDao;

	public FollowshipDao getFollowshipDao() {
		return this.followshipDao;
	}

	public void setFollowshipDao(FollowshipDao followshipDao) {
		this.followshipDao = followshipDao;
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

		MessageResult msgResult = null;
		try {
			msgResult = JPushUtil.getInstance().SendNotification(
					notification.getTarget(), notification.getTitle(),
					notification.getContent());
		} catch (Exception e) {
			throw new PushNotificationFailedException(e);
		}

		if (null != msgResult) {
			if (ErrorCodeEnum.NOERROR.value() == msgResult.getErrcode()) {

			} else {
				throw new PushNotificationFailedException(msgResult.getErrmsg());
			}
		} else {
			throw new PushNotificationFailedException("no push result");
		}
	}

	@Override
	public void followshipDropped(
			FollowshipDroppedRequest followshipDroppedRequest)
			throws PushNotificationFailedException {

		FollowshipDroppedNotification notification = new FollowshipDroppedNotification();
		notification.setSource(followshipDroppedRequest.getMemberId());
		notification.setTarget(followshipDroppedRequest.getShopId());

		MessageResult msgResult = null;
		try {
			msgResult = JPushUtil.getInstance().SendNotification(
					notification.getTarget(), notification.getTitle(),
					notification.getContent());
		} catch (Exception e) {
			throw new PushNotificationFailedException(e);
		}

		if (null != msgResult) {
			if (ErrorCodeEnum.NOERROR.value() == msgResult.getErrcode()) {

			} else {
				throw new PushNotificationFailedException(msgResult.getErrmsg());
			}
		} else {
			throw new PushNotificationFailedException("no push result");
		}
	}
}
