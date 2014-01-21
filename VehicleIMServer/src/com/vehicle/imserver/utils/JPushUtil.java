package com.vehicle.imserver.utils;

import java.util.Map;

import cn.jpush.api.DeviceEnum;
import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;

public class JPushUtil {
	private static final String appKey = "ee54a39cda5f4cf0ed4e2617";
	private static final String masterSecret = "94e283e4837ba401fcb12c08";

	private static long timeToLive = 60 * 60 * 24;

	private static JPushClient jpush = null;
	private static JPushClient iosJpush = null;
	private static JPushUtil instance = new JPushUtil();

	private JPushUtil() {
		jpush = new JPushClient(masterSecret, appKey, timeToLive);
		iosJpush = new JPushClient(masterSecret, appKey, timeToLive,DeviceEnum.IOS);
	}

	public static JPushUtil getInstance() {
		return instance;
	}

	public MessageResult SendAndroidMessage(String target, String title, String content)
	{
		return jpush.sendCustomMessageWithAlias(getRandomSendNo(), target, title, content);
	}
	
	public MessageResult SendIOSMessage(String target, String title, String content)
	{
		return iosJpush.sendNotificationWithAlias(getRandomSendNo(), target, title, content);
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
