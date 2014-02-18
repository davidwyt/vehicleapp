package com.vehicle.app.msg.worker;

import com.vehicle.app.activities.RecentContactListActivity;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.msg.bean.RecentMessage;
import com.vehicle.app.utils.ActivityUtil;
import com.vehicle.app.utils.Constants;

import android.content.Context;
import android.content.Intent;

public abstract class MessageBaseRecipient implements IMessageRecipient {

	protected Context context;

	public MessageBaseRecipient(Context context) {
		this.context = context;
	}

	protected boolean shouldNotifyBar() {
		return true;
	}

	public void updateRecentMessage(RecentMessage recentMsg) {
		// TODO Auto-generated method stub

		DBManager dbMgr = new DBManager(context.getApplicationContext());
		dbMgr.insertOrUpdateRecentMessage(recentMsg);

		if (ActivityUtil.isRecentMsgTop(context)) {
			Intent msgIntent = new Intent(Constants.ACTION_RECENTMSG_UPDATE);
			msgIntent.putExtra(RecentContactListActivity.KEY_RECENTMSG, recentMsg);
			context.sendBroadcast(msgIntent);
		}
	}
}
