package com.vehicle.imserver.utils;

import java.util.UUID;

public class GUIDUtil {
	
	public static String genNewGuid()
	{
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
}
