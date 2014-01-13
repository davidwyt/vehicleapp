package com.vehicle.imserver.service.handler;

import com.vehicle.imserver.persistence.dao.Message;
import com.vehicle.imserver.persistence.dao.MessageStatus;
import com.vehicle.imserver.persistence.handler.MessageDaoHandler;
import com.vehicle.imserver.service.bean.MessageACKRequest;
import com.vehicle.imserver.service.bean.MessageSendingRequest;
import com.vehicle.imserver.service.exception.JPushException;
import com.vehicle.imserver.service.exception.MessageNotFoundException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.utils.JPushUtil;

public class MessageServiceHandler {

	private MessageServiceHandler()
	{
		
	}
	
	public static String SendMessage(
			MessageSendingRequest msgReq) throws JPushException,
			PersistenceException {

		System.out.println(msgReq.toString());

		Message msg = msgReq.toRawMessage();

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
}
