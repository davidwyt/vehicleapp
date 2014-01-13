package com.vehicle.imserver.utils;

public class MessageUtil {
	
	public static String FormatSendingMsg(String msgId, String content)
	{
		return String.format("%s%s%s", msgId, Contants.MESSAGE_FORMAT_SEPERATOR, content);
	}
}
