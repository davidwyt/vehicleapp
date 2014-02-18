package com.vehicle.app.mgrs;

import java.util.Hashtable;
import java.util.Map;

public class MessageMgr {

	private Map<String, Integer> mUnreadMsgNumMap;

	private MessageMgr() {
		mUnreadMsgNumMap = new Hashtable<String, Integer>();
	}

	private static class InstanceHolder {
		private static MessageMgr instance = new MessageMgr();
	}

	public static MessageMgr getInstance() {
		return InstanceHolder.instance;
	}

	public void clear() {
		this.mUnreadMsgNumMap.clear();
	}
}
