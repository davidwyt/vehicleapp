package com.vehicle.imserver.utils;

import java.util.Map;

import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.service.bean.INotification;

import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;

public class JPushUtil {
	private static final String appKey = "ee54a39cda5f4cf0ed4e2617";
	private static final String masterSecret = "94e283e4837ba401fcb12c08";

	private static long timeToLive = 60 * 60 * 24;

	private static JPushClient jpush = null;
	private static JPushUtil instance = new JPushUtil();

	private JPushUtil() {
		jpush = new JPushClient(masterSecret, appKey, timeToLive);
	}

	public static JPushUtil getInstance() {
		return instance;
	}

	public MessageResult SendCustomMessage(String target, String title, String content)
	{
		return jpush.sendCustomMessageWithAlias(getRandomSendNo(), target, title, content);
	}
	
	public MessageResult SendNotification(String target, String title, String content)
	{
		return jpush.sendNotificationWithAlias(getRandomSendNo(), target, title, content);
	}
	
	public MessageResult SendNotification(String target, String title, String content, Map<String, Object> extras)
	{
		return jpush.sendNotificationWithAlias(getRandomSendNo(), target, title, content, 0, extras);
	}
	
	private static final int MAX = Integer.MAX_VALUE;
	private static final int MIN = (int) MAX / 2;

	private static int getRandomSendNo() {
		return (int) (MIN + Math.random() * (MAX - MIN));
	}
	
}
