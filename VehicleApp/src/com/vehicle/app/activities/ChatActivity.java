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
import com.vehicle.app.bean.Message;
import com.vehicle.app.bean.MessageStatus;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.JsonUtil;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.service.bean.MessageOne2OneResponse;
import com.vehicle.service.bean.NewFileNotification;

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
	public static final String KEY_MESSAGE = "message";
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
	private List<Message> mDataArrays = new ArrayList<Message>();

	private String mFellowId;

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
			this.mFellowId = bundle.getString(KEY_FELLOWID);
		}

		TextView tvFellow = (TextView) this.findViewById(R.id.chat_tv_fellowalias);
		tvFellow.setText(this.mFellowId);

		System.out.println("iddddddddddddd:" + this.mFellowId);
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

		String fileName = path + File.separator + name;

		System.out.println("photoooooooooooo:" + fileName);

		try {
			fout = new FileOutputStream(fileName);
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

		File file = new File(fileName);

		if (file.isFile() && file.exists()) {

			AsyncTask<String, Void, Void> sendTask = new AsyncTask<String, Void, Void>() {

				@Override
				protected Void doInBackground(String... arg0) {
					// TODO Auto-generated method stub
					String file = arg0[0];

					VehicleClient vClient = new VehicleClient(SelfMgr.getInstance().getId());
					vClient.SendFile(mFellowId, file);
					return null;
				}
			};

			sendTask.execute(fileName);
		} else {

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
		filter.addAction(Constants.ACTION_MESSAGE_RECEIVED);
		filter.addAction(Constants.ACTION_MESSAGE_ACKOK);
		filter.addAction(Constants.ACTION_NEWFILE_RECEIVED);
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
		this.mDataArrays = dbMgr.queryMessage(SelfMgr.getInstance().getSelfDriver().getId(), this.mFellowId);
		dbMgr.close();

		mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		this.mMsgList.setAdapter(mAdapter);
	}

	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.chat_btn_back:
			back();
			break;
		case R.id.chat_btn_send:
			send();
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

	private void send() {

		String content = mEditTextContent.getText().toString();

		if (content.length() > 0) {

			Message entity = new Message();
			entity.setSource(SelfMgr.getInstance().getSelfDriver().getId());
			entity.setTarget(SelfMgr.getInstance().getSelfDriver().getId());
			entity.setContent(content);
			entity.setStatus(MessageStatus.SENT);

			AsyncTask<Message, Void, Void> sendAsync = new AsyncTask<Message, Void, Void>() {

				@Override
				protected Void doInBackground(Message... params) {
					// TODO Auto-generated method stub

					System.out.println("In Send Async Task.................");

					try {
						Message msg = params[0];

						VehicleClient client = new VehicleClient(Constants.SERVERURL, SelfMgr.getInstance().getId());

						MessageOne2OneResponse resp = client.SendMessage(msg.getTarget(), msg.getContent());

						if (null != resp && resp.isSucess()) {

							msg.setId(resp.getMsgId());
							msg.setSentDate(resp.getMsgSentTime());

							Intent msgIntent = new Intent(Constants.ACTION_MESSAGE_ACKOK);
							msgIntent.putExtra(ChatActivity.KEY_MESSAGE, JsonUtil.toJsonString(msg));
							ChatActivity.this.sendBroadcast(msgIntent);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					mEditTextContent.setText("");
				}

			};

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				sendAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, entity);
			} else {
				sendAsync.execute(entity);
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

			if (Constants.ACTION_MESSAGE_RECEIVED.equals(action)) {

				onNewMessageReceived(intent);
			} else if (Constants.ACTION_MESSAGE_ACKOK.equals(action)) {

				onMessageACKReceived(intent);
			} else if (Constants.ACTION_NEWFILE_RECEIVED.equals(action)) {

				onNewFileReceived(intent);
			}
		}

		private void onNewMessageReceived(Intent intent) {
			String message = intent.getStringExtra(KEY_MESSAGE);

			System.out.println("newwwwwwwwwww:" + message);

			Message msg = JsonUtil.fromJson(message, Message.class);

			try {
				DBManager dbMgr = new DBManager(ChatActivity.this.getApplicationContext());
				dbMgr.addMessage(msg);
				dbMgr.close();

				if (mFellowId.equals(msg.getSource())) {
					mAdapter.addMsg(msg);
					mAdapter.notifyDataSetChanged();
					mMsgList.setSelection(mMsgList.getCount() - 1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void onMessageACKReceived(Intent intent) {
			String message = intent.getStringExtra(KEY_MESSAGE);
			Message msg = JsonUtil.fromJson(message, Message.class);

			System.out.println("newwwwwwwwwwackkkkkk:" + message);

			if (!mFellowId.equals(msg.getTarget()))
				return;

			try {
				DBManager dbMgr = new DBManager(ChatActivity.this.getApplicationContext());
				dbMgr.addMessage(msg);
				dbMgr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				mAdapter.addMsg(msg);
				mAdapter.notifyDataSetChanged();
				mMsgList.setSelection(mMsgList.getCount() - 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void onNewFileReceived(Intent intent) {

			String message = intent.getStringExtra(KEY_MESSAGE);

			NewFileNotification notification = JsonUtil.fromJson(message, NewFileNotification.class);

			if (!mFellowId.equals(notification.getTarget()))
				return;

			AsyncTask<NewFileNotification, Void, Boolean> fetchTask = new AsyncTask<NewFileNotification, Void, Boolean>() {

				@Override
				protected Boolean doInBackground(NewFileNotification... params) {
					// TODO Auto-generated method stub

					NewFileNotification notification = params[0];

					VehicleClient vClient = new VehicleClient(SelfMgr.getInstance().getId());

					String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
							+ Environment.DIRECTORY_PICTURES + File.separator + "Received";

					File dir = new File(path);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String filePath = path + File.separator + notification.getFileName();

					System.out.println("fetch file to:" + filePath);

					vClient.FetchFile(notification.getToken(), filePath);

					return true;
				}
			};

			fetchTask.execute(notification);
		}
	}
}
