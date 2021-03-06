package com.vehicle.app.msg.worker;

import com.vehicle.app.activities.RecentContactListActivity;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.msg.bean.RecentMessage;
import com.vehicle.app.utils.ActivityUtil;
import com.vehicle.app.utils.Constants;

import android.content.Context;
import android.content.Intent;

public abstract class MessageBaseCourier implements IMessageCourier {

	protected Context context;

	public MessageBaseCourier(Context context) {
		this.context = context;
	}

	public void updateRecentMessage(RecentMessage recentMsg) {
		// TODO Auto-generated method stub

		try {
			DBManager dbMgr = new DBManager(context.getApplicationContext());
			dbMgr.insertOrUpdateRecentMessage(recentMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (ActivityUtil.isRecentMsgTop(context)) {
			Intent msgIntent = new Intent(Constants.ACTION_RECENTMSG_UPDATE);
			msgIntent.putExtra(RecentContactListActivity.KEY_RECENTMSG, recentMsg);
			context.sendBroadcast(msgIntent);
		}
	}
}
