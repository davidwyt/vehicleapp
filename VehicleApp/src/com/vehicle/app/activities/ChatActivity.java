package com.vehicle.app.activities;

import java.util.ArrayList;
import java.util.List;

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

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

	private Button mBtnSend;
	private Button mBtnBack;
	private Button mBtnSave;

	private EditText mEditTextContent;
	private ChatMsgViewAdapter mAdapter;
	private ListView mMsgList;

	private BroadcastReceiver messageReceiver;
	private List<Message> mDataArrays = new ArrayList<Message>();

	private String mFellowId;

	private DBManager mDBMgr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);

		mDBMgr = new DBManager(this);

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

		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
		mEditTextContent.setCursorVisible(true);

		Bundle bundle = this.getIntent().getExtras();
		this.mFellowId = bundle.getString("com.vehicle.app.activities.fellowId");

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
		mDBMgr.close();
	}

	private void registerMessageReceiver() {
		// this.unregisterReceiver(messageReceiver);
		try {
			unregisterReceiver(messageReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}

		messageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(Constants.ACTION_MESSAGE_RECEIVED);
		filter.addAction(Constants.ACTION_MESSAGE_ACKOK);
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
		}
	}

	private void save() {
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

						VehicleClient client = new VehicleClient(Constants.SERVERURL, SelfMgr.getInstance()
								.getSelfDriver().getId());

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

	class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

				String message = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				String title = intent.getStringExtra(KEY_TITLE);

				System.out.println(message);

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
			} else if (Constants.ACTION_MESSAGE_ACKOK.equals(intent.getAction())) {
				String message = intent.getStringExtra(KEY_MESSAGE);
				Message msg = JsonUtil.fromJson(message, Message.class);

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
		}
	}
}
