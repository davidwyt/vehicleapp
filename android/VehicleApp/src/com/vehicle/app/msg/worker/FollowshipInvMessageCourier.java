package com.vehicle.app.msg.worker;

import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.utils.JsonUtil;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.service.bean.FollowshipInvitationResponse;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

public class FollowshipInvMessageCourier extends MessageBaseCourier {

	public FollowshipInvMessageCourier(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void dispatch(IMessageItem item) {
		// TODO Auto-generated method stub
		if (!(item instanceof FollowshipInvitationMessage)) {
			throw new IllegalArgumentException("item not FollowshipInvitationMessage");
		}

		final FollowshipInvitationMessage msg = (FollowshipInvitationMessage) item;

		AsyncTask<Void, Void, FollowshipInvitationResponse> asyncTask = new AsyncTask<Void, Void, FollowshipInvitationResponse>() {

			@Override
			protected FollowshipInvitationResponse doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				FollowshipInvitationResponse resp = null;

				VehicleClient vClient = new VehicleClient(SelfMgr.getInstance().getId());
				try {
					resp = vClient.InviteFollowship(msg.getTarget());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return resp;
			}

			@Override
			protected void onPostExecute(FollowshipInvitationResponse result) {
				if (null != result && result.isSucess()) {
					System.out.println("followship invitation sent successful:" + JsonUtil.toJsonString(msg));

					try {
						DBManager dbManager = new DBManager(context);
						msg.setFlag(MessageFlag.SELF);
						msg.setId(result.getInvitationId());
						msg.setSentTime(result.getReqTime());
						dbManager.insertFollowshipInvMessage(msg);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					System.out.println(String.format("followship invitation %s send failed with:%s",
							JsonUtil.toJsonString(msg), null == result ? "" : JsonUtil.toJsonString(result)));
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
