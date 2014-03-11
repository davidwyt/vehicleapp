package com.vehicle.app.msg.worker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.bean.FileMessage;
import com.vehicle.app.msg.bean.RecentMessage;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.StringUtil;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.service.bean.BaseResponse;
import com.vehicle.service.bean.FileMultiTransmissionResponse;
import com.vehicle.service.bean.FileTransmissionResponse;

public class FileMessageCourier extends MessageBaseCourier {

	private boolean isGroupChat = false;

	public FileMessageCourier(Context context, boolean isGroupChat) {
		super(context);
		this.isGroupChat = isGroupChat;
	}

	private void processMessage(FileMessage msg) {
		try {
			DBManager dbMgr = new DBManager(context.getApplicationContext());
			dbMgr.insertFileMessage(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			RecentMessage recentMsg = new RecentMessage();
			recentMsg.setSelfId(SelfMgr.getInstance().getId());
			recentMsg.setFellowId(msg.getTarget());
			recentMsg.setMessageType(msg.getMessageType());
			recentMsg.setContent("");
			recentMsg.setSentTime(msg.getSentTime());
			recentMsg.setMessageId(msg.getToken());

			updateRecentMessage(recentMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void dispatch(IMessageItem item) {
		// TODO Auto-generated method stub

		if (!(item instanceof FileMessage)) {
			throw new IllegalArgumentException();
		}

		final FileMessage picMessage = (FileMessage) item;

		AsyncTask<Void, Void, BaseResponse> sendTask = new AsyncTask<Void, Void, BaseResponse>() {

			@Override
			protected BaseResponse doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				BaseResponse resp = null;

				try {
					if (isGroupChat) {
						VehicleClient vClient = new VehicleClient(SelfMgr.getInstance().getId());
						resp = vClient.SendMultiFile(picMessage.getTarget(), picMessage.getPath(),
								picMessage.getMessageType());
					} else {
						VehicleClient vClient = new VehicleClient(SelfMgr.getInstance().getId());
						resp = vClient.SendFile(picMessage.getTarget(), picMessage.getPath(),
								picMessage.getMessageType());
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
						FileMultiTransmissionResponse fileMultiResp = (FileMultiTransmissionResponse) resp;
						// picMessage.setSentTime(fileMultiResp.getSentTime());
						picMessage.setToken(fileMultiResp.getToken());
						picMessage.setFlag(MessageFlag.SELF);

						if (!StringUtil.IsNullOrEmpty(picMessage.getTarget())) {
							String[] targets = picMessage.getTarget().split(Constants.COMMA);
							if (null != targets) {
								for (String tar : targets) {
									if (!StringUtil.IsNullOrEmpty(tar)) {
										FileMessage msg = new FileMessage();
										msg.setFlag(MessageFlag.SELF);
										msg.setMsgType(picMessage.getMsgType());
										msg.setPath(picMessage.getPath());
										msg.setSentTime(picMessage.getSentTime());
										msg.setSource(picMessage.getSource());
										msg.setTarget(tar);
										msg.setToken(picMessage.getToken());

										processMessage(msg);
									}
								}
							}
						}
					} else {
						FileTransmissionResponse fileResp = (FileTransmissionResponse) resp;
						// picMessage.setSentTime(fileResp.getSentTime());
						picMessage.setToken(fileResp.getToken());
						picMessage.setFlag(MessageFlag.SELF);

						processMessage(picMessage);

						Log.i("file sent", picMessage.getPath());
					}

					// Intent msgIntent = new
					// Intent(Constants.ACTION_FILEMSG_SENTOK);
					// msgIntent.putExtra(ChatActivity.KEY_MESSAGE, picMessage);
					// context.sendBroadcast(msgIntent);

				} else {
					if (picMessage.getMessageType() == IMessageItem.MESSAGE_TYPE_IMAGE) {
						Intent msgIntent = new Intent(Constants.ACTION_IMGMESSAGE_SENTFAILED);
						context.sendBroadcast(msgIntent);
					} else if (picMessage.getMessageType() == IMessageItem.MESSAGE_TYPE_AUDIO) {
						Intent msgIntent = new Intent(Constants.ACTION_AUDIOMESSAGE_SENTFAILED);
						context.sendBroadcast(msgIntent);
					}
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
