package com.vehicle.app.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.adapter.ChatMsgViewAdapter;
import com.vehicle.app.bean.Message;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.JsonUtil;
import com.vehicle.sdk.client.VehicleClient;

import android.os.AsyncTask;
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

public class ChatActivity extends Activity implements OnClickListener {

	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	private Button mBtnSend;
	private Button mBtnBack;
	private EditText mEditTextContent;
	private ChatMsgViewAdapter mAdapter;
	private ListView mListView;

	private List<Message> mDataArrays = new ArrayList<Message>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		initView();
		initData();
		registerMessageReceiver();
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.listview);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		mBtnSend = (Button) findViewById(R.id.btn_send);
		mBtnSend.setOnClickListener(this);
		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);

		mEditTextContent.setCursorVisible(true);
		mListView.setFastScrollEnabled(true);
		mListView.setFastScrollAlwaysVisible(true);
	}

	private void registerMessageReceiver() {
		MessageReceiver messageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(Constants.ACTION_MESSAGE_RECEIVED);
		registerReceiver(messageReceiver, filter);
	}

	private String[] msgArray = new String[] {
			"  孩子们，要好好学习，天天向上！要好好听课，不要翘课！不要挂科，多拿奖学金！三等奖学金的争取拿二等，二等的争取拿一等，一等的争取拿励志！",
			"姚妈妈还有什么吩咐...", "还有，明天早上记得跑操啊，不来的就扣德育分！", "德育分是什么？扣了会怎么样？",
			"德育分会影响奖学金评比，严重的话，会影响毕业", "哇！学院那么不人道？", "你要是你不听话，我当场让你不能毕业！",
			"姚妈妈，我知错了(- -我错在哪了...)" };

	private String[] dateArray = new String[] { "2012-09-01 18:00:03",
			"2012-09-01 18:10:43", "2012-09-01 18:11:59",
			"2012-09-01 18:20:05", "2012-09-01 18:30:32",
			"2012-09-01 18:35:54", "2012-09-01 18:40:20", "2012-09-01 18:50:11" };

	private final static int COUNT = 8;

	private void initData() {
		for (int i = 0; i < COUNT; i++) {

			Message msg = new Message();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.US);

			try {
				msg.setSentDate(sdf.parse(dateArray[i]));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (i % 2 == 0) {
				msg.setSource(Constants.SELFID);
				msg.setTarget(Constants.HERID);
			} else {
				msg.setSource(Constants.HERID);
				msg.setTarget(Constants.SELFID);
			}

			msg.setContent(msgArray[i]);
			mDataArrays.add(msg);
		}

		mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
	}

	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btn_back:
			back();
			break;
		case R.id.btn_send:
			send();
			break;
		}
	}

	private void send() {
		String content = mEditTextContent.getText().toString();

		if (content.length() > 0) {

			Message entity = new Message();
			entity.setSentDate(new Date());
			entity.setSource(Constants.SELFID);
			entity.setTarget(Constants.HERID);
			entity.setContent(content);

			this.mAdapter.addMsg(entity);
			mAdapter.notifyDataSetChanged();
			mEditTextContent.setText("");
			mListView.setSelection(mListView.getCount() - 1);

			AsyncTask<String, Void, Void> sendAsync = new AsyncTask<String, Void, Void>() {

				@Override
				protected Void doInBackground(String... params) {
					// TODO Auto-generated method stub
					String content = params[0];
					VehicleClient client = new VehicleClient(Constants.SELFID);
					client.SendMessage(Constants.HERID, content);
					return null;
				}

			};
			
			sendAsync.execute(content);

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
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				String title = intent.getStringExtra(KEY_TITLE);

				Message msg = JsonUtil.fromJson(messge, Message.class);

				mAdapter.addMsg(msg);
				mAdapter.notifyDataSetChanged();
				mListView.setSelection(mListView.getCount() - 1);
			}
		}
	}
}
