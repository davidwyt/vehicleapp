package com.vehicle.app.msg.worker;

import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.NotificationMgr;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.InvitationVerdict;
import com.vehicle.app.msg.bean.InvitationVerdictMessage;
import com.vehicle.app.msg.bean.RecentMessage;

import android.content.Context;
import android.os.AsyncTask;

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

		final InvitationVerdictMessage msg = (InvitationVerdictMessage) item;

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub

				SelfMgr.getInstance().retrieveInfo(msg.getSource());

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

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (shouldNotifyBar()) {
					NotificationMgr notificationMgr = new NotificationMgr(context);
					notificationMgr.notifyNewInvitationVerdictMsg(msg);
				}

			}
		};

		asyncTask.execute((Void) null);
	}

	@Override
	protected boolean shouldNotifyBar() {
		return true;
	}
}
