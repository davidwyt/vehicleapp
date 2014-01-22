package com.vehicle.imserver.dao.interfaces;

import com.vehicle.imserver.dao.bean.Message;

public interface MessageDao extends BaseDao<Message>{
	
	public void InsertMessage(Message msg);
	
}
