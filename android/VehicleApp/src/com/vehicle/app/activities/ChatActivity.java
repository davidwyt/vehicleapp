package com.vehicle.app.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.adapter.ChatMsgViewAdapter;
import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.bean.FileMessage;
import com.vehicle.app.msg.bean.SimpleLocation;
import com.vehicle.app.msg.bean.TextMessage;
import com.vehicle.app.msg.worker.IMessageCourier;
import com.vehicle.app.msg.worker.FileMessageCourier;
import com.vehicle.app.msg.worker.TextMessageCourier;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.FileUtil;
import com.vehicle.app.utils.JsonUtil;
import com.vehicle.app.utils.StringUtil;

import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends TemplateActivity implements OnClickListener {

	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "com.vehicle.app.key.textmessage";
	public static final String KEY_EXTRAS = "extras";

	public static final String KEY_FELLOWID = "com.vehicle.app.chat.fellowId";
	public static final String KEY_CHATSTYLE = "com.vehicle.app.chat.key.style";

	private static final int REQUESTCODE_CAPTURE_IMAGE = 0x00000001;
	private static final int REQUESTCODE_BROWSE_ALBUM = 0x00000002;
	private static final int REQUESTCODE_CAPTURE_LOCATION = 0x00000003;

	public static final int CHAT_STYLE_2ONE = 0;
	public static final int CHAT_STYLE_2NEARBY = 1;
	public static final int CHAT_STYLE_2FELLOWS = 2;

	private Button mBtnSend;
	private Button mBtnBack;
	private Button mBtnSave;
	private Button mBtnPlus;
	private Button mBtnVoice;

	private ImageButton mBtnSpeak;

	private EditText mEditTextContent;
	private ChatMsgViewAdapter mAdapter;
	private ListView mMsgList;

	private View mTitleView;

	private BroadcastReceiver messageReceiver;
	private List<IMessageItem> mDataArrays = new ArrayList<IMessageItem>();

	private static String mFellowId = "18726";

	private int mChatStyle = 1;

	private PopupWindow chatPlusPopup;

	private final static int[] CHATPLUS_ICONS = { R.drawable.icon_chatplus_camera, R.drawable.icon_chatplus_gallery,
			R.drawable.icon_chatplus_location };

	private static String[] CHATPLUS_FUNS = new String[3];

	public final static String ACTIVITYNAME = "com.vehicle.app.activities.ChatActivity";

	private Driver mDriver;
	private Vendor mVendor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);

		CHATPLUS_FUNS[0] = this.getResources().getString(R.string.camera_zh);
		CHATPLUS_FUNS[1] = this.getResources().getString(R.string.gallery_zh);
		CHATPLUS_FUNS[2] = this.getResources().getString(R.string.location_zh);

		initView();
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

		mBtnVoice = (Button) findViewById(R.id.chat_btn_voice);
		mBtnVoice.setOnClickListener(this);

		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
		mEditTextContent.setCursorVisible(true);

		mTitleView = (View) this.findViewById(R.id.chat_title);

		chatPlusPopup();

		mAdapter = new ChatMsgViewAdapter(this.getApplicationContext(), mDataArrays);
		this.mMsgList.setAdapter(mAdapter);

		mBtnSpeak = (ImageButton) this.findViewById(R.id.chat_btn_speak);
		mBtnSpeak.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startRecording();
					break;
				case MotionEvent.ACTION_UP:
					mBtnSpeak.setVisibility(View.GONE);
					stopRecording();
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void chatPlusPopup() {

		View contentView = getLayoutInflater().inflate(R.layout.popwindow_chatplus, null);

		chatPlusPopup = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		chatPlusPopup.setFocusable(true);
		chatPlusPopup.setBackgroundDrawable(new BitmapDrawable());
		chatPlusPopup.setAnimationStyle(R.style.chatpluspopupwin_animation);

		GridView gridView = (GridView) contentView.findViewById(R.id.chatplus_gridView);

		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < CHATPLUS_ICONS.length; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("icon", CHATPLUS_ICONS[i]);
			item.put("fun", CHATPLUS_FUNS[i]);
			data.add(item);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.chatplusitem,
				new String[] { "icon", "fun" }, new int[] { R.id.chatplus_imageView, R.id.chatplus_textView });

		gridView.setAdapter(simpleAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (chatPlusPopup.isShowing()) {
					chatPlusPopup.dismiss();
				}

				switch (position) {
				case 0:
					captureImage();
					break;
				case 1:
					browseImage();
					break;
				case 2:
					locate();
					break;
				}

			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		registerMessageReceiver();
		initData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public static void setCurrentFellowId(String fellowId) {
		mFellowId = fellowId;
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
		case REQUESTCODE_CAPTURE_LOCATION:
			onLocate(resultCode, data);
			break;
		default:
			System.out.println("invalid requestcode :" + requestCode + " in chat activity");
		}
	}

	private void registerMessageReceiver() {

		messageReceiver = new ChatMessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(Constants.ACTION_TEXTMESSAGE_RECEIVED);
		filter.addAction(Constants.ACTION_TEXTMESSAGE_SENTOK);
		filter.addAction(Constants.ACTION_FILEMSG_RECEIVED);
		filter.addAction(Constants.ACTION_FILEMSG_SENTOK);
		filter.addAction(Constants.ACTION_TEXTMESSAGE_SENTFAILED);
		filter.addAction(Constants.ACTION_IMGMESSAGE_SENTFAILED);
		filter.addAction(Constants.ACTION_LOCMESSAGE_SENTFAILED);
		filter.addAction(Constants.ACTION_AUDIOMESSAGE_SENTFAILED);

		try {
			registerReceiver(messageReceiver, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void unregisterMessageReceiver() {
		try {
			unregisterReceiver(messageReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterMessageReceiver();
	}

	private void updateTitle() {
		TextView tvFellow = (TextView) this.findViewById(R.id.chat_tv_fellowalias);

		if (CHAT_STYLE_2ONE == mChatStyle) {
			this.mTitleView.setBackgroundResource(R.drawable.icon_bakground);

			if (SelfMgr.getInstance().isDriver()) {
				this.mVendor = SelfMgr.getInstance().getVendorInfo(mFellowId);
				tvFellow.setVisibility(View.VISIBLE);
				if (null != this.mVendor) {
					tvFellow.setText(this.mVendor.getName());
				}
			} else {
				this.mDriver = SelfMgr.getInstance().getDriverInfo(mFellowId);
				tvFellow.setVisibility(View.VISIBLE);
				if (null != this.mDriver) {
					tvFellow.setText(this.mDriver.getAlias());
				}
			}
		} else if (CHAT_STYLE_2NEARBY == mChatStyle) {
			tvFellow.setVisibility(View.INVISIBLE);

			if (SelfMgr.getInstance().isDriver()) {
				this.mTitleView.setBackgroundResource(R.drawable.icon_title_groupmsgnearbyvendors);
			} else {
				this.mTitleView.setBackgroundResource(R.drawable.icon_title_groupmsgnearbydrivers);
			}
		} else if (CHAT_STYLE_2FELLOWS == mChatStyle) {
			tvFellow.setVisibility(View.INVISIBLE);

			if (SelfMgr.getInstance().isDriver()) {
				this.mTitleView.setBackgroundResource(R.drawable.icon_title_groupmsgdriverfellows);
			} else {
				this.mTitleView.setBackgroundResource(R.drawable.icon_title_groupmsgvendorfellows);
			}
		} else {

		}
	}

	private void initData() {

		Bundle bundle = this.getIntent().getExtras();
		if (null != bundle) {
			mFellowId = bundle.getString(KEY_FELLOWID);
			mChatStyle = bundle.getInt(KEY_CHATSTYLE);
			updateTitle();
		}

		if (CHAT_STYLE_2ONE == this.mChatStyle) {
			try {
				DBManager dbMgr = new DBManager(this.getApplicationContext());
				List<IMessageItem> msgs = new ArrayList<IMessageItem>();

				try {
					List<TextMessage> textMsgs = dbMgr.queryAllTextMessage(SelfMgr.getInstance().getId(), mFellowId);
					msgs.addAll(textMsgs);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					List<FileMessage> fileList = dbMgr.queryAllFileMessage(SelfMgr.getInstance().getId(), mFellowId);
					msgs.addAll(fileList);
				} catch (Exception e) {
					e.printStackTrace();
				}

				sortMsg(msgs);

				this.mDataArrays.clear();
				this.mDataArrays.addAll(msgs);
				this.mAdapter.notifyDataSetChanged();
				mMsgList.setSelection(mMsgList.getCount() - 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void sortMsg(List<IMessageItem> msgs) {
		Collections.sort(msgs, new Comparator<IMessageItem>() {

			@Override
			public int compare(IMessageItem item1, IMessageItem item2) {
				// TODO Auto-generated method stub
				return Long.compare(item1.getSentTime(), item2.getSentTime());
			}
		});
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
		case R.id.chat_btn_voice:
			voice();
			break;
		}
	}

	private void voice() {
		if (this.mBtnSpeak.getVisibility() == View.VISIBLE) {
			this.mBtnSpeak.setVisibility(View.GONE);
		} else {
			this.mBtnSpeak.setVisibility(View.VISIBLE);
		}
	}

	private void plus() {
		chatPlusPopup.showAtLocation(this.mBtnPlus, Gravity.BOTTOM, 0, 0);
	}

	private void save() {
		Intent intent = new Intent();
		intent.putExtra(MsgMgrActivity.KEY_FELLOWID, mFellowId);
		intent.setClass(getApplicationContext(), MsgMgrActivity.class);
		this.startActivity(intent);
	}

	private String curImgPath = "";

	private void captureImage() {
		this.curImgPath = FileUtil.getTempImgPath();

		System.out.println("generated img path:" + this.curImgPath);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(curImgPath)));
		this.startActivityForResult(intent, REQUESTCODE_CAPTURE_IMAGE);
	}

	private void browseImage() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");

		startActivityForResult(intent, REQUESTCODE_BROWSE_ALBUM);
	}

	private void locate() {
		Intent intent = new Intent(this, LocationActivity.class);

		this.startActivityForResult(intent, REQUESTCODE_CAPTURE_LOCATION);
	}

	private void onCaptureImage(int resultCode, Intent data) {

		if (Activity.RESULT_OK != resultCode) {
			return;
		}

		if (Activity.RESULT_OK == resultCode) {
			String sdState = Environment.getExternalStorageState();
			if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
				System.out.println("sd card unmount");
				return;
			}
		}

		System.out.println("captured img path:" + this.curImgPath);

		File file = new File(this.curImgPath);
		try {
			if (file.isFile() && file.exists()) {
				String destPath = FileUtil.genPathForImage(getApplicationContext(), Constants.POSTFIX_DEFAULT_IMAGE);

				FileUtil.CopyFile(this.curImgPath, destPath);

				try {
					file.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}

				File destFile = new File(destPath);
				if (destFile.isFile() && destFile.exists()) {
					sendFile(destPath, IMessageItem.MESSAGE_TYPE_IMAGE);
				}
			} else {
				Toast.makeText(getApplicationContext(), "capture image failed", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.curImgPath = "";
	}

	private void onBrowseAlbum(int resultCode, Intent data) {

		if (Activity.RESULT_OK == resultCode) {

			Uri originalUri = data.getData();
			ContentResolver resolver = getContentResolver();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = resolver.query(originalUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String path = cursor.getString(column_index);

			File file = new File(path);
			try {
				if (file.isFile() && file.exists()) {
					String postFix = FileUtil.getPostFix(path);
					if (StringUtil.IsNullOrEmpty(postFix)) {
						postFix = Constants.POSTFIX_DEFAULT_IMAGE;
					}

					String newFile = FileUtil.genPathForImage(getApplicationContext(), postFix);
					FileUtil.CopyFile(path, newFile);
					sendFile(newFile, IMessageItem.MESSAGE_TYPE_IMAGE);
					System.out.println("selected file:" + path);
				} else {
					System.out.println("file not exist:" + path);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void onLocate(int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (Activity.RESULT_OK == resultCode) {
			SimpleLocation location = (SimpleLocation) data.getSerializableExtra(LocationActivity.KEY_LOCATION);
			sendLocationMsg(location);
		}
	}

	private void sendTextMsg() {

		String content = mEditTextContent.getText().toString();

		if (content.length() > 0) {

			mEditTextContent.setText("");

			TextMessage entity = new TextMessage();
			entity.setSource(SelfMgr.getInstance().getId());
			entity.setTarget(mFellowId);
			entity.setContent(content);
			entity.setFlag(MessageFlag.SELF);
			entity.setMessageType(IMessageItem.MESSAGE_TYPE_TEXT);
			entity.setSentTime(new Date().getTime());

			IMessageCourier msgCourier = new TextMessageCourier(this.getApplicationContext(),
					CHAT_STYLE_2ONE != this.mChatStyle, false);
			msgCourier.dispatch(entity);

			FakeSendTask task = new FakeSendTask(entity);
			task.execute();
		}
	}

	private void sendFile(String filePath, int type) {

		Assert.assertEquals(true, type == IMessageItem.MESSAGE_TYPE_AUDIO || type == IMessageItem.MESSAGE_TYPE_IMAGE);
		if (type != IMessageItem.MESSAGE_TYPE_AUDIO && type != IMessageItem.MESSAGE_TYPE_IMAGE)
			return;

		File file = new File(filePath);

		if (file.isFile() && file.exists()) {

			final FileMessage picItem = new FileMessage();
			picItem.setSource(SelfMgr.getInstance().getId());
			picItem.setTarget(mFellowId);
			picItem.setFlag(MessageFlag.SELF);
			picItem.setPath(filePath);
			picItem.setMsgType(type);
			System.out.println("send file " + filePath);
			picItem.setSentTime(new Date().getTime());

			IMessageCourier msgCourier = new FileMessageCourier(this.getApplicationContext(),
					CHAT_STYLE_2ONE != this.mChatStyle);
			msgCourier.dispatch(picItem);

			Log.i("file send path", filePath);

			FakeSendTask task = new FakeSendTask(picItem);
			task.execute();
		} else {
			Toast.makeText(getApplicationContext(), "file:" + filePath + " not exist", Toast.LENGTH_LONG).show();
		}
	}

	private class FakeSendTask extends AsyncTask<Void, Void, Void> {

		private IMessageItem msgItem;

		public FakeSendTask(IMessageItem msg) {
			this.msgItem = msg;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			addToMsgList(msgItem);
		}
	}

	private void sendLocationMsg(SimpleLocation simLocation) {

		String content = JsonUtil.toJsonString(simLocation);

		TextMessage entity = new TextMessage();
		entity.setSource(SelfMgr.getInstance().getId());
		entity.setTarget(mFellowId);
		entity.setContent(content);
		entity.setFlag(MessageFlag.SELF);
		entity.setMessageType(IMessageItem.MESSAGE_TYPE_LOCATION);
		entity.setSentTime(new Date().getTime());

		IMessageCourier msgCourier = new TextMessageCourier(this.getApplicationContext(),
				CHAT_STYLE_2ONE != this.mChatStyle, false);
		msgCourier.dispatch(entity);

		FakeSendTask task = new FakeSendTask(entity);
		task.execute();
	}

	private void back() {
		this.onBackPressed();
		this.finish();
	}

	private void addToMsgList(final IMessageItem msg) {

		try {
			mDataArrays.add(msg);
			mAdapter.notifyDataSetChanged();
			mMsgList.setSelection(mMsgList.getCount() - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			} else if (Constants.ACTION_TEXTMESSAGE_SENTFAILED.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_textmsgfailed),
						Toast.LENGTH_LONG).show();
			} else if (Constants.ACTION_IMGMESSAGE_SENTFAILED.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_picmsgfailed),
						Toast.LENGTH_LONG).show();
			} else if (Constants.ACTION_LOCMESSAGE_SENTFAILED.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_locmsgfailed),
						Toast.LENGTH_LONG).show();
			} else if (Constants.ACTION_AUDIOMESSAGE_SENTFAILED.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_audiomsgfailed),
						Toast.LENGTH_LONG).show();
			}
		}

		private void onNewMessageReceived(Intent intent) {
			TextMessage msg = intent.getParcelableExtra(KEY_MESSAGE);

			if (!mFellowId.equals(msg.getSource()) || !msg.getTarget().equals(SelfMgr.getInstance().getId()))
				return;

			System.out.println("receive new msg:" + msg.getMessageType());

			addToMsgList(msg);
		}

		private void onMessageSent(Intent intent) {
			TextMessage msg = intent.getParcelableExtra(KEY_MESSAGE);

			if (!mFellowId.equals(msg.getTarget()) || !msg.getSource().equals(SelfMgr.getInstance().getId()))
				return;

			// addToMsgList(msg);
		}

		private void onNewFileSent(Intent intent) {
			FileMessage msg = intent.getParcelableExtra(KEY_MESSAGE);

			if (!mFellowId.equals(msg.getTarget()) || !msg.getSource().equals(SelfMgr.getInstance().getId()))
				return;

			// addToMsgList(msg);
		}

		private void onNewFileReceived(Intent intent) {

			FileMessage msg = intent.getParcelableExtra(KEY_MESSAGE);

			System.out.println("file msg:" + JsonUtil.toJsonString(msg));

			if (!mFellowId.equals(msg.getSource()) || !msg.getTarget().equals(SelfMgr.getInstance().getId()))
				return;

			System.out.println("add to list");

			addToMsgList(msg);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private MediaRecorder mRecorder = null;
	private String audioFile = "";

	private void startRecording() {
		try {
			audioFile = FileUtil.genPathForAudio(getApplicationContext(), Constants.POSTFIX_DEFAULT_AUDIO);

			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);

			mRecorder.setOutputFile(audioFile);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			try {
				mRecorder.prepare();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			}

			mRecorder.start();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	private void stopRecording() {
		try {
			if (null != mRecorder) {
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;

				File file = new File(this.audioFile);
				if (file.isFile() && file.exists()) {
					sendFile(this.audioFile, IMessageItem.MESSAGE_TYPE_AUDIO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

}
