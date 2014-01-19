package com.vehicle.app.utils;

public class UserUtil {

	public static boolean isSelf(String user)
	{
		return Constants.SELFID.equals(user);
	}

}
