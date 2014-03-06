package com.vehicle.imserver.utils;

import cn.jpush.api.DeviceEnum;
import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.JPushClient;
import cn.jpush.api.MessageResult;
import cn.jpush.api.receive.ReceiveResult;

public class JPushUtil {
	private static final String appKey = "ee54a39cda5f4cf0ed4e2617";
	private static final String masterSecret = "94e283e4837ba401fcb12c08";

	private static long timeToLive = 0;

	private static JPushClient androidJpush = null;
	private static JPushClient iosJpush = null;
	private static JPushUtil instance = new JPushUtil();
	private static JPushClient reportClient = null;

	private JPushUtil() {
		androidJpush = new JPushClient(masterSecret, appKey, timeToLive,
				DeviceEnum.Android);
		iosJpush = new JPushClient(masterSecret, appKey, timeToLive,
				DeviceEnum.IOS);

		reportClient = new JPushClient(masterSecret, appKey, timeToLive);
	}

	public static JPushUtil getInstance() {
		return instance;
	}

	public int GetIOSMessageReceivedNum(MessageResult iosResult) {
		ReceiveResult iosRecResult = null;
		try {
			if (null != iosResult
					&& iosResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
				iosRecResult = reportClient.getReceived(iosResult.getMsg_id()
						.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null == iosRecResult ? 0 : iosRecResult.getIos_apns_sent();
	}

	public int GetAndroidMessageReceivedNum(MessageResult andResult) {
		ReceiveResult andRecResult = null;

		try {
			if (null != andResult
					&& andResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
				andRecResult = reportClient.getReceived(andResult.getMsg_id()
						.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null == andRecResult ? 0 : andRecResult.getAndroid_received();
	}

	public void SendAndroidMessage(String target, String title, String content) {
		try {
			androidJpush.sendCustomMessageWithAlias(getRandomSendNo(), target,
					title, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SendIOSMessage(String target, String title, String content) {
		try {
			iosJpush.sendNotificationWithAlias(getRandomSendNo(), target,
					title, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SendAllMessage(String target, String title, String content) {
		SendAndroidMessage(target, title, content);
		SendIOSMessage(target, title, content);
	}

	private static final int MAX = Integer.MAX_VALUE;
	private static final int MIN = (int) MAX / 2;

	private static int getRandomSendNo() {
		return (int) (MIN + Math.random() * (MAX - MIN));
	}

}
