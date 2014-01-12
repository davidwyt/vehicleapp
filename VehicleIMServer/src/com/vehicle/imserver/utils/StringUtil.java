package com.vehicle.imserver.utils;

public class StringUtil {
	
	public static boolean isEmptyOrNull(String str)
	{
		return null == str || 0 >= str.length();
	}
}
