package com.vehicle.app.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.adapter.ChatMsgViewAdapter;
import com.vehicle.app.bean.IMessageItem;
import com.vehicle.app.bean.TextMessageItem;
import com.vehicle.app.bean.PictureMessageItem;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.utils.Constants;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.service.bean.FileTransmissionResponse;
import com.vehicle.service.bean.MessageOne2OneResponse;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ChatActivity extends Activity implements OnClickListener {

	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "com.vehicle.app.key.textmessage";
	public static final String KEY_EXTRAS = "extras";

	public static final String KEY_FELLOWID = "com.vehicle.app.activities.fellowId";

	private static final int REQUESTCODE_CAPTURE_IMAGE = 0x00000001;
	private static final int REQUESTCODE_BROWSE_ALBUM = 0x00000002;

	private Button mBtnSend;
	private Button mBtnBack;
	private Button mBtnSave;
	private Button mBtnPlus;

	private EditText mEditTextContent;
	private ChatMsgViewAdapter mAdapter;
	private ListView mMsgList;

	private BroadcastReceiver messageReceiver;
	private List<IMessageItem> mDataArrays = new ArrayList<IMessageItem>();

	private static String mFellowId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);

		initView();
		initData();
	}

	private void initView() {
		mMsgList = (ListView) findViewById(R.id.msglist);

		mBtnBack = (Button) findViewById(R.id.chat_btn_back);
		mBtnBack.setOnClickListener(this);

		mBtnSave = (Button) this.findViewById(R.id.chat_btn_save);
		mBtnSave.setOnClickListener(this);

		mBtnSend = (Button) findViewById(R.id.chat_btn_send);
		mBtnSend.setOnClickListener(this);

		mBtnPlus = (Button) findViewById(R.id.chat_btn_plus);
		mBtnPlus.setOnClickListener(this);

		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
		mEditTextContent.setCursorVisible(true);

		Bundle bundle = this.getIntent().getExtras();
		if (null != bundle) {
			mFellowId = bundle.getString(KEY_FELLOWID);
		}

		TextView tvFellow = (TextView) this.findViewById(R.id.chat_tv_fellowalias);
		tvFellow.setText(mFellowId);

		System.out.println("iddddddddddddd:" + mFellowId);
	}

	@Override
	protected void onStart() {
		super.onStart();
		registerMessageReceiver();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public static String getCurrentFellowId() {
		return mFellowId;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQUESTCODE_CAPTURE_IMAGE:
			onCaptureImage(resultCode, data);
			break;
		case REQUESTCODE_BROWSE_ALBUM:
			onBrowseAlbum(resultCode, data);
			break;
		default:
			System.out.println("invalid requestcode :" + requestCode + " in chat activity");
		}
	}

	private void onCaptureImage(int resultCode, Intent data) {

		if (Activity.RESULT_OK == resultCode) {
			String sdState = Environment.getExternalStorageState();
			if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
				System.out.println("sd card unmount");
				return;
			}
		}

		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
				+ Environment.DIRECTORY_DCIM + File.separator + "Camera";
		String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";

		Bundle bundle = data.getExtras();
		Bitmap bitmap = (Bitmap) bundle.get("data");
		FileOutputStream fout = null;

		String filePath = path + File.separator + name;

		System.out.println("photoooooooooooo:" + filePath);

		try {
			fout = new FileOutputStream(filePath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
		} catch (FileNotFoundException e) {
			e.printStackTrace();

			// do something
		} finally {
			try {
				if (null != fout) {
					fout.flush();
					fout.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		sendFile(filePath);
	}

	private void sendFile(String filePath) {
		File file = new File(filePath);

		if (file.isFile() && file.exists()) {

			final PictureMessageItem picItem = new PictureMessageItem();
			picItem.setSource(SelfMgr.getInstance().getId());
			picItem.setTarget(SelfMgr.getInstance().getId());
			picItem.setName(file.getName());
			picItem.setContent(BitmapFactory.decodeFile(filePath));

			AsyncTask<String, Void, FileTransmissionResponse> sendTask = new AsyncTask<String, Void, FileTransmissionResponse>() {

				@Override
				protected FileTransmissionResponse doInBackground(String... arg0) {
					// TODO Auto-generated method stub
					String file = arg0[0];

					VehicleClient vClient = new VehicleClient(SelfMgr.getInstance().getId());
					FileTransmissionResponse resp = vClient.SendFile(mFellowId, file);
					return resp;
				}

				@Override
				protected void onPostExecute(FileTransmissionResponse resp) {
					if (null != resp && resp.isSucess()) {

						picItem.setSentTime(resp.getSentTime());
						picItem.setToken(resp.getToken());

						Intent msgIntent = new Intent(Constants.ACTION_FILEMSG_SENTOK);
						msgIntent.putExtra(ChatActivity.KEY_MESSAGE, picItem);
						ChatActivity.this.sendBroadcast(msgIntent);
					}

				}
			};

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				sendTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, filePath);
			} else {
				sendTask.execute(filePath);
			}
		}
	}

	private void onBrowseAlbum(int resultCode, Intent data) {

		if (Activity.RESULT_OK == resultCode) {

		}
	}

	private void registerMessageReceiver() {
		// this.unregisterReceiver(messageReceiver);
		try {
			unregisterReceiver(messageReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}

		messageReceiver = new ChatMessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(Constants.ACTION_TEXTMESSAGE_RECEIVED);
		filter.addAction(Constants.ACTION_TEXTMESSAGE_SENTOK);
		filter.addAction(Constants.ACTION_FILEMSG_RECEIVED);
		filter.addAction(Constants.ACTION_FILEMSG_SENTOK);

		registerReceiver(messageReceiver, filter);
	}

	@Override
	protected void onStop() {
		super.onStop();
		try {
			unregisterReceiver(messageReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initData() {

		DBManager dbMgr = new DBManager(this);

		List<TextMessageItem> msgList = dbMgr.queryAllTextMessage(SelfMgr.getInstance().getSelfDriver().getId(),
				mFellowId);

		this.mDataArrays.addAll(msgList);

		mAdapter = new ChatMsgViewAdapter(this.getApplicationContext(), mDataArrays);
		this.mMsgList.setAdapter(mAdapter);
	}

	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.chat_btn_back:
			back();
			break;
		case R.id.chat_btn_send:
			sendTextMsg();
			break;
		case R.id.chat_btn_save:
			save();
			break;
		case R.id.chat_btn_plus:
			plus();
			break;
		}
	}

	private void plus() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		this.startActivityForResult(intent, REQUESTCODE_CAPTURE_IMAGE);
	}

	private void save() {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), MsgMgrActivity.class);
		this.startActivity(intent);
	}

	private void sendTextMsg() {

		String content = mEditTextContent.getText().toString();

		if (content.length() > 0) {

			final TextMessageItem entity = new TextMessageItem();
			entity.setSource(SelfMgr.getInstance().getSelfDriver().getId());
			entity.setTarget(SelfMgr.getInstance().getSelfDriver().getId());
			entity.setContent(content);

			AsyncTask<Void, Void, MessageOne2OneResponse> sendAsync = new AsyncTask<Void, Void, MessageOne2OneResponse>() {

				@Override
				protected MessageOne2OneResponse doInBackground(Void... params) {
					// TODO Auto-generated method stub

					System.out.println("In Send Async Task.................");

					try {

						VehicleClient client = new VehicleClient(Constants.SERVERURL, SelfMgr.getInstance().getId());

						MessageOne2OneResponse resp = client.SendMessage(entity.getTarget(), entity.getContent());

						return resp;
					} catch (Exception e) {
						e.printStackTrace();
					}

					return null;
				}

				@Override
				protected void onPostExecute(MessageOne2OneResponse resp) {
					if (null != resp && resp.isSucess()) {

						mEditTextContent.setText("");

						entity.setId(resp.getMsgId());
						entity.setSentTime(resp.getMsgSentTime());

						Intent msgIntent = new Intent(Constants.ACTION_TEXTMESSAGE_SENTOK);
						msgIntent.putExtra(ChatActivity.KEY_MESSAGE, entity);
						ChatActivity.this.sendBroadcast(msgIntent);
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		back();
		return true;
	}

	private void back() {
		finish();
	}

	class ChatMessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();

			if (Constants.ACTION_TEXTMESSAGE_RECEIVED.equals(action)) {

				onNewMessageReceived(intent);
			} else if (Constants.ACTION_TEXTMESSAGE_SENTOK.equals(action)) {

				onMessageSent(intent);
			} else if (Constants.ACTION_FILEMSG_RECEIVED.equals(action)) {

				onNewFileReceived(intent);
			} else if (Constants.ACTION_FILEMSG_SENTOK.equals(action)) {
				onNewFileSent(intent);
			}
		}

		private void onNewMessageReceived(Intent intent) {
			TextMessageItem msg = intent.getParcelableExtra(KEY_MESSAGE);

			if (!mFellowId.equals(msg.getSource()) || !msg.getTarget().equals(SelfMgr.getInstance().getId()))
				return;

			try {
				mAdapter.addChatItem(msg);
				mAdapter.notifyDataSetChanged();
				mMsgList.setSelection(mMsgList.getCount() - 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void onMessageSent(Intent intent) {
			TextMessageItem msg = intent.getParcelableExtra(KEY_MESSAGE);

			if (!mFellowId.equals(msg.getTarget()) || !msg.getSource().equals(SelfMgr.getInstance().getId()))
				return;

			try {
				DBManager dbMgr = new DBManager(ChatActivity.this.getApplicationContext());
				dbMgr.insertTextMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				mAdapter.addChatItem(msg);
				mAdapter.notifyDataSetChanged();
				mMsgList.setSelection(mMsgList.getCount() - 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void onNewFileSent(Intent intent) {
			PictureMessageItem msg = intent.getParcelableExtra(KEY_MESSAGE);

			if (!mFellowId.equals(msg.getTarget()) || !msg.getSource().equals(SelfMgr.getInstance().getId()))
				return;

			try {
				DBManager dbMgr = new DBManager(ChatActivity.this.getApplicationContext());
				dbMgr.insertFileMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				mAdapter.addChatItem(msg);
				mAdapter.notifyDataSetChanged();
				mMsgList.setSelection(mMsgList.getCount() - 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void onNewFileReceived(Intent intent) {

			PictureMessageItem msg = intent.getParcelableExtra(KEY_MESSAGE);

			if (!mFellowId.equals(msg.getSource()) || !msg.getTarget().equals(SelfMgr.getInstance().getId()))
				return;

			try {
				mAdapter.addChatItem(msg);
				mAdapter.notifyDataSetChanged();
				mMsgList.setSelection(mMsgList.getCount() - 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
