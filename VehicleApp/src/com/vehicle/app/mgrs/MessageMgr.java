package com.vehicle.app.mgrs;

import java.util.HashMap;
import java.util.List;

import com.vehicle.app.bean.Message;

public class MessageMgr {
	
	private HashMap<String, List<Message>> messageMap = new HashMap<String, List<Message>>();
	
	private static class InstanceHolder{
		private static MessageMgr instance = new MessageMgr();
	}
	
	public static MessageMgr getInstance()
	{
		return InstanceHolder.instance;
	}
	
	public List<Message> getMessageByUser(String id)
	{
		return messageMap.get(id);
	}
	
	public void setMessage(String id, List<Message> msgs)
	{
		messageMap.put(id, msgs);
	}
}
