package com.vehicle.app.activities;

import java.util.Vector;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vehicle.app.adapter.RecentContactListViewAdapter;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.RecentMessage;
import com.vehicle.app.utils.Constants;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RecentContactListActivity extends Activity implements OnCheckedChangeListener {

	private PullToRefreshListView mPullRefreshListView;

	private BaseAdapter mAdapter;

	private Vector<RecentMessage> mRecentMsgVector = new Vector<RecentMessage>();

	private RadioGroup mRdGroup;

	private BroadcastReceiver messageReceiver;

	public static final String KEY_RECENTMSG = "com.vehicle.app.key.recentmsg";

	public static final String ACTIVITYNAME = "com.vehicle.app.activities.RecentContactListActivity";

	@Override
	protected void onCreate(Bundle bundle) {

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(bundle);
		setContentView(R.layout.activity_recentcontactlist);

		initView();
	}

	@Override
	protected void onStart() {
		super.onStart();

		((RadioButton) this.findViewById(R.id.bar_rabtn_message)).setChecked(true);
		initData();
		registerMessageReceiver();
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

	private void registerMessageReceiver() {
		// this.unregisterReceiver(messageReceiver);
		try {
			unregisterReceiver(messageReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}

		messageReceiver = new RecentMessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(Constants.ACTION_RECENTMSG_UPDATE);

		registerReceiver(messageReceiver, filter);
	}

	private void initData() {

		mRecentMsgVector.clear();

		DBManager dbMgr = new DBManager(this.getApplicationContext());

		mRecentMsgVector.addAll(dbMgr.queryRecentMessage(SelfMgr.getInstance().getId()));

		this.mAdapter.notifyDataSetChanged();
	}

	private void initView() {

		this.mAdapter = new RecentContactListViewAdapter(this, mRecentMsgVector);

		this.mPullRefreshListView = (PullToRefreshListView) this.findViewById(R.id.list_users);
		this.mPullRefreshListView.getRefreshableView().setAdapter(this.mAdapter);
		this.mPullRefreshListView.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Object item = mAdapter.getItem(position - 1);
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ChatActivity.class);

				RecentMessage msg = (RecentMessage) item;

				if (SelfMgr.getInstance().isDriver()) {
					intent.putExtra(ChatActivity.KEY_FELLOWID, msg.getFellowId());
					intent.putExtra(ChatActivity.KEY_CHATSTYLE, ChatActivity.CHAT_STYLE_2ONE);
				} else if (!SelfMgr.getInstance().isDriver()) {
					intent.putExtra(ChatActivity.KEY_FELLOWID, msg.getFellowId());
					intent.putExtra(ChatActivity.KEY_CHATSTYLE, ChatActivity.CHAT_STYLE_2ONE);
				}

				RecentContactListActivity.this.startActivity(intent);
			}
		});

		this.mRdGroup = (RadioGroup) this.findViewById(R.id.bottom_rdgroup);
		this.mRdGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if (R.id.bar_rabtn_setting == checkedId) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), SettingHomeActivity.class);
			this.startActivity(intent);
		} else if (R.id.bar_rabtn_middle == checkedId) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), NearbyMainActivity.class);
			this.startActivity(intent);
		}
	}

	private void updateRecentMsg(RecentMessage newMsg) {
		synchronized (this.mRecentMsgVector) {
			for (RecentMessage message : this.mRecentMsgVector) {
				if (message.getFellowId().equals(newMsg.getFellowId())
						&& message.getSelfId().equals(newMsg.getSelfId())) {
					this.mRecentMsgVector.remove(message);
					break;
				}
			}

			this.mRecentMsgVector.add(0, newMsg);
		}

		this.mAdapter.notifyDataSetChanged();
	}

	class RecentMessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();

			if (Constants.ACTION_RECENTMSG_UPDATE.equals(action)) {
				RecentMessage msg = (RecentMessage) intent.getSerializableExtra(KEY_RECENTMSG);
				updateRecentMsg(msg);
			} else {
				System.err.println("what the hell of action:" + action);
			}
		}
	}
}
