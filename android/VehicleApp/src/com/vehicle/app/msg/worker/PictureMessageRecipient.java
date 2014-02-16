package com.vehicle.app.msg.worker;

import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import com.vehicle.app.activities.ChatActivity;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.NotificationMgr;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.bean.PictureMessage;
import com.vehicle.app.utils.ActivityUtil;
import com.vehicle.app.utils.Constants;
import com.vehicle.sdk.client.VehicleClient;

public class PictureMessageRecipient extends MessageBaseRecipient {

	public PictureMessageRecipient(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void receive(IMessageItem msg) {
		// TODO Auto-generated method stub
		if (!(msg instanceof PictureMessage)) {
			throw new IllegalArgumentException("msg is not PictureMessageItem");
		}

		final PictureMessage picMsgItem = (PictureMessage) msg;

		AsyncTask<Void, Void, Boolean> fetchTask = new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub

				VehicleClient vClient = new VehicleClient(SelfMgr.getInstance().getId());

				String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
						+ Environment.DIRECTORY_PICTURES + File.separator + "Received";

				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				String filePath = path + File.separator + picMsgItem.getName();

				System.out.println("fetch file to:" + filePath);

				InputStream fileStream = vClient.FetchFile(picMsgItem.getToken(), filePath);
				if (null == fileStream) {
					System.out.println("fetch file failed:" + picMsgItem.getToken());
					return false;
				}

				File file = new File(filePath);
				System.out.println("file: " + filePath + " exist:" + file.exists());

				picMsgItem.setContent(BitmapFactory.decodeFile(filePath));
				picMsgItem.setPath(filePath);
				
				System.out.println("content null :" + (null == picMsgItem.getContent()));
				return true;
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

					NotificationMgr notificationMgr = new NotificationMgr(context);
					notificationMgr.notifyNewFileMsg(picMsgItem);
				}
			}
		};

		fetchTask.execute();
	}
	
	@Override
	protected boolean shouldNotifyBar() {
		return true;
	}
}
