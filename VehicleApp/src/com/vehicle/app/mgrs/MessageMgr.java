package com.vehicle.app.mgrs;

import java.util.HashMap;
import java.util.List;

import com.vehicle.app.msg.TextMessageItem;

public class MessageMgr {

	private HashMap<String, List<TextMessageItem>> messageMap = new HashMap<String, List<TextMessageItem>>();

	private static class InstanceHolder {
		private static MessageMgr instance = new MessageMgr();
	}

	public static MessageMgr getInstance() {
		return InstanceHolder.instance;
	}

	public List<TextMessageItem> getMessageByUser(String id) {
		return messageMap.get(id);
	}

	public void setMessage(String id, List<TextMessageItem> msgs) {
		messageMap.put(id, msgs);
	}
}
