package com.vehicle.app.msg.worker;

import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.NotificationMgr;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.sdk.client.VehicleClient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

public class FollowshipInvMessageRecipient extends MessageBaseRecipient {

	public FollowshipInvMessageRecipient(Context context, boolean wakeup) {
		super(context, wakeup);
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

					VehicleClient client = new VehicleClient(SelfMgr.getInstance().getId());
					client.FollowshipInvAck(msg.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (shouldNotifyBar()) {
					try {
						NotificationMgr notificationMgr = new NotificationMgr(context);
						notificationMgr.notifyNewFollowshipInvitationMsg(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			asyncTask.execute();
		}

	}

	@Override
	protected boolean shouldNotifyBar() {
		return true;
	}
}
