package com.vehicle.app.activities;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.StringUtil;
import com.vehicle.service.bean.Notifications;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import cn.edu.sjtu.vehicleapp.R;
import cn.jpush.android.api.JPushInterface;

public class MessageReceiver extends BroadcastReceiver {
	private static final String TAG = "MyJPushReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "接收Registration Id : " + regId);
			// send the Registration Id to your server...
		} else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "接收UnRegistration Id : " + regId);
			// send the UnRegistration Id to your server...
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

			processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			Log.d(TAG, "用户点击打开了通知");

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			Log.d(TAG, "用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else {
			Log.d(TAG, "Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	private void processCustomMessage(Context context, Bundle bundle) {

		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		String title = bundle.getString(JPushInterface.EXTRA_TITLE);

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

		String topActivity = taskInfo.get(0).topActivity.getClassName();

		// System.out.println("CURRENT Activity ::" +
		// taskInfo.get(0).topActivity.getClassName());

		if (!topActivity.equals(ChatActivity.class.getCanonicalName())) {
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
					.setSmallIcon(R.drawable.jpush_notification_icon)
					.setContentTitle(title)
					.setContentText(message)
					.setDefaults(
							Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
					.setTicker(title);

			Intent resultIntent = new Intent(context, ChatActivity.class);
			PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			notificationBuilder.setContentIntent(resultPendingIntent);
			notificationBuilder.setAutoCancel(true);

			NotificationManager notificationMgr = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationMgr.notify(R.string.app_name, notificationBuilder.build());
		}

		if (Notifications.NewMessage.toString().equalsIgnoreCase(title)) {
			onNewMessageReceived(context, title, message, extras);
		} else if (Notifications.NewFile.toString().equalsIgnoreCase(title)) {
			onNewFileReceived(context, title, message, extras);
		}
	}

	private void onNewMessageReceived(Context context, String title, String message, String extras) {

		Intent msgIntent = new Intent(Constants.ACTION_MESSAGE_RECEIVED);
		msgIntent.putExtra(ChatActivity.KEY_MESSAGE, message);
		msgIntent.putExtra(ChatActivity.KEY_TITLE, title);

		if (StringUtil.IsNullOrEmpty(extras)) {
			try {
				JSONObject extraJson = new JSONObject(extras);
				if (null != extraJson && extraJson.length() > 0) {
					msgIntent.putExtra(ChatActivity.KEY_EXTRAS, extras);
				}
			} catch (JSONException e) {

			}
		}
		context.sendBroadcast(msgIntent);
	}

	private void onNewFileReceived(Context context, String title, String message, String extras) {

		Intent intent = new Intent(Constants.ACTION_NEWFILE_RECEIVED);
		intent.putExtra(ChatActivity.KEY_MESSAGE, message);
		intent.putExtra(ChatActivity.KEY_TITLE, title);

		context.sendBroadcast(intent);
	}
}
