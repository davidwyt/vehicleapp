package com.vehicle.imserver.service.handler;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.vehicle.imserver.persistence.dao.Message;
import com.vehicle.imserver.persistence.dao.MessageStatus;
import com.vehicle.imserver.persistence.handler.FollowshipDaoHandler;
import com.vehicle.imserver.persistence.handler.MessageDaoHandler;
import com.vehicle.imserver.service.bean.MessageACKRequest;
import com.vehicle.imserver.service.bean.One2FolloweesMessageRequest;
import com.vehicle.imserver.service.bean.One2FollowersMessageRequest;
import com.vehicle.imserver.service.bean.One2OneMessageRequest;
import com.vehicle.imserver.service.exception.JPushException;
import com.vehicle.imserver.service.exception.MessageNotFoundException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.utils.JPushUtil;

public class MessageServiceHandler {

	private MessageServiceHandler()
	{
		
	}
	
	private static void PersistentAndSendMessage(Message msg) throws PersistenceException, JPushException
	{
		try {
			MessageDaoHandler.InsertMessage(msg);
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}

		try {
			JPushUtil.getInstance().SendCustomMessage(msg);
		} catch (Exception e) {
			throw new JPushException(e.getMessage(), e);
		}
	}
	
	public static String SendMessage(
			One2OneMessageRequest msgReq) throws JPushException,
			PersistenceException {

		System.out.println(msgReq.toString());

		Message msg = msgReq.toRawMessage();

		PersistentAndSendMessage(msg);

		return msg.getId();
	}

	public static void MessageReceived(MessageACKRequest msgACKReq)
			throws MessageNotFoundException, PersistenceException {

		System.out.println(msgACKReq.toString());

		try {
			MessageDaoHandler.UpdateMessageStatus(
					msgACKReq.getMsgId(), MessageStatus.RECEIVED);

		} catch (MessageNotFoundException msgNotFoundException) {
			throw msgNotFoundException;
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}
	
	private static void PersistentAndSendMessageList(String source, List<String> targets, String content) throws PersistenceException, JPushException
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
	
	public static void SendMessage2Followees(One2FolloweesMessageRequest msgRequest) throws PersistenceException, JPushException
	{
		System.out.println(msgRequest.toString());
		
		String source = msgRequest.getSource();
		List<String> followees = FollowshipDaoHandler.GetFollowees(source);
		
		PersistentAndSendMessageList(source, followees, msgRequest.getContent());
	}
	
	public static void SendMessage2Followers(One2FollowersMessageRequest msgRequest) throws PersistenceException, JPushException
	{
		System.out.println(msgRequest.toString());
		
		String source = msgRequest.getSource();
		List<String> followers = FollowshipDaoHandler.GetFollowers(source);
		
		PersistentAndSendMessageList(source, followers, msgRequest.getContent());
	}
}
