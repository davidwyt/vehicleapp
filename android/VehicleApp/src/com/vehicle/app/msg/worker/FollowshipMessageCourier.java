package com.vehicle.app.msg.worker;

import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.FollowshipMessage;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.RecentMessage;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.web.bean.AddFavVendorResult;
import com.vehicle.sdk.client.VehicleWebClient;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

public class FollowshipMessageCourier extends MessageBaseCourier {

	private Context context;

	public FollowshipMessageCourier(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public void dispatch(IMessageItem item) {
		// TODO Auto-generated method stub

		if (!(item instanceof FollowshipMessage)) {
			throw new IllegalArgumentException("item is not FollowshipMessage");
		}

		final FollowshipMessage msg = (FollowshipMessage) item;

		AsyncTask<Void, Void, AddFavVendorResult> asyncTask = new AsyncTask<Void, Void, AddFavVendorResult>() {

			@Override
			protected AddFavVendorResult doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				AddFavVendorResult result = null;
				try {
					VehicleWebClient client = new VehicleWebClient();
					result = client.AddFavVendor(msg.getSource(), msg.getTarget());

					if (null != result && result.isSuccess()) {
						SelfMgr.getInstance().refreshFellows();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return result;
			}

			@Override
			protected void onPostExecute(AddFavVendorResult result) {

				if (null != result && result.isSuccess()) {
					System.out.println("add favorite success");

					RecentMessage recentMsg = new RecentMessage();
					recentMsg.setSelfId(SelfMgr.getInstance().getId());
					recentMsg.setFellowId(msg.getTarget());
					recentMsg.setMessageType(msg.getMessageType());
					recentMsg.setContent("");
					recentMsg.setSentTime(msg.getSentTime());
					recentMsg.setMessageId("fellowship");
					updateRecentMessage(recentMsg);

					Intent intent = new Intent(Constants.ACTION_FOLLOWSHIP_SUCCESS);
					context.sendBroadcast(intent);
				} else {
					Intent intent = new Intent(Constants.ACTION_FOLLOWSHIP_FAILED);
					context.sendBroadcast(intent);
				}
			}
		};

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			asyncTask.execute();
		}

	}

}
