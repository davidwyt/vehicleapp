package com.vehicle.app.msg.worker;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.vehicle.app.activities.ChatActivity;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.NotificationMgr;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.bean.RecentMessage;
import com.vehicle.app.msg.bean.TextMessage;
import com.vehicle.app.utils.ActivityUtil;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.JsonUtil;

public class TextMessageRecipient extends MessageBaseRecipient {

	public TextMessageRecipient(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void receive(IMessageItem msg) {
		// TODO Auto-generated method stub
		if (!(msg instanceof TextMessage)) {
			throw new IllegalArgumentException("msg is not TextMessageItem");
		}

		final TextMessage textMsgItem = (TextMessage) msg;

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				
				textMsgItem.setSentTime(new Date().getTime());
				
				System.out.println("msg received :" + JsonUtil.toJsonString(textMsgItem));
				
				SelfMgr.getInstance().retrieveInfo(textMsgItem.getSource());
				if (ActivityUtil.IsChattingWithFellow(context, textMsgItem.getSource())) {
					textMsgItem.setFlag(MessageFlag.READ);
					try {
						DBManager dbManager = new DBManager(context);
						dbManager.insertTextMessage(textMsgItem);
					} catch (Exception e) {
						e.printStackTrace();
					}

					Intent msgIntent = new Intent(Constants.ACTION_TEXTMESSAGE_RECEIVED);
					msgIntent.putExtra(ChatActivity.KEY_MESSAGE, textMsgItem);
					context.sendBroadcast(msgIntent);
				} else {
					textMsgItem.setFlag(MessageFlag.UNREAD);
					try {
						DBManager dbManager = new DBManager(context);
						dbManager.insertTextMessage(textMsgItem);
					} catch (Exception e) {
						e.printStackTrace();
					}

					NotificationMgr notificationMgr = new NotificationMgr(context);
					if (IMessageItem.MESSAGE_TYPE_TEXT == textMsgItem.getMessageType()) {
						notificationMgr.notifyNewTextMsg(textMsgItem);
					} else if (IMessageItem.MESSAGE_TYPE_LOCATION == textMsgItem.getMessageType()) {
						notificationMgr.notifyNewLocationMsg(textMsgItem);
					}
				}

				RecentMessage recentMsg = new RecentMessage();
				recentMsg.setSelfId(SelfMgr.getInstance().getId());
				recentMsg.setFellowId(textMsgItem.getSource());
				recentMsg.setMessageType(textMsgItem.getMessageType());
				recentMsg.setContent(textMsgItem.getContent());
				recentMsg.setSentTime(textMsgItem.getSentTime());
				recentMsg.setMessageId(textMsgItem.getId());

				updateRecentMessage(recentMsg);

				return null;
			}
		};

		asyncTask.execute((Void) null);
	}

	@Override
	protected boolean shouldNotifyBar() {
		return true;
	}

}
