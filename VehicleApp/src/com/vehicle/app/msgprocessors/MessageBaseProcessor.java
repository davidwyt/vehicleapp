package com.vehicle.app.msgprocessors;

import java.util.List;

import com.vehicle.app.activities.ChatActivity;

import android.app.ActivityManager;
import android.content.Context;

public abstract class MessageBaseProcessor implements IMessageProcessor {

	protected Context context;

	public MessageBaseProcessor(Context context) {
		this.context = context;
	}

	public boolean IsChattingWithFellow(String id) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

		String topActivity = taskInfo.get(0).topActivity.getClassName();

		return ChatActivity.class.getCanonicalName().equals(topActivity)
				&& ChatActivity.getCurrentFellowId().equals(id);
	}
}
