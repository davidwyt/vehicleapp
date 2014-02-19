package com.vehicle.app.msg.worker;

import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.NotificationMgr;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.InvitationVerdict;
import com.vehicle.app.msg.bean.InvitationVerdictMessage;
import com.vehicle.app.msg.bean.RecentMessage;

import android.content.Context;

public class InvitationVerdictMessageRecipient extends MessageBaseRecipient {

	public InvitationVerdictMessageRecipient(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void receive(IMessageItem item) {
		// TODO Auto-generated method stub
		if (!(item instanceof InvitationVerdictMessage)) {
			throw new IllegalArgumentException("item is not InvitationVerdictMessage");
		}

		InvitationVerdictMessage msg = (InvitationVerdictMessage) item;

		try {
			DBManager dbManager = new DBManager(context);
			dbManager.insertInvitationVerdictMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (InvitationVerdict.ACCEPTED.equals(msg.getVerdict())) {
			RecentMessage recentMsg = new RecentMessage();
			recentMsg.setSelfId(SelfMgr.getInstance().getId());
			recentMsg.setFellowId(msg.getTarget());
			recentMsg.setMessageType(msg.getMessageType());
			recentMsg.setContent("");
			recentMsg.setSentTime(msg.getSentTime());
			recentMsg.setMessageId(msg.getInvitationId());

			updateRecentMessage(recentMsg);
		}

		if (shouldNotifyBar()) {
			NotificationMgr notificationMgr = new NotificationMgr(context);
			notificationMgr.notifyNewInvitationVerdictMsg(msg);
		}
	}

	@Override
	protected boolean shouldNotifyBar() {
		return true;
	}
}
