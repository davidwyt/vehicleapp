package com.vehicle.app.mgrs;

import java.util.HashMap;
import java.util.List;

import com.vehicle.app.msg.bean.TextMessage;

public class MessageMgr {

	private HashMap<String, List<TextMessage>> messageMap = new HashMap<String, List<TextMessage>>();

	private static class InstanceHolder {
		private static MessageMgr instance = new MessageMgr();
	}

	public static MessageMgr getInstance() {
		return InstanceHolder.instance;
	}

	public List<TextMessage> getMessageByUser(String id) {
		return messageMap.get(id);
	}

	public void setMessage(String id, List<TextMessage> msgs) {
		messageMap.put(id, msgs);
	}
}
