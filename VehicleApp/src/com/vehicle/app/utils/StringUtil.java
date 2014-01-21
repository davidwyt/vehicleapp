package com.vehicle.app.utils;

public class StringUtil {

	public static boolean IsNullOrEmpty(String str)
	{
		return null == str || str.isEmpty();
	}
	
	public static boolean IsEmail(String str)
	{
		return str.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+");
	}
}
