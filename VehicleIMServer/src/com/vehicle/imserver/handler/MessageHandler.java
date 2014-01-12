package com.vehicle.imserver.handler;

import java.util.UUID;

import com.vehicle.imserver.bean.MessageSendingRequest;
import com.vehicle.imserver.bean.MessageSendingResponse;
import com.vehicle.imserver.bean.MessageSendingStatus;
import com.vehicle.imserver.utils.JPushUtil;

public class MessageHandler {

	public static MessageSendingResponse SendMessage(
			MessageSendingRequest msgReq) {
		
		System.out.println(msgReq.toString());
		
		JPushUtil.getInstance().SendCustomMessage(msgReq);
		
		MessageSendingResponse resp = new MessageSendingResponse();
		UUID uuid = UUID.randomUUID();
		resp.setMsgId(uuid.toString());
		resp.setStatus(MessageSendingStatus.SUCCESS);

		return resp;
	}
}
