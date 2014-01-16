package com.vehicle.imserver.dao.interfaces;

import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.dao.bean.MessageStatus;
import com.vehicle.imserver.service.exception.MessageNotFoundException;

public interface MessageDao extends BaseDao<Message>{
	
	public void InsertMessage(Message msg);
	
	public void UpdateMessageStatus(String msgId, MessageStatus status) throws MessageNotFoundException;

}
