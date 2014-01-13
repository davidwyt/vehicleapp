package com.vehicle.imserver.service.handler;

import java.util.UUID;

import com.vehicle.imserver.persistence.dao.Message;
import com.vehicle.imserver.persistence.handler.MessageDaoHandler;
import com.vehicle.imserver.service.bean.MessageSendingRequest;
import com.vehicle.imserver.service.bean.MessageSendingResponse;
import com.vehicle.imserver.service.bean.MessageSendingStatus;
import com.vehicle.imserver.utils.JPushUtil;

public class MessageServiceHandler {

	public static MessageSendingResponse SendMessage(
			MessageSendingRequest msgReq) {
		
		System.out.println(msgReq.toString());
		
		Message msg = msgReq.toRawMessage();
		MessageDaoHandler.getInstance().InsertMessage(msg);

		JPushUtil.getInstance().SendCustomMessage(msgReq);
		
		MessageSendingResponse resp = new MessageSendingResponse();
		UUID uuid = UUID.randomUUID();
		resp.setMsgId(uuid.toString());
		resp.setStatus(MessageSendingStatus.SUCCESS);

		return resp;
	}
}
