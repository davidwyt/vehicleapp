package com.vehicle.app.msgprocessors;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import com.vehicle.app.activities.ChatActivity;
import com.vehicle.app.bean.IMessageItem;
import com.vehicle.app.bean.MessageFlag;
import com.vehicle.app.bean.PictureMessageItem;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.NotificationMgr;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.utils.Constants;
import com.vehicle.sdk.client.VehicleClient;

public class PictureMessageProcessor extends MessageBaseProcessor{

	public PictureMessageProcessor(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void process(IMessageItem msg) {
		// TODO Auto-generated method stub
		if (!(msg instanceof PictureMessageItem)) {
			throw new IllegalArgumentException("msg is not PictureMessageItem");
		}

		final PictureMessageItem picMsgItem = (PictureMessageItem) msg;
		
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

				vClient.FetchFile(picMsgItem.getToken(), filePath);
				
				picMsgItem.setContent(BitmapFactory.decodeFile(filePath));
				
				return true;
			}
			
			@Override
			protected void onPostExecute(Boolean result)
			{
				if(!result)
				{
					System.out.println(String.format("fetch file:%s from server failed", picMsgItem.getToken()));
					return;
				}
				
				if(IsChattingWithFellow(picMsgItem.getSource()))
				{
					picMsgItem.setFlag(MessageFlag.READ);
					DBManager dbManager = new DBManager(context);
					dbManager.insertFileMessage(picMsgItem);
					
					Intent msgIntent = new Intent(Constants.ACTION_FILEMSG_RECEIVED);
					msgIntent.putExtra(ChatActivity.KEY_MESSAGE, picMsgItem);
					context.sendBroadcast(msgIntent);
				}else
				{
					picMsgItem.setFlag(MessageFlag.UNREAD);
					DBManager dbManager = new DBManager(context);
					dbManager.insertFileMessage(picMsgItem);
					
					NotificationMgr notificationMgr = new NotificationMgr(context);
					notificationMgr.notifyNewFileMsg(picMsgItem);
				}
			}
		};

		fetchTask.execute();
	}
}
