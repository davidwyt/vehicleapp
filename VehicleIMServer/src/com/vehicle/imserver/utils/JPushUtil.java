package com.vehicle.imserver.utils;

import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.service.bean.INotification;

import cn.jpush.api.ErrorCodeEnum;
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

	public void SendCustomMessage(Message msg) {
		int sendNo = getRandomSendNo();
		String msgTitle = "+;//jpush\"\"";
		
		String msgContent = MessageUtil.FormatSendingMsg(msg.getId(), msg.getContent());
		MessageResult msgResult = jpush.sendCustomMessageWithAppKey(sendNo,
				msgTitle, msgContent);

		if (null != msgResult) {
			System.out.println(String.format("from jpush server: %s",
					msgResult.toString()));
			if (msgResult.getErrcode() == ErrorCodeEnum.NOERROR.value()) {
				System.out.println(String.format(
						"send success， sendNo= %s,messageId= %s",
						msgResult.getSendno(), msgResult.getMsg_id()));
			} else {
				System.out.println(String.format(
						"send failed， errorcode=%s, errormsg=%s",
						msgResult.getErrcode(), msgResult.getErrmsg()));
			}
		} else {
			System.out.println("can't retrieve msg");
		}
	}

	public MessageResult SendNotification(INotification notification)
	{
		return jpush.sendNotificationWithAlias(getRandomSendNo(), notification.getTarget(), notification.getTitle(), notification.getContent(), 0, notification.getExtras());
	}
	
	private static final int MAX = Integer.MAX_VALUE;
	private static final int MIN = (int) MAX / 2;

	private static int getRandomSendNo() {
		return (int) (MIN + Math.random() * (MAX - MIN));
	}
	
}
