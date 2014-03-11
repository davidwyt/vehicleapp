package com.vehicle.app.msg.worker;

import java.util.Calendar;
import java.util.List;

import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.FileMessage;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.bean.TextMessage;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.service.bean.OfflineMessageResponse;
import com.vehicle.service.bean.RespMessage;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

public class OfflineMessageCourier extends MessageBaseCourier {

	public OfflineMessageCourier(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private void processMessage(RespMessage msg) {
		if (msg.getMessageType() == IMessageItem.MESSAGE_TYPE_TEXT
				|| msg.getMessageType() == IMessageItem.MESSAGE_TYPE_LOCATION) {
			TextMessage textMsg = new TextMessage();
			textMsg.setId(msg.getId());
			textMsg.setContent(msg.getContent());
			textMsg.setFlag(MessageFlag.UNREAD);
			textMsg.setMessageType(msg.getMessageType());
			textMsg.setSentTime(msg.getSentTime());
			textMsg.setSource(msg.getSource());
			textMsg.setTarget(msg.getTarget());

			IMessageRecipient recipient = new TextMessageRecipient(context, true);
			recipient.receive(textMsg);

		} else if (msg.getMessageType() == IMessageItem.MESSAGE_TYPE_IMAGE
				|| msg.getMessageType() == IMessageItem.MESSAGE_TYPE_AUDIO) {
			FileMessage imgMsg = new FileMessage();
			imgMsg.setFlag(MessageFlag.UNREAD);
			imgMsg.setToken(msg.getContent());
			imgMsg.setSentTime(msg.getSentTime());
			imgMsg.setSource(msg.getSource());
			imgMsg.setTarget(msg.getTarget());
			/**
			 * if (msg.getMessageType() == IMessageItem.MESSAGE_TYPE_IMAGE)
			 * imgMsg.setName(UUID.randomUUID().toString() + ".png"); else
			 * imgMsg.setName(UUID.randomUUID().toString() + ".wav");
			 */
			IMessageRecipient recipient = new FileMessageRecipient(context, true);
			recipient.receive(imgMsg);
		}
	}

	@Override
	public void dispatch(IMessageItem item) {
		// TODO Auto-generated method stub
		AsyncTask<Void, Void, OfflineMessageResponse> asyncTask = new AsyncTask<Void, Void, OfflineMessageResponse>() {

			@Override
			protected OfflineMessageResponse doInBackground(Void... params) {
				// TODO Auto-generated method stub

				OfflineMessageResponse resp = null;
				try {
					String id = SelfMgr.getInstance().getId();

					VehicleClient client = new VehicleClient(id);
					Calendar rightNow = Calendar.getInstance();
					rightNow.add(Calendar.DAY_OF_MONTH, -10);
					resp = client.GetOfflineMessage(id, 0);
					client.OfflineAck(SelfMgr.getInstance().getId());
				} catch (Exception e) {
					e.printStackTrace();
				}

				return resp;
			}

			@Override
			protected void onPostExecute(OfflineMessageResponse result) {
				if (null == result || !result.isSucess()) {
					System.out.println("login to imserver failed");
					return;
				}

				List<RespMessage> messages = result.getMessages();

				if (null != messages) {
					for (RespMessage msg : messages) {
						processMessage(msg);
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
}
