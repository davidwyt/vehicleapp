package com.vehicle.app.msg.worker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import com.vehicle.app.activities.ChatActivity;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.bean.RecentMessage;
import com.vehicle.app.msg.bean.TextMessage;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.StringUtil;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.service.bean.BaseResponse;
import com.vehicle.service.bean.MessageOne2MultiResponse;
import com.vehicle.service.bean.MessageOne2OneResponse;

public class TextMessageCourier extends MessageBaseCourier {

	private boolean isGroupChat = false;
	private boolean broadResult = false;

	public TextMessageCourier(Context context, boolean isGroupChat, boolean broadResult) {
		super(context);
		this.isGroupChat = isGroupChat;
		this.broadResult = broadResult;
	}

	@Override
	public void dispatch(IMessageItem item) {
		// TODO Auto-generated method stub

		if (!(item instanceof TextMessage)) {
			throw new IllegalArgumentException("item is not valid TextMessageItem");
		}

		final TextMessage msg = (TextMessage) item;

		AsyncTask<Void, Void, BaseResponse> sendAsync = new AsyncTask<Void, Void, BaseResponse>() {

			private void processMessage(TextMessage msg) {
				try {
					DBManager dbMgr = new DBManager(context);
					dbMgr.insertTextMessage(msg);

				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					RecentMessage recentMsg = new RecentMessage();
					recentMsg.setSelfId(SelfMgr.getInstance().getId());
					recentMsg.setFellowId(msg.getTarget());
					recentMsg.setMessageType(msg.getMessageType());
					recentMsg.setContent(msg.getContent());
					recentMsg.setSentTime(msg.getSentTime());
					recentMsg.setMessageId(msg.getId());

					updateRecentMessage(recentMsg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			protected BaseResponse doInBackground(Void... params) {
				// TODO Auto-generated method stub

				System.out.println("In Send Async Task.................");
				BaseResponse resp = null;
				try {

					VehicleClient client = new VehicleClient(SelfMgr.getInstance().getId());

					if (IMessageItem.MESSAGE_TYPE_TEXT == msg.getMessageType()) {
						if (isGroupChat) {
							resp = client.SendMultiTextMessage(msg.getTarget(), msg.getContent());
						} else {
							resp = client.SendTextMessage(msg.getTarget(), msg.getContent());
						}
					} else if (IMessageItem.MESSAGE_TYPE_LOCATION == msg.getMessageType()) {
						if (isGroupChat) {
							resp = client.SendMultiLocationMessage(msg.getTarget(), msg.getContent());
						} else {
							resp = client.SendLocationMessage(msg.getTarget(), msg.getContent());
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return resp;
			}

			@Override
			protected void onPostExecute(BaseResponse resp) {
				if (null != resp && resp.isSucess()) {

					if (isGroupChat) {
						MessageOne2MultiResponse multiResp = (MessageOne2MultiResponse) resp;
						msg.setId(multiResp.getMsgId());

						String targets = msg.getTarget();
						if (!StringUtil.IsNullOrEmpty(targets)) {
							String[] targetArray = msg.getTarget().split(Constants.COMMA);
							if (null != targetArray) {
								for (String tar : targetArray) {
									if (!StringUtil.IsNullOrEmpty(tar)) {
										TextMessage textMsg = new TextMessage();
										textMsg.setContent(msg.getContent());
										textMsg.setId("groupmsg");
										textMsg.setSource(msg.getSource());
										textMsg.setTarget(tar);
										textMsg.setFlag(MessageFlag.SELF);
										textMsg.setSentTime(msg.getSentTime());
										textMsg.setMessageType(msg.getMessageType());

										this.processMessage(textMsg);
									}
								}
							}
						}

						// msg.setSentTime(multiResp.getMsgSentTime());
					} else {

						MessageOne2OneResponse oneResp = (MessageOne2OneResponse) resp;
						msg.setId(oneResp.getMsgId());
						// msg.setSentTime(oneResp.getMsgSentTime());
						msg.setFlag(MessageFlag.SELF);

						processMessage(msg);

						System.out.println("send msg success:" + oneResp.getMsgId());
					}

					if (broadResult) {
						Intent msgIntent = new Intent(Constants.ACTION_TEXTMESSAGE_SENTOK);
						msgIntent.putExtra(ChatActivity.KEY_MESSAGE, msg);
						context.sendBroadcast(msgIntent);
					}
				} else {
					if (msg.getMessageType() == IMessageItem.MESSAGE_TYPE_LOCATION) {
						Intent msgIntent = new Intent(Constants.ACTION_LOCMESSAGE_SENTFAILED);
						context.sendBroadcast(msgIntent);
					} else {
						Intent msgIntent = new Intent(Constants.ACTION_TEXTMESSAGE_SENTFAILED);
						context.sendBroadcast(msgIntent);
					}
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
