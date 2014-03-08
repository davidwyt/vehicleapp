package com.vehicle.app.msg.worker;

import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.NotificationMgr;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.InvitationVerdict;
import com.vehicle.app.msg.bean.InvitationVerdictMessage;
import com.vehicle.app.msg.bean.RecentMessage;
import com.vehicle.sdk.client.VehicleClient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

public class InvitationVerdictMessageRecipient extends MessageBaseRecipient {

	public InvitationVerdictMessageRecipient(Context context, boolean wakeup) {
		super(context, wakeup);
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
					recentMsg.setFellowId(msg.getSource());
					recentMsg.setMessageType(msg.getMessageType());
					recentMsg.setContent(InvitationVerdict.ACCEPTED.toString());
					recentMsg.setSentTime(msg.getSentTime());
					recentMsg.setMessageId(msg.getInvitationId());

					updateRecentMessage(recentMsg);

					SelfMgr.getInstance().refreshFellows();
				}

				try {
					VehicleClient client = new VehicleClient(SelfMgr.getInstance().getId());
					client.FollowshipInvAck(msg.getInvitationId());
				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (shouldNotifyBar() && InvitationVerdict.ACCEPTED.equals(msg.getVerdict())) {
					try {
						NotificationMgr notificationMgr = new NotificationMgr(context);
						notificationMgr.notifyNewInvitationVerdictMsg(msg);
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
