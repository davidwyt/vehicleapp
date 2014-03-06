package com.vehicle.app.mgrs;

import junit.framework.Assert;
import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.activities.ChatActivity;
import com.vehicle.app.activities.FollowshipInvitationActivity;
import com.vehicle.app.activities.SettingHomeActivity;
import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.InvitationVerdict;
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

	private Context context;

	public NotificationMgr(Context context) {
		this.context = context;
	}

	public void notifyNewTextMsg(TextMessage msg) {

		Assert.assertEquals(true, msg.getMessageType() == IMessageItem.MESSAGE_TYPE_TEXT);
		if (IMessageItem.MESSAGE_TYPE_TEXT != msg.getMessageType())
			return;

		String ticker = "";
		String name = SelfMgr.getInstance().getFellowName(context, msg.getSource());
		ticker = context.getResources().getString(R.string.tip_notificationformat_newmsg, name, msg.getContent());

		Intent resultIntent = new Intent(context, ChatActivity.class);
		resultIntent.putExtra(ChatActivity.KEY_FELLOWID, msg.getSource());
		resultIntent.putExtra(ChatActivity.KEY_CHATSTYLE, ChatActivity.CHAT_STYLE_2ONE);

		createNotification(ticker, context.getResources().getString(R.string.tip_newmsg), resultIntent);
	}

	public void notifyNewLocationMsg(TextMessage msg) {

		Assert.assertEquals(true, msg.getMessageType() == IMessageItem.MESSAGE_TYPE_LOCATION);
		if (IMessageItem.MESSAGE_TYPE_LOCATION != msg.getMessageType())
			return;

		String ticker = "";
		String name = SelfMgr.getInstance().getFellowName(context, msg.getSource());
		ticker = context.getResources().getString(R.string.tip_notificationformat_newlocation, name, msg.getContent());

		Intent resultIntent = new Intent(context, ChatActivity.class);
		resultIntent.putExtra(ChatActivity.KEY_FELLOWID, msg.getSource());
		resultIntent.putExtra(ChatActivity.KEY_CHATSTYLE, ChatActivity.CHAT_STYLE_2ONE);

		createNotification(ticker, context.getResources().getString(R.string.tip_newlocation), resultIntent);
	}

	public void notifyNewImgMsg(FileMessage msg) {

		Assert.assertEquals(true, msg.getMessageType() == IMessageItem.MESSAGE_TYPE_IMAGE);
		if (IMessageItem.MESSAGE_TYPE_IMAGE != msg.getMessageType())
			return;

		String ticker = "";
		String name = SelfMgr.getInstance().getFellowName(context, msg.getSource());
		ticker = context.getResources().getString(R.string.tip_notificationformat_newfile, name);

		Intent resultIntent = new Intent(context, ChatActivity.class);
		resultIntent.putExtra(ChatActivity.KEY_FELLOWID, msg.getSource());
		resultIntent.putExtra(ChatActivity.KEY_CHATSTYLE, ChatActivity.CHAT_STYLE_2ONE);

		createNotification(ticker, context.getResources().getString(R.string.tip_newfile), resultIntent);
	}

	public void notifyNewAudioMsg(FileMessage msg) {

		Assert.assertEquals(true, msg.getMessageType() == IMessageItem.MESSAGE_TYPE_AUDIO);
		if (IMessageItem.MESSAGE_TYPE_AUDIO != msg.getMessageType())
			return;

		String ticker = "";
		String name = SelfMgr.getInstance().getFellowName(context, msg.getSource());
		ticker = context.getResources().getString(R.string.tip_notificationformat_newaudio, name);

		Intent resultIntent = new Intent(context, ChatActivity.class);
		resultIntent.putExtra(ChatActivity.KEY_FELLOWID, msg.getSource());
		resultIntent.putExtra(ChatActivity.KEY_CHATSTYLE, ChatActivity.CHAT_STYLE_2ONE);

		createNotification(ticker, context.getResources().getString(R.string.tip_newaudio), resultIntent);
	}

	public void notifyNewInvitationVerdictMsg(InvitationVerdictMessage msg) {

		Assert.assertEquals(true, msg.getMessageType() == IMessageItem.MESSAGE_TYPE_INVITATIONVERDICT);
		if (IMessageItem.MESSAGE_TYPE_INVITATIONVERDICT != msg.getMessageType())
			return;

		Assert.assertEquals(true, msg.getVerdict() == InvitationVerdict.ACCEPTED);
		if (InvitationVerdict.ACCEPTED != msg.getVerdict())
			return;

		String ticker = "";
		String name = SelfMgr.getInstance().getFellowName(context, msg.getSource());
		ticker = context.getResources().getString(R.string.tip_notificationformat_newinvverdict, name);

		Intent resultIntent = new Intent(context, SettingHomeActivity.class);
		createNotification(ticker, context.getResources().getString(R.string.tip_newmsg), resultIntent);
	}

	public void notifyNewFollowshipInvitationMsg(FollowshipInvitationMessage msg) {
		String ticker = "";
		String name = SelfMgr.getInstance().getFellowName(context, msg.getSource());
		ticker = context.getResources().getString(R.string.tip_notificationformat_newinv, name);

		Intent resultIntent = new Intent(context, FollowshipInvitationActivity.class);
		resultIntent.putExtra(FollowshipInvitationActivity.KEY_FOLLOWSHIPINVITATION, msg);

		createNotification(ticker, context.getResources().getString(R.string.tip_newinv), resultIntent);
	}

	private void createNotification(String ticker, String title, Intent intent) {
		int num = nextNotificationNum();

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.jpush_notification_icon)
				.setContentTitle(title).setContentText(ticker)
				.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
				.setTicker(ticker);

		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notificationBuilder.setContentIntent(resultPendingIntent);
		notificationBuilder.setAutoCancel(true);

		NotificationManager notificationMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationMgr.notify(num, notificationBuilder.build());
	}
}
