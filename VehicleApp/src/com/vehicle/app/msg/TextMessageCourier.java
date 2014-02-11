package com.vehicle.app.msg;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import com.vehicle.app.activities.ChatActivity;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.utils.Constants;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.service.bean.MessageOne2OneResponse;

public class TextMessageCourier extends MessageBaseCourier{

	public TextMessageCourier(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void dispatch(IMessageItem item) {
		// TODO Auto-generated method stub
		
		if(!(item instanceof TextMessageItem))
		{
			throw new IllegalArgumentException("item is not valid TextMessageItem");
		}
		
		final TextMessageItem textMsg = (TextMessageItem)item;
		
		AsyncTask<Void, Void, MessageOne2OneResponse> sendAsync = new AsyncTask<Void, Void, MessageOne2OneResponse>() {

			@Override
			protected MessageOne2OneResponse doInBackground(Void... params) {
				// TODO Auto-generated method stub

				System.out.println("In Send Async Task.................");

				try {

					VehicleClient client = new VehicleClient(Constants.SERVERURL, SelfMgr.getInstance().getId());

					MessageOne2OneResponse resp = client.SendMessage(textMsg.getTarget(), textMsg.getContent());

					return resp;
				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(MessageOne2OneResponse resp) {
				if (null != resp && resp.isSucess()) {

					textMsg.setId(resp.getMsgId());
					textMsg.setSentTime(resp.getMsgSentTime());
					
					try {
						DBManager dbMgr = new DBManager(context);
						dbMgr.insertTextMessage(textMsg);
					} catch (Exception e) {
						e.printStackTrace();
					}

					Intent msgIntent = new Intent(Constants.ACTION_TEXTMESSAGE_SENTOK);
					msgIntent.putExtra(ChatActivity.KEY_MESSAGE, textMsg);
					context.sendBroadcast(msgIntent);
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
