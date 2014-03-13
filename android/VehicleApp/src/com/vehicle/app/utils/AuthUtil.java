package com.vehicle.app.utils;

import java.util.Hashtable;
import java.util.Map;

public class AuthUtil {

	private final static String[] unSafeActivities = { "com.vehicle.app.activities.RecentContactListActivity",
			"com.vehicle.app.activities.MyFellowListActivity", "com.vehicle.app.activities.ChatActivity",
			"com.vehicle.app.activities.VendorRatingActivity", "com.vehicle.app.activities.MyCommentsActivity",
			"com.vehicle.app.activities.AdviceActivity", "com.vehicle.app.activities.EccActivity",
			"com.vehicle.app.activities.DriverHomeActivity", "com.vehicle.app.activities.MsgMgrActivity" };
	private final static Map<String, Boolean> unSafeMap = new Hashtable<String, Boolean>();

	static {
		for (String ac : unSafeActivities) {
			unSafeMap.put(ac, Boolean.TRUE);
		}
	}

	public static boolean isReqAuth(String oper) {
		return unSafeMap.containsKey(oper);
	}

}
