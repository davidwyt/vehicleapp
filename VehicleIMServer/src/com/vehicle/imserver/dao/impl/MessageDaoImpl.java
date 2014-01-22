package com.vehicle.imserver.dao.impl;

import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.dao.interfaces.MessageDao;

public class MessageDaoImpl extends BaseDaoImpl<Message> implements MessageDao{
	
	public void InsertMessage(Message msg)
	{
		this.save(msg);
	}
	
}
