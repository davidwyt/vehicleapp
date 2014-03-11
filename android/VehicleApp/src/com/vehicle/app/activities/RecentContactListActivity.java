package com.vehicle.app.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.vehicle.app.adapter.RecentContactListViewAdapter;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.mgrs.TopMsgerMgr;
import com.vehicle.app.msg.bean.RecentMessage;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.web.bean.DriverListViewResult;
import com.vehicle.app.web.bean.VendorListViewResult;
import com.vehicle.sdk.client.VehicleWebClient;

import cn.edu.sjtu.vehicleapp.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RecentContactListActivity extends TemplateActivity implements OnCheckedChangeListener {

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
		registerMessageReceiver();

		Handler handler = new Handler();
		final Runnable myRunnable = new Runnable() {
			public void run() {
				initData();
			}
		};
		handler.post(new Runnable() {
			public void run() {
				new Thread(myRunnable).start();
			}
		});
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initData() {

		try {
			DBManager dbMgr = new DBManager(this.getApplicationContext());

			final List<RecentMessage> recentMsgs = dbMgr.queryRecentMessage(SelfMgr.getInstance().getId());

			List<String> topMsgs = TopMsgerMgr.getInstance().getTops();
			for (int i = topMsgs.size() - 1; i >= 0; i--) {
				String top = topMsgs.get(i);
				for (int j = 0; j < recentMsgs.size(); j++) {
					RecentMessage recent = recentMsgs.get(j);
					if (recent.getFellowId().equals(top)) {
						recentMsgs.remove(j);
						recentMsgs.add(0, recent);
						break;
					}
				}
			}

			this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mRecentMsgVector.clear();
					mRecentMsgVector.addAll(recentMsgs);
					mAdapter.notifyDataSetChanged();
				}
			});

			List<String> unknown = new ArrayList<String>();

			for (RecentMessage msg : recentMsgs) {
				if (SelfMgr.getInstance().isDriver()) {
					if (null == SelfMgr.getInstance().getVendorInfo(msg.getFellowId())) {
						unknown.add(msg.getFellowId());
					}
				} else {
					if (null == SelfMgr.getInstance().getDriverInfo(msg.getFellowId())) {
						unknown.add(msg.getFellowId());
					}
				}
			}

			if (unknown.size() > 0) {
				VehicleWebClient webClient = new VehicleWebClient();
				if (SelfMgr.getInstance().isDriver()) {
					VendorListViewResult result = webClient.VendorListView(unknown);
					if (null != result && result.isSuccess()) {
						SelfMgr.getInstance().addUnknownVendors(result.getInfoBean());
					}
				} else {
					DriverListViewResult result = webClient.DriverListView(unknown);
					if (null != result && result.isSuccess()) {
						SelfMgr.getInstance().addUnknownDrivers(result.getInfoBean());
					}
				}
			}

			this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mAdapter.notifyDataSetChanged();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initView() {

		this.mAdapter = new RecentContactListViewAdapter(this, mRecentMsgVector);

		this.mPullRefreshListView = (PullToRefreshListView) this.findViewById(R.id.list_users);
		this.mPullRefreshListView.setMode(Mode.DISABLED);
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
			this.finish();
		} else if (R.id.bar_rabtn_middle == checkedId) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), NearbyMainActivity.class);
			this.startActivity(intent);
			this.finish();
		}
	}

	private void updateRecentMsg(RecentMessage newMsg) {
		synchronized (this.mRecentMsgVector) {

			try {
				int index = -1;
				for (RecentMessage message : this.mRecentMsgVector) {
					index++;
					if (message.getFellowId().equals(newMsg.getFellowId())
							&& message.getSelfId().equals(newMsg.getSelfId())) {
						if (TopMsgerMgr.getInstance().isTop(newMsg.getFellowId())) {
							this.mRecentMsgVector.set(index, newMsg);
						} else {
							this.mRecentMsgVector.remove(message);
						}

						break;
					}
				}

				if (!TopMsgerMgr.getInstance().isTop(newMsg.getFellowId())) {
					this.mRecentMsgVector.add(TopMsgerMgr.getInstance().getTops().size(), newMsg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.mAdapter.notifyDataSetChanged();
		}
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
