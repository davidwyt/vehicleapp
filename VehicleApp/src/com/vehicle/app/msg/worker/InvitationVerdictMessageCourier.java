package com.vehicle.app.msg.worker;

import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.InvitationVerdict;
import com.vehicle.app.msg.bean.InvitationVerdictMessage;
import com.vehicle.app.utils.JsonUtil;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.service.bean.FollowshipInvitationResultResponse;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

public class InvitationVerdictMessageCourier extends MessageBaseCourier {

	public InvitationVerdictMessageCourier(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void dispatch(IMessageItem item) {
		// TODO Auto-generated method stub
		if (!(item instanceof InvitationVerdictMessage)) {
			throw new IllegalArgumentException("item is not IvitationVerdictMessage");
		}

		final InvitationVerdictMessage msg = (InvitationVerdictMessage) item;

		AsyncTask<Void, Void, FollowshipInvitationResultResponse> asyncTask = new AsyncTask<Void, Void, FollowshipInvitationResultResponse>() {

			@Override
			protected FollowshipInvitationResultResponse doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				FollowshipInvitationResultResponse resp = null;

				VehicleClient vClient = new VehicleClient(SelfMgr.getInstance().getId());
				try {
					resp = vClient.FollowshipInvitationVerdict(msg.getInvitationId(),
							InvitationVerdict.ACCEPTED.equals(msg.getVerdict()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return resp;
			}

			@Override
			protected void onPostExecute(FollowshipInvitationResultResponse result) {
				if (null != result && result.isSucess()) {
					System.out.println("invitation verdict sent successful:" + JsonUtil.toJsonString(msg));
					
					try {
						DBManager dbManager = new DBManager(context);
						dbManager.insertInvitationVerdictMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}					
				} else {
					System.out.println(String.format("invitation verdict %s send failed with:%s",
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
