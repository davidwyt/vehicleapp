package com.vehicle.app.msgprocessors;

import android.content.Context;
import android.content.Intent;

import com.vehicle.app.activities.ChatActivity;
import com.vehicle.app.bean.IMessageItem;
import com.vehicle.app.bean.MessageFlag;
import com.vehicle.app.bean.TextMessageItem;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.NotificationMgr;
import com.vehicle.app.utils.Constants;

public class TextMessageProcessor extends MessageBaseProcessor{

	public TextMessageProcessor(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void process(IMessageItem msg) {
		// TODO Auto-generated method stub
		if (!(msg instanceof TextMessageItem)) {
			throw new IllegalArgumentException("msg is not TextMessageItem");
		}
		
		TextMessageItem textMsgItem = (TextMessageItem) msg;
		if(IsChattingWithFellow(textMsgItem.getSource()))
		{
			textMsgItem.setFlag(MessageFlag.READ);
			DBManager dbManager = new DBManager(context);
			dbManager.insertTextMessage(textMsgItem);
			
			Intent msgIntent = new Intent(Constants.ACTION_TEXTMESSAGE_RECEIVED);
			msgIntent.putExtra(ChatActivity.KEY_MESSAGE, textMsgItem);
			context.sendBroadcast(msgIntent);
		}else
		{
			textMsgItem.setFlag(MessageFlag.UNREAD);
			DBManager dbManager = new DBManager(context);
			dbManager.insertTextMessage(textMsgItem);
			
			NotificationMgr notificationMgr = new NotificationMgr(context);
			notificationMgr.notifyNewTextMsg(textMsgItem);
		}
	}
	
}
