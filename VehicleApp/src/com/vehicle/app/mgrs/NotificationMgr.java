package com.vehicle.app.mgrs;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.activities.ChatActivity;
import com.vehicle.app.bean.PictureMessageItem;
import com.vehicle.app.bean.TextMessageItem;

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

	private Context context;

	public NotificationMgr(Context context) {
		this.context = context;
	}

	public void notifyNewTextMsg(TextMessageItem msg) {
		int num = nextNotificationNum();
		String ticker = String.format(FORMAT_NOTIFICATION_NEWTEXTMSG, msg.getSource(), msg.getContent());

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.jpush_notification_icon).setContentTitle("New Text Message")
				.setContentText(msg.getContent())
				.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
				.setTicker(ticker);

		Intent resultIntent = new Intent(context, ChatActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notificationBuilder.setContentIntent(resultPendingIntent);
		notificationBuilder.setAutoCancel(true);

		NotificationManager notificationMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationMgr.notify(num, notificationBuilder.build());
	}

	public void notifyNewFileMsg(PictureMessageItem msg) {

		int num = nextNotificationNum();
		String ticker = String.format(FORMAT_NOTIFICATION_NEWPICMSG, msg.getSource());

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.jpush_notification_icon).setContentTitle("New Picture Message")
				.setContentText(ticker)
				.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
				.setTicker(ticker);

		Intent resultIntent = new Intent(context, ChatActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notificationBuilder.setContentIntent(resultPendingIntent);
		notificationBuilder.setAutoCancel(true);

		NotificationManager notificationMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationMgr.notify(num, notificationBuilder.build());
	}
}
