package com.vehicle.app.mgrs;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.activities.ChatActivity;
import com.vehicle.app.activities.FollowshipInvitationActivity;
import com.vehicle.app.activities.SettingActivity;
import com.vehicle.app.activities.SettingHomeActivity;
import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.bean.InvitationVerdictMessage;
import com.vehicle.app.msg.bean.FileMessage;
import com.vehicle.app.msg.bean.TextMessage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationMgr {

	private static int notificationNum = 0;

	private synchronized static int nextNotificationNum() {
		notificationNum++;
		if (notificationNum >= Integer.MAX_VALUE) {
			notificationNum = 0;
		}

		return notificationNum;
	}

	private static final String FORMAT_NOTIFICATION_NEWTEXTMSG = "%s said:%s";
	private static final String FORMAT_NOTIFICATION_NEWPICMSG = "%s sent a new picture";
	private static final String FORMAT_NOTIFICATION_NEWINVVERIDCT = "%s %s your follow invitation";
	private static final String FORMAT_NOTIFICATION_NEWINVITATION = "%s send you a followship invitation";

	private Context context;

	public NotificationMgr(Context context) {
		this.context = context;
	}

	public void notifyNewTextMsg(TextMessage msg) {
		int num = nextNotificationNum();
		String ticker = String.format(FORMAT_NOTIFICATION_NEWTEXTMSG, msg.getSource(), msg.getContent());

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.jpush_notification_icon).setContentTitle("New Text Message")
				.setContentText(msg.getContent())
				.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
				.setTicker(ticker);

		Intent resultIntent = new Intent(context, ChatActivity.class);
		resultIntent.putExtra(ChatActivity.KEY_FELLOWID, msg.getSource());
		resultIntent.putExtra(ChatActivity.KEY_CHATSTYLE, ChatActivity.CHAT_STYLE_2ONE);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notificationBuilder.setContentIntent(resultPendingIntent);
		notificationBuilder.setAutoCancel(true);

		NotificationManager notificationMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationMgr.notify(num, notificationBuilder.build());
	}

	public void notifyNewFileMsg(FileMessage msg) {

		int num = nextNotificationNum();
		String ticker = String.format(FORMAT_NOTIFICATION_NEWPICMSG, msg.getSource());

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.jpush_notification_icon).setContentTitle("New Picture Message")
				.setContentText(ticker)
				.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
				.setTicker(ticker);

		Intent resultIntent = new Intent(context, ChatActivity.class);
		resultIntent.putExtra(ChatActivity.KEY_FELLOWID, msg.getSource());
		resultIntent.putExtra(ChatActivity.KEY_CHATSTYLE, ChatActivity.CHAT_STYLE_2ONE);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notificationBuilder.setContentIntent(resultPendingIntent);
		notificationBuilder.setAutoCancel(true);

		NotificationManager notificationMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationMgr.notify(num, notificationBuilder.build());
	}

	public void notifyNewInvitationVerdictMsg(InvitationVerdictMessage msg) {
		int num = nextNotificationNum();
		String ticker = String.format(FORMAT_NOTIFICATION_NEWINVVERIDCT, msg.getSource(), msg.getVerdict().toString());

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.jpush_notification_icon).setContentTitle("New Invitation Verdict Message")
				.setContentText(ticker)
				.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
				.setTicker(ticker);

		Intent resultIntent = new Intent(context, SettingHomeActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notificationBuilder.setContentIntent(resultPendingIntent);
		notificationBuilder.setAutoCancel(true);

		NotificationManager notificationMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationMgr.notify(num, notificationBuilder.build());
	}
	
	public void notifyNewFollowshipInvitationMsg(FollowshipInvitationMessage msg) {
		int num = nextNotificationNum();
		String ticker = String.format(FORMAT_NOTIFICATION_NEWINVITATION, msg.getSource());

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.jpush_notification_icon).setContentTitle("New Followship Invitation Message")
				.setContentText(ticker)
				.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
				.setTicker(ticker);

		Intent resultIntent = new Intent(context, FollowshipInvitationActivity.class);
		resultIntent.putExtra(FollowshipInvitationActivity.KEY_FOLLOWSHIPINVITATION, msg);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notificationBuilder.setContentIntent(resultPendingIntent);
		notificationBuilder.setAutoCancel(true);

		NotificationManager notificationMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationMgr.notify(num, notificationBuilder.build());
	}
	
}
