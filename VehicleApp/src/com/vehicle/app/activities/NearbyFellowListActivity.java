package com.vehicle.app.activities;

import java.util.ArrayList;
import java.util.List;

import com.vehicle.app.adapter.NearbyFellowsViewAdapter;
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
import android.widget.ListView;

public class NearbyFellowListActivity extends Activity {

	private ListView mLVFellows;

	private BaseAdapter mAdapter;

	public static final String KEY_NEARBYFELLOWS = "com.vehicle.app.key.nearbyfellows";

	@SuppressWarnings("rawtypes")
	private List<?> mListFillows = new ArrayList();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_nearbyfellowlist);

		initView();
	}

	private void initView() {
		mLVFellows = (ListView) this.findViewById(R.id.nearbyfellows);

		this.mAdapter = new NearbyFellowsViewAdapter(this, this.mListFillows);
		this.mLVFellows.setAdapter(mAdapter);

		this.mLVFellows.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Object user = mListFillows.get(position);
				if (SelfMgr.getInstance().isDriver() && user instanceof Vendor) {
					Vendor vendor = (Vendor) user;
					Intent intent = new Intent(getApplicationContext(), VendorInfoActivity.class);
					intent.putExtra(VendorInfoActivity.KEY_NEARBYVENDORID, vendor.getId());
					startActivity(intent);
				} else if (!SelfMgr.getInstance().isDriver() && user instanceof Driver) {
					Driver driver = (Driver) user;
					Intent intent = new Intent(getApplicationContext(), DriverInfoActivity.class);
					intent.putExtra(DriverInfoActivity.KEY_NEARBYDRIVERID, driver.getId());
					startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		initData();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initData() {
		this.mListFillows.clear();

		Bundle bundle = this.getIntent().getExtras();
		if (null != bundle) {
			List fellows = (List<?>) bundle.getSerializable(KEY_NEARBYFELLOWS);
			this.mListFillows.addAll(fellows);
		}

		this.mAdapter.notifyDataSetChanged();
		this.mLVFellows.setSelection(0);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
