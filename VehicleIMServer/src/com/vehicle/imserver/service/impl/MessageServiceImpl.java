package com.vehicle.imserver.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.dao.bean.MessageStatus;
import com.vehicle.imserver.dao.impl.FollowshipDaoImpl;
import com.vehicle.imserver.dao.interfaces.FollowshipDao;
import com.vehicle.imserver.dao.interfaces.MessageDao;
import com.vehicle.imserver.service.bean.MessageACKRequest;
import com.vehicle.imserver.service.bean.One2FolloweesMessageRequest;
import com.vehicle.imserver.service.bean.One2FollowersMessageRequest;
import com.vehicle.imserver.service.bean.One2OneMessageRequest;
import com.vehicle.imserver.service.exception.JPushException;
import com.vehicle.imserver.service.exception.MessageNotFoundException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.interfaces.MessageService;
import com.vehicle.imserver.utils.JPushUtil;

public class MessageServiceImpl implements MessageService{
	
	MessageDao messageDao;
	
	public MessageDao getMessageDao(){
		return this.messageDao;
	}
	
	public void setMessageDao(MessageDao messageDao){
		this.messageDao=messageDao;
	}
	
	FollowshipDao followshipDao;
	public FollowshipDao getFollowshipDao(){
		return this.followshipDao;
	}
	
	public void setFollowshipDao(FollowshipDao followshipDao){
		this.followshipDao=followshipDao;
	}
	
	private void PersistentAndSendMessage(Message msg) throws PersistenceException, JPushException
	{
		try {
			messageDao.InsertMessage(msg);
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}

		try {
			JPushUtil.getInstance().SendCustomMessage(msg);
		} catch (Exception e) {
			throw new JPushException(e.getMessage(), e);
		}
	}
	
	public String SendMessage(
			One2OneMessageRequest msgReq) throws JPushException,
			PersistenceException {

		System.out.println(msgReq.toString());

		Message msg = msgReq.toRawMessage();

		PersistentAndSendMessage(msg);

		return msg.getId();
	}

	public void MessageReceived(MessageACKRequest msgACKReq)
			throws MessageNotFoundException, PersistenceException {

		System.out.println(msgACKReq.toString());

		try {
			messageDao.UpdateMessageStatus(
					msgACKReq.getMsgId(), MessageStatus.RECEIVED);

		} catch (MessageNotFoundException msgNotFoundException) {
			throw msgNotFoundException;
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}
	
	private void PersistentAndSendMessageList(String source, List<String> targets, String content) throws PersistenceException, JPushException
	{
		Date now = new Date();
		for(String target : targets)
		{
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
	
	public void SendMessage2Followees(One2FolloweesMessageRequest msgRequest) throws PersistenceException, JPushException
	{
		System.out.println(msgRequest.toString());
		
		String source = msgRequest.getSource();
		List<String> followees = followshipDao.GetFollowees(source);
		
		PersistentAndSendMessageList(source, followees, msgRequest.getContent());
	}
	
	public void SendMessage2Followers(One2FollowersMessageRequest msgRequest) throws PersistenceException, JPushException
	{
		System.out.println(msgRequest.toString());
		
		String source = msgRequest.getSource();
		List<String> followers = followshipDao.GetFollowers(source);
		
		PersistentAndSendMessageList(source, followers, msgRequest.getContent());
	}
}
