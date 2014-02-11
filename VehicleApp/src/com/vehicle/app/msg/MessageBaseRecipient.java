package com.vehicle.app.msg;

import java.util.List;

import com.vehicle.app.activities.ChatActivity;

import android.app.ActivityManager;
import android.content.Context;

public abstract class MessageBaseRecipient implements IMessageRecipient {

	protected Context context;

	public MessageBaseRecipient(Context context) {
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
