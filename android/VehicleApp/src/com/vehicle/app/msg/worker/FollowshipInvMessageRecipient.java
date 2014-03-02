package com.vehicle.app.msg.worker;

import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.NotificationMgr;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.bean.IMessageItem;

import android.content.Context;
import android.os.AsyncTask;

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

		final FollowshipInvitationMessage msg = (FollowshipInvitationMessage) item;

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub

				SelfMgr.getInstance().retrieveInfo(msg.getSource());

				try {
					DBManager dbManager = new DBManager(context);
					dbManager.insertFollowshipInvMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (shouldNotifyBar()) {
					NotificationMgr notificationMgr = new NotificationMgr(context);
					notificationMgr.notifyNewFollowshipInvitationMsg(msg);
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
