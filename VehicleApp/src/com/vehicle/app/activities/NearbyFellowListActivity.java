package com.vehicle.app.activities;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.vehicle.app.adapter.NearbyFellowsViewAdapter;
import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.SelfMgr;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class NearbyFellowListActivity extends Activity {

	private PullToRefreshListView mPullRefreshListView;
	
	private ImageView mTitle;

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
		mPullRefreshListView = (PullToRefreshListView) this.findViewById(R.id.nearbyfellows);
		this.mTitle = (ImageView) this.findViewById(R.id.nearby_title);

		if (SelfMgr.getInstance().isDriver()) {
			this.mTitle.setImageResource(R.drawable.icon_nearbyshopstitle);
		} else {
			this.mTitle.setImageResource(R.drawable.icon_nearbydriverstitle);
		}

		
		//this.mPullRefreshListView.setAdapter(mAdapter);
		mPullRefreshListView.setMode(Mode.BOTH);

		this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				//Toast.makeText(NearbyFellowListActivity.this, "Pull Down!", Toast.LENGTH_SHORT).show();
				//new GetDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				Toast.makeText(NearbyFellowListActivity.this, "Pull Up!", Toast.LENGTH_SHORT).show();
				new GetDataTask().execute();
			}

		});
		
		this.mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

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
		
		this.mAdapter = new NearbyFellowsViewAdapter(this, this.mListFillows);
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setAdapter(mAdapter);
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
		//this.mPullRefreshListView.setse
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			mAdapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
}
