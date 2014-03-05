package com.vehicle.app.mgrs;

import java.util.Stack;

import android.app.Activity;

public class ActivityManager {

	private static Stack<Activity> activityStack = new Stack<Activity>();

	private static class InstanceHolder {
		private static ActivityManager instance = new ActivityManager();
	}

	public static ActivityManager getInstance() {
		return InstanceHolder.instance;
	}

	public synchronized void pushActivity(Activity item) {
		activityStack.push(item);
	}

	public synchronized void finishAll() {
		while (activityStack.size() > 0) {
			Activity top = activityStack.pop();
			if (null != top) {
				try {
					top.finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public synchronized void popActivity(Activity item) {
		if (null != item) {
			try {
				activityStack.remove(item);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
