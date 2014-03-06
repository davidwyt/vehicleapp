package com.vehicle.imserver.dao.interfaces;

import java.util.List;

import com.vehicle.imserver.dao.bean.Message;

public interface MessageDao extends BaseDao<Message>{
	
	public void InsertMessage(Message msg);

	public List<Message> selectAllNewMessage(String memberId);
	
	public void updateAllNewMessage(String memberId);
}
