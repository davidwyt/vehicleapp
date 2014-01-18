package com.vehicle.imserver.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.MessageResult;

import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.dao.bean.MessageStatus;
import com.vehicle.imserver.dao.interfaces.FollowshipDao;
import com.vehicle.imserver.dao.interfaces.MessageDao;
import com.vehicle.imserver.service.bean.MessageACKRequest;
import com.vehicle.imserver.service.bean.MessageOne2FolloweesRequest;
import com.vehicle.imserver.service.bean.MessageOne2FollowersRequest;
import com.vehicle.imserver.service.bean.MessageOne2OneRequest;
import com.vehicle.imserver.service.exception.MessageNotFoundException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.exception.PushMessageFailedException;
import com.vehicle.imserver.service.interfaces.MessageService;
import com.vehicle.imserver.utils.JPushUtil;
import com.vehicle.imserver.utils.JsonUtil;
import com.vehicle.imserver.utils.RequestDaoUtil;

public class MessageServiceImpl implements MessageService {

	MessageDao messageDao;

	public MessageDao getMessageDao() {
		return this.messageDao;
	}

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	FollowshipDao followshipDao;

	public FollowshipDao getFollowshipDao() {
		return this.followshipDao;
	}

	public void setFollowshipDao(FollowshipDao followshipDao) {
		this.followshipDao = followshipDao;
	}

	private void PersistentAndSendMessage(Message msg)
			throws PersistenceException, PushMessageFailedException {
		try {
			messageDao.InsertMessage(msg);
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}

		MessageResult msgResult = null;
		try {
			msgResult = JPushUtil.getInstance().SendCustomMessage(
					msg.getTarget(), "", JsonUtil.toJsonString(msg));

		} catch (Exception e) {
			throw new PushMessageFailedException(e);
		}

		if (null != msgResult) {
			if (ErrorCodeEnum.NOERROR.value() == msgResult.getErrcode()) {

			} else {
				throw new PushMessageFailedException(msgResult.getErrmsg());
			}
		} else {
			throw new PushMessageFailedException("no push result");
		}
	}

	public String SendMessage(MessageOne2OneRequest msgReq)
			throws PersistenceException, PushMessageFailedException {

		System.out.println(msgReq.toString());

		Message msg = RequestDaoUtil.toRawMessage(msgReq);

		PersistentAndSendMessage(msg);

		return msg.getId();
	}

	public void MessageReceived(MessageACKRequest msgACKReq)
			throws MessageNotFoundException, PersistenceException {

		System.out.println(msgACKReq.toString());

		try {
			messageDao.UpdateMessageStatus(msgACKReq.getMsgId(),
					MessageStatus.RECEIVED);

		} catch (MessageNotFoundException msgNotFoundException) {
			throw msgNotFoundException;
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	private void PersistentAndSendMessageList(String source,
			List<String> targets, String content) throws PersistenceException,
			PushMessageFailedException {

		Date now = new Date();
		for (String target : targets) {
			Message msg = new Message();
			msg.setSource(source);
			msg.setTarget(target);
			msg.setContent(content);
			msg.setSentDate(now);
			msg.setStatus(MessageStatus.SENT);
			msg.setId(UUID.randomUUID().toString());

			PersistentAndSendMessage(msg);
		}
	}

	public void SendMessage2Followees(MessageOne2FolloweesRequest msgRequest)
			throws PersistenceException, PushMessageFailedException {
		System.out.println(msgRequest.toString());

		String source = msgRequest.getSource();
		List<String> followees = followshipDao.GetFollowees(source);

		PersistentAndSendMessageList(source, followees, msgRequest.getContent());
	}

	public void SendMessage2Followers(MessageOne2FollowersRequest msgRequest)
			throws PersistenceException, PushMessageFailedException {
		System.out.println(msgRequest.toString());

		String source = msgRequest.getSource();
		List<String> followers = followshipDao.GetFollowers(source);

		PersistentAndSendMessageList(source, followers, msgRequest.getContent());
	}
}
