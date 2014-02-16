package com.vehicle.app.msg.worker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import com.vehicle.app.activities.ChatActivity;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.PictureMessage;
import com.vehicle.app.utils.Constants;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.service.bean.FileTransmissionResponse;

public class PictureMessageCourier extends MessageBaseCourier {

	public PictureMessageCourier(Context context) {
		super(context);
	}

	@Override
	public void dispatch(IMessageItem item) {
		// TODO Auto-generated method stub

		if (!(item instanceof PictureMessage)) {
			throw new IllegalArgumentException();
		}

		final PictureMessage picMessage = (PictureMessage) item;

		AsyncTask<Void, Void, FileTransmissionResponse> sendTask = new AsyncTask<Void, Void, FileTransmissionResponse>() {

			@Override
			protected FileTransmissionResponse doInBackground(Void... arg0) {
				// TODO Auto-generated method stub

				VehicleClient vClient = new VehicleClient(SelfMgr.getInstance().getId());
				FileTransmissionResponse resp = vClient.SendFile(picMessage.getTarget(), picMessage.getPath());
				return resp;
			}

			@Override
			protected void onPostExecute(FileTransmissionResponse resp) {
				if (null != resp && resp.isSucess()) {

					picMessage.setSentTime(resp.getSentTime());
					picMessage.setToken(resp.getToken());

					try {
						DBManager dbMgr = new DBManager(context.getApplicationContext());
						dbMgr.insertFileMessage(picMessage);
					} catch (Exception e) {
						e.printStackTrace();
					}

					Intent msgIntent = new Intent(Constants.ACTION_FILEMSG_SENTOK);
					msgIntent.putExtra(ChatActivity.KEY_MESSAGE, picMessage);
					context.sendBroadcast(msgIntent);
				}

			}
		};

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			sendTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			sendTask.execute();
		}
	}

}
