package com.vehicle.app.utils;

import java.util.List;

import com.vehicle.app.activities.ChatActivity;
import com.vehicle.app.activities.RecentContactListActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.view.View;

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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static void showProgress(Context context, final View statusView, final View formView, final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

			statusView.setVisibility(View.VISIBLE);
			statusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							statusView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

			formView.setVisibility(View.VISIBLE);
			formView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							formView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			statusView.setVisibility(show ? View.VISIBLE : View.GONE);
			formView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

}
