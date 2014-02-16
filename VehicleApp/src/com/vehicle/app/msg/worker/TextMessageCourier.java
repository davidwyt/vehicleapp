package com.vehicle.app.msg.worker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import com.vehicle.app.activities.ChatActivity;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.TextMessage;
import com.vehicle.app.utils.Constants;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.service.bean.MessageOne2OneResponse;

public class TextMessageCourier extends MessageBaseCourier {

	public TextMessageCourier(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void dispatch(IMessageItem item) {
		// TODO Auto-generated method stub

		if (!(item instanceof TextMessage)) {
			throw new IllegalArgumentException("item is not valid TextMessageItem");
		}

		final TextMessage msg = (TextMessage) item;

		AsyncTask<Void, Void, MessageOne2OneResponse> sendAsync = new AsyncTask<Void, Void, MessageOne2OneResponse>() {

			@Override
			protected MessageOne2OneResponse doInBackground(Void... params) {
				// TODO Auto-generated method stub

				System.out.println("In Send Async Task.................");
				MessageOne2OneResponse resp = null;
				try {

					VehicleClient client = new VehicleClient(SelfMgr.getInstance().getId());

					if (IMessageItem.MESSAGE_TYPE_TEXT == msg.getMsgType()) {
						resp = client.SendTextMessage(msg.getTarget(), msg.getContent());
					} else if (IMessageItem.MESSAGE_TYPE_LOCATION == msg.getMsgType()) {
						resp = client.SendLocationMessage(msg.getTarget(), msg.getContent());
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return resp;
			}

			@Override
			protected void onPostExecute(MessageOne2OneResponse resp) {
				if (null != resp && resp.isSucess()) {

					msg.setId(resp.getMsgId());
					msg.setSentTime(resp.getMsgSentTime());

					try {
						DBManager dbMgr = new DBManager(context);
						dbMgr.insertTextMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}

					Intent msgIntent = new Intent(Constants.ACTION_TEXTMESSAGE_SENTOK);
					msgIntent.putExtra(ChatActivity.KEY_MESSAGE, msg);
					context.sendBroadcast(msgIntent);
					
					System.out.println("send msg success:" + resp.getMsgId());
				}
			}

		};

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			sendAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			sendAsync.execute();
		}
	}

}
