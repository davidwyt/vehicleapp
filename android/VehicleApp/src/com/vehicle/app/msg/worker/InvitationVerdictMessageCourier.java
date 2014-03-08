package com.vehicle.app.msg.worker;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.activities.FollowshipInvitationActivity;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.InvitationVerdict;
import com.vehicle.app.msg.bean.InvitationVerdictMessage;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.bean.RecentMessage;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.JsonUtil;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.sdk.client.VehicleWebClient;
import com.vehicle.service.bean.FollowshipInvitationResultResponse;

import android.content.Context;
import android.content.Intent;
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

				if (!SelfMgr.getInstance().isDriver())
					return null;

				FollowshipInvitationResultResponse resp = null;
				try {
					VehicleWebClient webClient = new VehicleWebClient();
					webClient.AddFavVendor(SelfMgr.getInstance().getId(), msg.getTarget());

					VehicleClient vClient = new VehicleClient(SelfMgr.getInstance().getId());

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
						msg.setFlag(MessageFlag.SELF);

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
						recentMsg.setContent(InvitationVerdict.ACCEPTED.toString());
						recentMsg.setSentTime(msg.getSentTime());
						recentMsg.setMessageId(msg.getInvitationId());

						updateRecentMessage(recentMsg);

						Intent intent = new Intent(Constants.ACTION_INVVERDICT_ACCEPTSUCCESS);
						context.sendBroadcast(intent);
					} else {
						Intent intent = new Intent(Constants.ACTION_INVVERDICT_REJECTSUCCESS);
						context.sendBroadcast(intent);
					}

				} else {

					if (InvitationVerdict.ACCEPTED.equals(msg.getVerdict())) {
						Intent intent = new Intent(Constants.ACTION_INVVERDICT_ACCEPTFAILED);
						if (null != result && result.getErrorCode() == 0x00000017) {
							String error = context.getResources().getString(R.string.tip_invprocesserro);
							intent.putExtra(FollowshipInvitationActivity.KEY_INVVERDICT_ERRORMSG, error);
						} else {
							String error = context.getResources().getString(R.string.tip_invverdictsentfailed);
							intent.putExtra(FollowshipInvitationActivity.KEY_INVVERDICT_ERRORMSG, error);
						}
						context.sendBroadcast(intent);
					} else {
						Intent intent = new Intent(Constants.ACTION_INVVERDICT_REJECTFAILED);
						if (null != result && result.getErrorCode() == 0x00000017) {
							String error = context.getResources().getString(R.string.tip_invprocesserro);
							intent.putExtra(FollowshipInvitationActivity.KEY_INVVERDICT_ERRORMSG, error);
						} else {
							String error = context.getResources().getString(R.string.tip_invverdictsentfailed);
							intent.putExtra(FollowshipInvitationActivity.KEY_INVVERDICT_ERRORMSG, error);
						}
						context.sendBroadcast(intent);
					}

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
