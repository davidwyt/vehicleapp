package com.vehicle.app.msg.worker;

import android.content.Context;
import android.content.Intent;

import com.vehicle.app.activities.ChatActivity;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.NotificationMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.bean.TextMessage;
import com.vehicle.app.utils.ActivityUtil;
import com.vehicle.app.utils.Constants;

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

		TextMessage textMsgItem = (TextMessage) msg;
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
			notificationMgr.notifyNewTextMsg(textMsgItem);
		}
	}
	
	@Override
	protected boolean shouldNotifyBar() {
		return true;
	}

}
