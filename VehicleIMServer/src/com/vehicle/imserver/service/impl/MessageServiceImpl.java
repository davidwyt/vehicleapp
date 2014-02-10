package com.vehicle.imserver.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.MessageResult;

import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.dao.bean.OfflineMessage;
import com.vehicle.imserver.dao.interfaces.FollowshipDao;
import com.vehicle.imserver.dao.interfaces.MessageDao;
import com.vehicle.imserver.dao.interfaces.OfflineMessageDao;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.exception.PushMessageFailedException;
import com.vehicle.imserver.service.interfaces.MessageService;
import com.vehicle.imserver.utils.JPushUtil;
import com.vehicle.imserver.utils.JsonUtil;
import com.vehicle.imserver.utils.RequestDaoUtil;
import com.vehicle.service.bean.MessageOne2FolloweesRequest;
import com.vehicle.service.bean.MessageOne2FollowersRequest;
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

	private void PersistentAndSendMessage(Message msg)
			throws PersistenceException, PushMessageFailedException {
		

		MessageResult androidMsgResult = null;
		MessageResult iosMsgResult=null;
		try {
			androidMsgResult = JPushUtil.getInstance().SendAndroidMessage(
					msg.getTarget(), Notifications.NewMessage.toString(), JsonUtil.toJsonString(msg));
			iosMsgResult=JPushUtil.getInstance().SendIOSMessage(msg.getTarget(), "chat", JsonUtil.toJsonString(msg));
		} catch (Exception e) {
			throw new PushMessageFailedException(e);
		}

		if (null != androidMsgResult&&null!=iosMsgResult) {
			if (ErrorCodeEnum.NOERROR.value() == androidMsgResult.getErrcode()||ErrorCodeEnum.NOERROR.value() == iosMsgResult.getErrcode()) {
				try {
					messageDao.InsertMessage(msg);
				} catch (Exception e) {
					throw new PersistenceException(e.getMessage(), e);
				}
			} else {
				offlineMessageDao.save(new OfflineMessage(msg));
				if (ErrorCodeEnum.NOERROR.value() != androidMsgResult.getErrcode())
					throw new PushMessageFailedException(androidMsgResult.getErrmsg());
				if (ErrorCodeEnum.NOERROR.value() != iosMsgResult.getErrcode())
					throw new PushMessageFailedException(iosMsgResult.getErrmsg());
			}
		} else {
			throw new PushMessageFailedException("no push result");
		}
	}

	public Message SendMessage(MessageOne2OneRequest msgReq)
			throws PersistenceException, PushMessageFailedException {

		System.out.println(msgReq.toString());

		Message msg = RequestDaoUtil.toRawMessage(msgReq);

		PersistentAndSendMessage(msg);

		return msg;
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
			msg.setSentTime(now.getTime());
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
	
	public OfflineMessageDao getOfflineMessageDao() {
		return offlineMessageDao;
	}

	public void setOfflineMessageDao(OfflineMessageDao offlineMessageDao) {
		this.offlineMessageDao = offlineMessageDao;
	}

	@Override
	public List<RespMessage> sendOfflineMsgs(OfflineMessageRequest omReq)throws PersistenceException{
		// TODO Auto-generated method stub
		List<OfflineMessage> list=this.getOfflineMessageDao().getOffline(omReq.getTarget(), new Date(omReq.getSince()));
		List<RespMessage> ret=new ArrayList<RespMessage>();
		for(int i=0;i<list.size();i++){
			RespMessage r=new RespMessage();
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
	public void offlineAck(OfflineAckRequest req) throws PersistenceException  {
		// TODO Auto-generated method stub
		String target=req.getId();
		this.getOfflineMessageDao().deleteOffline(target);
	}

}
