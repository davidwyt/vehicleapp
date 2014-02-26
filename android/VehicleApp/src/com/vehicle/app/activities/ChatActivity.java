package com.vehicle.app.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
import com.vehicle.app.utils.JsonUtil;

import android.net.Uri;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends Activity implements OnClickListener {

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
	public void onCreate(Bundle savedInstanceState) {
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

		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
		mEditTextContent.setCursorVisible(true);

		mTitleView = (View) this.findViewById(R.id.chat_title);

		chatPlusPopup();

		mAdapter = new ChatMsgViewAdapter(this.getApplicationContext(), mDataArrays);
		this.mMsgList.setAdapter(mAdapter);
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
		filter.addAction(Constants.ACTION_FILEMESSAGE_SENTFAILED);
		filter.addAction(Constants.ACTION_LOCMESSAGE_SENTFAILED);

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
				this.mVendor = SelfMgr.getInstance().getFavVendor(mFellowId);
				tvFellow.setVisibility(View.VISIBLE);
				if (null != this.mVendor) {
					tvFellow.setText(this.mVendor.getName());
				}
			} else {
				this.mDriver = SelfMgr.getInstance().getVendorFellow(mFellowId);
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

				List<TextMessage> textMsgs = dbMgr.queryAllTextMessage(SelfMgr.getInstance().getId(), mFellowId);
				List<FileMessage> fileList = dbMgr.queryAllFileMessage(SelfMgr.getInstance().getId(), mFellowId);

				List<IMessageItem> msgs = new ArrayList<IMessageItem>();
				msgs.addAll(fileList);
				msgs.addAll(textMsgs);

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

	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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

		sendFile(filePath, IMessageItem.MESSAGE_TYPE_IMAGE);
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
			if (file.isFile() && file.exists()) {
				sendFile(path, IMessageItem.MESSAGE_TYPE_IMAGE);
				System.out.println("selected file:" + path);
			} else {
				System.out.println("file not exist:" + path);
			}
		}
	}

	private void onLocate(int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "location received!", Toast.LENGTH_SHORT).show();

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
			IMessageCourier msgCourier = new TextMessageCourier(this.getApplicationContext(),
					CHAT_STYLE_2ONE != this.mChatStyle);
			msgCourier.dispatch(entity);
		}
	}

	private void sendFile(String filePath, int type) {

		Assert.assertEquals(true, type == IMessageItem.MESSAGE_TYPE_AUDIO || type == IMessageItem.MESSAGE_TYPE_IMAGE);
		File file = new File(filePath);

		if (file.isFile() && file.exists()) {

			FileMessage picItem = new FileMessage();
			picItem.setSource(SelfMgr.getInstance().getId());
			picItem.setTarget(mFellowId);
			picItem.setName(file.getName());
			picItem.setContent(BitmapFactory.decodeFile(filePath));
			picItem.setFlag(MessageFlag.SELF);
			picItem.setPath(filePath);
			picItem.setMsgType(type);
			System.out.println("send file " + filePath + " null:" + (null == picItem.getContent()));

			IMessageCourier msgCourier = new FileMessageCourier(this.getApplicationContext(),
					CHAT_STYLE_2ONE != this.mChatStyle);

			msgCourier.dispatch(picItem);

			addToMsgList(picItem);
		} else {
			System.out.println("selected file not exist:" + filePath);
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

		IMessageCourier msgCourier = new TextMessageCourier(this.getApplicationContext(),
				CHAT_STYLE_2ONE != this.mChatStyle);
		msgCourier.dispatch(entity);
	}

	private void back() {
		finish();
	}

	private void addToMsgList(IMessageItem msg) {
		try {
			// mAdapter.addChatItem(msg);
			this.mDataArrays.add(msg);
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
			} else if (Constants.ACTION_FILEMESSAGE_SENTFAILED.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_picmsgfailed),
						Toast.LENGTH_LONG).show();
			} else if (Constants.ACTION_LOCMESSAGE_SENTFAILED.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_locmsgfailed),
						Toast.LENGTH_LONG).show();
			}
		}

		private void onNewMessageReceived(Intent intent) {
			TextMessage msg = intent.getParcelableExtra(KEY_MESSAGE);

			if (!mFellowId.equals(msg.getSource()) || !msg.getTarget().equals(SelfMgr.getInstance().getId()))
				return;

			addToMsgList(msg);
		}

		private void onMessageSent(Intent intent) {
			TextMessage msg = intent.getParcelableExtra(KEY_MESSAGE);

			if (!mFellowId.equals(msg.getTarget()) || !msg.getSource().equals(SelfMgr.getInstance().getId()))
				return;

			addToMsgList(msg);
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
}
