package com.vehicle.imserver.service.handler;

import com.vehicle.imserver.persistence.dao.Message;
import com.vehicle.imserver.persistence.dao.MessageStatus;
import com.vehicle.imserver.persistence.handler.MessageDaoHandler;
import com.vehicle.imserver.service.bean.MessageACKRequest;
import com.vehicle.imserver.service.bean.MessageACKResponse;
import com.vehicle.imserver.service.bean.MessageACKStatus;
import com.vehicle.imserver.service.bean.MessageSendingRequest;
import com.vehicle.imserver.service.bean.MessageSendingResponse;
import com.vehicle.imserver.service.bean.MessageSendingStatus;
import com.vehicle.imserver.service.exception.MessageNotFoundException;
import com.vehicle.imserver.utils.ErrorCodes;
import com.vehicle.imserver.utils.JPushUtil;

public class MessageServiceHandler {

	public static MessageSendingResponse SendMessage(
			MessageSendingRequest msgReq) {

		System.out.println(msgReq.toString());

		Message msg = msgReq.toRawMessage();
		MessageDaoHandler.getInstance().InsertMessage(msg);

		JPushUtil.getInstance().SendCustomMessage(msg);

		MessageSendingResponse resp = new MessageSendingResponse();
		resp.setMsgId(msg.getId());
		resp.setStatus(MessageSendingStatus.SUCCESS);

		return resp;
	}

	public static MessageACKResponse MessageReceived(
			MessageACKRequest msgReceivedReq) {
		
		System.out.println(msgReceivedReq.toString());

		MessageACKResponse resp = new MessageACKResponse();
		resp.setMsgId(msgReceivedReq.getMsgId());
		
		try {
			MessageDaoHandler.getInstance().UpdateMessageStatus(
					msgReceivedReq.getMsgId(), MessageStatus.RECEIVED);
			
			resp.setStatus(MessageACKStatus.SUCCESS);
		} catch (MessageNotFoundException msgNotFoundException) {
			resp.setStatus(MessageACKStatus.FAILED);
			resp.setErrorCode(ErrorCodes.MESSAGE_NOT_FOUND_ERRCODE);
			resp.setErrorMsg(String.format(ErrorCodes.MESSAGE_NOT_FOUND_ERRMSG, msgReceivedReq.getMsgId()));
		} catch (Exception e) {
			resp.setStatus(MessageACKStatus.FAILED);
			resp.setErrorCode(ErrorCodes.UNKNOWN_ERROR_ERRCODE);
			resp.setErrorMsg(String.format(ErrorCodes.UNKNOWN_ERROR_ERRMSG, e.getMessage()));
		}

		return resp;
	}
}
