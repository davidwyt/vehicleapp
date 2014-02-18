package com.vehicle.app.activities;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.vehicle.app.adapter.MyFellowsViewAdapter;
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
import android.widget.ImageView;
import android.widget.ListView;

public class MyFellowListActivity extends Activity {

	private PullToRefreshListView mPullRefreshListView;

	private ImageView mTitle;

	private BaseAdapter mAdapter;

	@SuppressWarnings("rawtypes")
	private List mListFellows = new ArrayList();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_myfellowlist);

		initView();
	}

	private void initView() {
		mPullRefreshListView = (PullToRefreshListView) this.findViewById(R.id.myfellows);
		this.mTitle = (ImageView) this.findViewById(R.id.myfellowlist_title);

		if (SelfMgr.getInstance().isDriver()) {
			this.mTitle.setImageResource(R.drawable.icon_title_myfavvendors);
		} else {
			this.mTitle.setImageResource(R.drawable.icon_title_myfellowdrivers);
		}

		mPullRefreshListView.setMode(Mode.DISABLED);

		this.mPullRefreshListView.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Object user = mListFellows.get(position-1);
				if (SelfMgr.getInstance().isDriver() && user instanceof Vendor) {
					Vendor vendor = (Vendor) user;
					Intent intent = new Intent(getApplicationContext(), VendorInfoActivity.class);
					intent.putExtra(VendorInfoActivity.KEY_VENDORID, vendor.getId());
					intent.putExtra(VendorInfoActivity.KEY_ISNEARBY, false);
					startActivity(intent);
				} else if (!SelfMgr.getInstance().isDriver() && user instanceof Driver) {
					Driver driver = (Driver) user;
					Intent intent = new Intent(getApplicationContext(), DriverInfoActivity.class);
					intent.putExtra(DriverInfoActivity.KEY_DRIVERID, driver.getId());
					intent.putExtra(DriverInfoActivity.KEY_ISNEARBY, false);
					startActivity(intent);
				}
			}
		});

		this.mAdapter = new MyFellowsViewAdapter(this, this.mListFellows);
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setAdapter(mAdapter);
	}

	@Override
	protected void onStart() {
		super.onStart();
		initData();
	}

	@SuppressWarnings({ "unchecked" })
	private void initData() {
		this.mListFellows.clear();

		if (SelfMgr.getInstance().isDriver()) {
			this.mListFellows.addAll(SelfMgr.getInstance().getFavVendorDetailMap().values());
		} else {
			this.mListFellows.addAll(SelfMgr.getInstance().getVendorFellowDetailMap().values());
		}

		this.mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
