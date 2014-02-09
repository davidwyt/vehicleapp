package com.vehicle.imserver.utils;

import java.util.Map;

import com.vehicle.imserver.service.exception.PushNotificationFailedException;
import com.vehicle.service.bean.Notifications;

import cn.jpush.api.DeviceEnum;
import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;

public class JPushUtil {
	private static final String appKey = "ee54a39cda5f4cf0ed4e2617";
	private static final String masterSecret = "94e283e4837ba401fcb12c08";

	private static long timeToLive = 0;

	private static JPushClient androidJpush = null;
	private static JPushClient iosJpush = null;
	private static JPushUtil instance = new JPushUtil();

	private JPushUtil() {
		androidJpush = new JPushClient(masterSecret, appKey, timeToLive,
				DeviceEnum.Android);
		iosJpush = new JPushClient(masterSecret, appKey, timeToLive,
				DeviceEnum.IOS);
	}

	public static JPushUtil getInstance() {
		return instance;
	}

	public MessageResult SendAndroidMessage(String target, String title,
			String content) {
		return androidJpush.sendCustomMessageWithAlias(getRandomSendNo(),
				target, title, content);
	}

	public MessageResult SendIOSMessage(String target, String title,
			String content) {
		return iosJpush.sendNotificationWithAlias(getRandomSendNo(), target,
				title, content);
	}

	public MessageResult SendAndroidNotification(String target, String title,
			String content) {
		return androidJpush.sendNotificationWithAlias(getRandomSendNo(),
				target, title, content);
	}

	public MessageResult SendAndroidNotification(String target, String title,
			String content, Map<String, Object> extras) {
		return androidJpush.sendNotificationWithAlias(getRandomSendNo(),
				target, title, content, 0, extras);
	}

	public void SendNotification(String target, String title, String content)
			throws PushNotificationFailedException {
		MessageResult msgAndroidResult = null;
		MessageResult msgIosResult = null;
		try {
			msgAndroidResult = SendAndroidMessage(target, title, content);

			msgIosResult = SendIOSMessage(target,
					title, content);

		} catch (Exception e) {
			throw new PushNotificationFailedException(e);
		}

		if (null != msgAndroidResult && null != msgIosResult) {
			if (ErrorCodeEnum.NOERROR.value() == msgAndroidResult.getErrcode()
					|| ErrorCodeEnum.NOERROR.value() == msgIosResult
							.getErrcode()) {

			} else {
				if (ErrorCodeEnum.NOERROR.value() != msgAndroidResult
						.getErrcode())
					throw new PushNotificationFailedException(
							msgAndroidResult.getErrmsg());
				if (ErrorCodeEnum.NOERROR.value() != msgIosResult.getErrcode())
					throw new PushNotificationFailedException(
							msgIosResult.getErrmsg());
			}
		} else {
			if (null == msgAndroidResult)
				throw new PushNotificationFailedException(
						"no android push result");
			if (null == msgIosResult)
				throw new PushNotificationFailedException("no ios push result");
		}
	}

	private static final int MAX = Integer.MAX_VALUE;
	private static final int MIN = (int) MAX / 2;

	private static int getRandomSendNo() {
		return (int) (MIN + Math.random() * (MAX - MIN));
	}

}
