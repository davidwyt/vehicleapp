package com.vehicle.app.activities;

import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.bean.InvitationVerdictMessage;
import com.vehicle.app.msg.bean.FileMessage;
import com.vehicle.app.msg.bean.TextMessage;
import com.vehicle.app.msg.worker.FollowshipInvMessageRecipient;
import com.vehicle.app.msg.worker.IMessageRecipient;
import com.vehicle.app.msg.worker.InvitationVerdictMessageRecipient;
import com.vehicle.app.msg.worker.FileMessageRecipient;
import com.vehicle.app.msg.worker.TextMessageRecipient;
import com.vehicle.app.utils.JsonUtil;
import com.vehicle.service.bean.FollowshipInvitationAcceptNotification;
import com.vehicle.service.bean.FollowshipInvitationNotification;
import com.vehicle.service.bean.FollowshipInvitationRejectNotification;
import com.vehicle.service.bean.NewFileNotification;
import com.vehicle.service.bean.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
		String title = bundle.getString(JPushInterface.EXTRA_TITLE);

		if (Notifications.NewMessage.toString().equalsIgnoreCase(title)) {
			onNewMessageReceived(context, message);
		} else if (Notifications.NewFile.toString().equalsIgnoreCase(title)) {
			onNewFileReceived(context, message);
		} else if (Notifications.FollowshipInvitation.toString().equalsIgnoreCase(title)) {

		} else if (Notifications.FollowshipInvitationAccepted.toString().equalsIgnoreCase(title)) {
			onNewInvVerdictAcceptedReceived(context, message);
		} else if (Notifications.FollowshipInvitationRejected.toString().equalsIgnoreCase(title)) {
			onNewInvVerdictRejectedReceived(context, message);
		} else if (Notifications.FollowshipInvitation.toString().equalsIgnoreCase(title)) {
			onNewFollowshipInvitationReceived(context, message);
		}
	}

	private void onNewMessageReceived(Context context, String message) {

		TextMessage msg = JsonUtil.fromJson(message, TextMessage.class);

		IMessageRecipient cpu = new TextMessageRecipient(context);
		cpu.receive(msg);
	}

	private void onNewFileReceived(Context context, String message) {

		NewFileNotification newFileNotification = JsonUtil.fromJson(message, NewFileNotification.class);

		System.out.println("receive new file: " + message);

		FileMessage msg = new FileMessage();
		msg.fromRawNotification(newFileNotification);

		IMessageRecipient cpu = new FileMessageRecipient(context);
		cpu.receive(msg);
	}

	private void onNewInvVerdictAcceptedReceived(Context context, String message) {
		System.out.println("new invition verdict received:" + message);

		FollowshipInvitationAcceptNotification notification = JsonUtil.fromJson(message,
				FollowshipInvitationAcceptNotification.class);

		InvitationVerdictMessage msg = new InvitationVerdictMessage();
		msg.fromRawNotification(notification);

		IMessageRecipient cpu = new InvitationVerdictMessageRecipient(context);
		cpu.receive(msg);
	}

	private void onNewInvVerdictRejectedReceived(Context context, String message) {
		System.out.println("new invition verdict received:" + message);

		FollowshipInvitationRejectNotification notification = JsonUtil.fromJson(message,
				FollowshipInvitationRejectNotification.class);

		InvitationVerdictMessage msg = new InvitationVerdictMessage();
		msg.fromRawNotification(notification);

		IMessageRecipient cpu = new InvitationVerdictMessageRecipient(context);
		cpu.receive(msg);
	}

	private void onNewFollowshipInvitationReceived(Context context, String message) {
		System.out.println("new invition received:" + message);

		FollowshipInvitationNotification notification = JsonUtil.fromJson(message,
				FollowshipInvitationNotification.class);

		FollowshipInvitationMessage msg = new FollowshipInvitationMessage();
		msg.fromRawNotification(notification);

		IMessageRecipient cpu = new FollowshipInvMessageRecipient(context);
		cpu.receive(msg);
	}
}
