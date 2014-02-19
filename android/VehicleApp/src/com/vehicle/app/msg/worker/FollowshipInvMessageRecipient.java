package com.vehicle.app.msg.worker;

import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.NotificationMgr;
import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.bean.IMessageItem;

import android.content.Context;

public class FollowshipInvMessageRecipient extends MessageBaseRecipient {

	public FollowshipInvMessageRecipient(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void receive(IMessageItem item) {
		// TODO Auto-generated method stub
		if (!(item instanceof FollowshipInvitationMessage)) {
			throw new IllegalArgumentException("item not FollowshipInvitationMessage");
		}

		FollowshipInvitationMessage msg = (FollowshipInvitationMessage) item;

		try {
			DBManager dbManager = new DBManager(context);
			dbManager.insertFollowshipInvMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (shouldNotifyBar()) {
			NotificationMgr notificationMgr = new NotificationMgr(context);
			notificationMgr.notifyNewFollowshipInvitationMsg(msg);
		}
	}

	@Override
	protected boolean shouldNotifyBar() {
		return true;
	}
}
