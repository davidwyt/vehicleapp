package com.vehicle.app.utils;

import java.util.List;

import com.vehicle.app.activities.ChatActivity;
import com.vehicle.app.activities.RecentContactListActivity;

import android.app.ActivityManager;
import android.content.Context;

public class ActivityUtil {

	public static String getTopActivity(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

		String topActivity = taskInfo.get(0).topActivity.getClassName();

		return topActivity;
	}

	public static boolean IsChattingWithFellow(Context context, String id) {
		return ChatActivity.class.getCanonicalName().equals(getTopActivity(context))
				&& ChatActivity.getCurrentFellowId().equals(id);
	}

	public static boolean isRecentMsgTop(Context context) {
		return RecentContactListActivity.class.getCanonicalName().equals(getTopActivity(context));
	}

}
