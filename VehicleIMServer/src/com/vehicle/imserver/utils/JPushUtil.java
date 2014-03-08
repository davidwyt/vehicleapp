package com.vehicle.imserver.utils;

import java.lang.reflect.Type;
import java.util.Map;

import com.vehicle.imserver.dao.bean.FollowshipInvitation;
import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.service.bean.FollowshipInvitationAcceptNotification;
import com.vehicle.service.bean.FollowshipInvitationNotification;
import com.vehicle.service.bean.FollowshipInvitationRejectNotification;
import com.vehicle.service.bean.NewFileNotification;
import com.vehicle.service.bean.Notifications;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
			int msgType = 0;
			Map<String, Object> extras = null;
			if (title.equals("chat")) {
				Message m = new Gson().fromJson(content, Message.class);
				extras = m.toMap();
			} else if (title.equals(Notifications.NewFile.toString())) {
				NewFileNotification n = new Gson().fromJson(content,
						NewFileNotification.class);
				extras = n.toMap();
			} else if (title.equals(Notifications.FollowshipInvitation
					.toString())) {
				FollowshipInvitationNotification f = new Gson().fromJson(
						content, FollowshipInvitationNotification.class);
				extras = f.toMap();
			} else if (title.equals(Notifications.FollowshipInvitationAccepted
					.toString())) {
				FollowshipInvitationAcceptNotification f = new Gson().fromJson(
						content, FollowshipInvitationAcceptNotification.class);
				extras = f.toMap();
			} else if (title.equals(Notifications.FollowshipInvitationRejected
					.toString())) {
				FollowshipInvitationRejectNotification f = new Gson().fromJson(
						content, FollowshipInvitationRejectNotification.class);
				extras = f.toMap();
			}
			String desc = "";
			if (extras != null && extras.get("messageType") != null) {
				msgType = (int) extras.get("messageType");

				switch (msgType) {
				case 0:
					desc = "您收到一条消息";
					break;
				case 1:
					desc = "您收到一张图片";
					break;
				case 2:
					desc = "您收到一条位置信息";
					break;
				case 3:
					desc = "您收到一段语音";
					break;
				}
			}
			iosJpush.sendNotificationWithAlias(getRandomSendNo(), target,
					title, desc, 0, extras);
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
