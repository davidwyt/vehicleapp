package com.vehicle.app.utils;

import com.google.gson.Gson;

public class JsonUtil {
	
	public static String toJsonString(Object o)
	{
		Gson gson = new Gson();
		
		return gson.toJson(o);
	}
	
	public static <T> T fromJson(String str, Class<?> T)
	{
		Gson gson = new Gson();
		return (T) gson.fromJson(str, T);
	}
}
