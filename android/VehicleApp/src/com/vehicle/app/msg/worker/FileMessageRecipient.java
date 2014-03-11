package com.vehicle.app.msg.worker;

import java.io.File;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.vehicle.app.activities.ChatActivity;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.NotificationMgr;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.bean.FileMessage;
import com.vehicle.app.msg.bean.RecentMessage;
import com.vehicle.app.utils.ActivityUtil;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.FileUtil;
import com.vehicle.app.utils.StringUtil;
import com.vehicle.sdk.client.VehicleClient;

public class FileMessageRecipient extends MessageBaseRecipient {

	public FileMessageRecipient(Context context, boolean wakeup) {
		super(context, wakeup);
	}

	@Override
	public void receive(IMessageItem msg) {
		// TODO Auto-generated method stub
		if (!(msg instanceof FileMessage)) {
			throw new IllegalArgumentException("msg is not PictureMessageItem");
		}

		final FileMessage picMsgItem = (FileMessage) msg;

		AsyncTask<Void, Void, Boolean> fetchTask = new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub

				if (!isFromWakeup) {
					picMsgItem.setSentTime(new Date().getTime());
				}

				try {
					VehicleClient vClient = new VehicleClient(SelfMgr.getInstance().getId());

					String destPath = "";

					String postFix = FileUtil.getPostFix(picMsgItem.getPath());
					if (picMsgItem.getMessageType() == IMessageItem.MESSAGE_TYPE_IMAGE) {
						if (StringUtil.IsNullOrEmpty(postFix)) {
							postFix = Constants.POSTFIX_DEFAULT_IMAGE;
						}

						destPath = FileUtil.genPathForImage(context, postFix);
					} else if (picMsgItem.getMessageType() == IMessageItem.MESSAGE_TYPE_AUDIO) {
						if (StringUtil.IsNullOrEmpty(postFix)) {
							postFix = Constants.POSTFIX_DEFAULT_AUDIO;
						}

						destPath = FileUtil.genPathForAudio(context, postFix);
					}

					System.out.println("fetch file to:" + destPath);
					System.out.println("file token :" + picMsgItem.getToken());

					vClient.FetchFile(picMsgItem.getToken(), destPath);

					SelfMgr.getInstance().retrieveInfo(picMsgItem.getSource());

					File file = new File(destPath);
					System.out.println("file: " + destPath + " exist:" + file.exists());

					if (file.exists()) {
						Log.i("file received", destPath);
						picMsgItem.setPath(destPath);
					} else {
						Log.i("file received failed", destPath);
						System.out.println("fetch file to path failed:" + destPath);
						return false;
					}

					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}

				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (!result) {
					System.out.println(String.format("fetch file:%s from server failed", picMsgItem.getToken()));
					return;
				}

				if (ActivityUtil.IsChattingWithFellow(context, picMsgItem.getSource())) {
					picMsgItem.setFlag(MessageFlag.READ);
					System.out.println("Is chatting with fellow");
					try {
						DBManager dbManager = new DBManager(context);
						dbManager.insertFileMessage(picMsgItem);
					} catch (Exception e) {
						e.printStackTrace();
					}

					Intent msgIntent = new Intent(Constants.ACTION_FILEMSG_RECEIVED);
					msgIntent.putExtra(ChatActivity.KEY_MESSAGE, picMsgItem);
					context.sendBroadcast(msgIntent);
				} else {
					System.out.println("Is Not chatting with fellow");

					picMsgItem.setFlag(MessageFlag.UNREAD);

					try {
						DBManager dbManager = new DBManager(context);
						dbManager.insertFileMessage(picMsgItem);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {

						NotificationMgr notificationMgr = new NotificationMgr(context);
						if (picMsgItem.getMessageType() == IMessageItem.MESSAGE_TYPE_IMAGE) {
							notificationMgr.notifyNewImgMsg(picMsgItem);
						} else if (picMsgItem.getMessageType() == IMessageItem.MESSAGE_TYPE_AUDIO) {
							notificationMgr.notifyNewAudioMsg(picMsgItem);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				try {
					RecentMessage recentMsg = new RecentMessage();
					recentMsg.setSelfId(SelfMgr.getInstance().getId());
					recentMsg.setFellowId(picMsgItem.getSource());
					recentMsg.setMessageType(picMsgItem.getMessageType());
					recentMsg.setContent(picMsgItem.getPath());
					recentMsg.setSentTime(picMsgItem.getSentTime());
					recentMsg.setMessageId(picMsgItem.getToken());

					updateRecentMessage(recentMsg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			fetchTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			fetchTask.execute();
		}
	}

	@Override
	protected boolean shouldNotifyBar() {
		return true;
	}
}
