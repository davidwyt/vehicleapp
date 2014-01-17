package com.vehicle.imserver.dao.impl;

import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.dao.bean.MessageStatus;
import com.vehicle.imserver.dao.interfaces.MessageDao;
import com.vehicle.imserver.service.exception.MessageNotFoundException;

public class MessageDaoImpl extends BaseDaoImpl<Message> implements MessageDao{
	
	public void InsertMessage(Message msg)
	{
		this.save(msg);
	}
	
	public void UpdateMessageStatus(String msgId, MessageStatus status) throws MessageNotFoundException
	{
		Message message=this.get(msgId);
		if(null == message)
		{
			throw new MessageNotFoundException(String.format("message %s not found", msgId));
		} else {
			message.setStatus(status);
			this.update(message);
		}
	}
	
}
