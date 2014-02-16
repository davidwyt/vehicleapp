package com.vehicle.app.activities;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vehicle.app.adapter.RecentContactListViewAdapter;
import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.SelfMgr;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.content.Intent;
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

	@SuppressWarnings("rawtypes")
	private List mListRecentContacts = new ArrayList();

	private RadioGroup mRdGroup;

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
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@SuppressWarnings("unchecked")
	private void initData() {

		mListRecentContacts.clear();

		if (SelfMgr.getInstance().isDriver()) {
			mListRecentContacts.addAll(SelfMgr.getInstance().getFavVendorDetailMap().values());
		} else {
			mListRecentContacts.addAll(SelfMgr.getInstance().getVendorFellowDetailMap().values());
		}

		this.mAdapter.notifyDataSetChanged();
	}

	private void initView() {

		this.mAdapter = new RecentContactListViewAdapter(this, mListRecentContacts);

		this.mPullRefreshListView = (PullToRefreshListView) this.findViewById(R.id.list_users);
		this.mPullRefreshListView.getRefreshableView().setAdapter(this.mAdapter);
		this.mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Object user = mAdapter.getItem(position);
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ChatActivity.class);

				if (SelfMgr.getInstance().isDriver() && user instanceof Vendor) {
					intent.putExtra(ChatActivity.KEY_FELLOWID, ((Vendor) user).getId());
				} else if (!SelfMgr.getInstance().isDriver() && user instanceof Driver) {
					intent.putExtra(ChatActivity.KEY_FELLOWID, ((Driver) user).getId());
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
			intent.setClass(getApplicationContext(), SettingActivity.class);
			this.startActivity(intent);
		} else if (R.id.bar_rabtn_middle == checkedId) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), NearbyMainActivity.class);
			this.startActivity(intent);
		}
	}
}
