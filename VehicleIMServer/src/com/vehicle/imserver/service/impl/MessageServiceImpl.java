package com.vehicle.imserver.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.dao.bean.OfflineMessage;
import com.vehicle.imserver.dao.interfaces.FollowshipDao;
import com.vehicle.imserver.dao.interfaces.MessageDao;
import com.vehicle.imserver.dao.interfaces.OfflineMessageDao;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.exception.PushMessageFailedException;
import com.vehicle.imserver.service.interfaces.MessageService;
import com.vehicle.imserver.utils.CallableQueue;
import com.vehicle.imserver.utils.JPushUtil;
import com.vehicle.imserver.utils.JsonUtil;
import com.vehicle.imserver.utils.RequestDaoUtil;
import com.vehicle.imserver.utils.StringUtil;
import com.vehicle.service.bean.MessageACKRequest;
import com.vehicle.service.bean.MessageOne2FolloweesRequest;
import com.vehicle.service.bean.MessageOne2FollowersRequest;
import com.vehicle.service.bean.MessageOne2MultiRequest;
import com.vehicle.service.bean.MessageOne2OneRequest;
import com.vehicle.service.bean.Notifications;
import com.vehicle.service.bean.OfflineAckRequest;
import com.vehicle.service.bean.OfflineMessageRequest;
import com.vehicle.service.bean.RespMessage;

public class MessageServiceImpl implements MessageService {

	MessageDao messageDao;
	OfflineMessageDao offlineMessageDao;

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

	private void PersistentMessage(Message msg) throws PersistenceException {
		try {
			messageDao.InsertMessage(msg);
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	private void PersistentAndSendMessage(Message msg)
			throws PersistenceException {

		try {
			JPushUtil.getInstance().SendAndroidMessage(msg.getTarget(),
					Notifications.NewMessage.toString(),
					JsonUtil.toJsonString(msg));
			JPushUtil.getInstance().SendIOSMessage(msg.getTarget(), "chat",
					JsonUtil.toJsonString(msg));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			messageDao.InsertMessage(msg);
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	public Message SendMessage(MessageOne2OneRequest msgReq)
			throws PersistenceException, PushMessageFailedException {

		System.out.println(msgReq.toString());

		Message msg = RequestDaoUtil.toRawMessage(msgReq);

		PersistentAndSendMessage(msg);

		return msg;
	}

	private void PersistentMessageList(String source, List<String> targets,
			String content, int msgType) throws PersistenceException {

		Date now = new Date();
		for (String target : targets) {
			Message msg = new Message();
			msg.setSource(source);
			msg.setTarget(target);
			msg.setContent(content);
			msg.setSentTime(now.getTime());
			msg.setMessageType(msgType);
			msg.setId(UUID.randomUUID().toString());
			try {
				PersistentMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void SendMessage2Followees(MessageOne2FolloweesRequest msgRequest)
			throws PersistenceException, PushMessageFailedException {
		System.out.println(msgRequest.toString());

		String source = msgRequest.getSource();
		List<String> followees = followshipDao.GetFollowees(source);

		// PersistentAndSendMessageList(source, followees,
		// msgRequest.getContent());
	}

	public void SendMessage2Followers(MessageOne2FollowersRequest msgRequest)
			throws PersistenceException, PushMessageFailedException {
		System.out.println(msgRequest.toString());

		String source = msgRequest.getSource();
		List<String> followers = followshipDao.GetFollowers(source);

		// PersistentAndSendMessageList(source, followers,
		// msgRequest.getContent());
	}

	public OfflineMessageDao getOfflineMessageDao() {
		return offlineMessageDao;
	}

	public void setOfflineMessageDao(OfflineMessageDao offlineMessageDao) {
		this.offlineMessageDao = offlineMessageDao;
	}

	@Override
	public List<RespMessage> sendOfflineMsgs(OfflineMessageRequest omReq)
			throws PersistenceException {
		// TODO Auto-generated method stub
		List<OfflineMessage> list = this.getOfflineMessageDao().getOffline(
				omReq.getTarget(), new Date(omReq.getSince()));
		List<RespMessage> ret = new ArrayList<RespMessage>();
		for (int i = 0; i < list.size(); i++) {
			RespMessage r = new RespMessage();
			r.setContent(list.get(i).getContent());
			r.setId(list.get(i).getId());
			r.setSentTime(list.get(i).getSentTime());
			r.setSource(list.get(i).getSource());
			r.setTarget(list.get(i).getTarget());
			r.setMessageType(list.get(i).getMessageType());
			ret.add(r);
		}

		return ret;
	}

	@Override
	public void offlineAck(OfflineAckRequest req) throws PersistenceException {
		// TODO Auto-generated method stub
		String target = req.getId();
		this.getOfflineMessageDao().deleteOffline(target);
	}

	@Override
	public void sendMessage2Multi(MessageOne2MultiRequest req)
			throws PersistenceException, PushMessageFailedException {
		// TODO Auto-generated method stub
		System.out.println(req.toString());

		String source = req.getSource();
		List<String> multi = new ArrayList<String>();
		String[] temps = req.getTargets().split(",");
		if (null != temps && temps.length > 0) {
			for (int i = 0; i < temps.length; i++) {
				if (!StringUtil.isEmptyOrNull(temps[i]))
					multi.add(temps[i]);
			}

			try {
				PersistentMessageList(source, multi, req.getContent(),
						req.getMessageType());
			} catch (Exception e) {
				e.printStackTrace();
			}

			MultiTextCourier courier = new MultiTextCourier(source, multi,
					req.getContent(), req.getMessageType());
			CallableQueue.getInstance().Queue(courier);
		}
	}

	@Override
	public void ackOneMessage(MessageACKRequest request)
			throws PersistenceException {

		try {
			Message msg = this.messageDao.get(request.getMsgId());
			if (null != msg && msg.getStatus() == Message.STATUS_SENT) {
				msg.setStatus(Message.STATUS_RECEIVED);
				this.messageDao.update(msg);
			}
		} catch (Exception e) {
			throw new PersistenceException(
					"persistence error when ack message", e);
		}
	}

	private void SendMessage(Message msg) {

		try {
			JPushUtil.getInstance().SendAndroidMessage(msg.getTarget(),
					Notifications.NewMessage.toString(),
					JsonUtil.toJsonString(msg));
			JPushUtil.getInstance().SendIOSMessage(msg.getTarget(), "chat",
					JsonUtil.toJsonString(msg));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	private class MultiTextCourier implements Callable {
		String source;
		List<String> targets;
		int msgType;
		String content;

		MultiTextCourier(String source, List<String> targets, String content,
				int msgType) {
			this.source = source;
			this.targets = targets;
			this.content = content;
			this.msgType = msgType;
		}

		private void SendMessageList(String source, List<String> targets,
				String content, int msgType) {

			Date now = new Date();
			for (String target : targets) {
				Message msg = new Message();
				msg.setSource(source);
				msg.setTarget(target);
				msg.setContent(content);
				msg.setSentTime(now.getTime());
				msg.setMessageType(msgType);
				msg.setId(UUID.randomUUID().toString());
				try {
					SendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public Object call() throws Exception {
			// TODO Auto-generated method stub
			try {
				this.SendMessageList(source, targets, content, msgType);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
