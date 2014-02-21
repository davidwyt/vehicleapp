package com.vehicle.app.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.vehicle.app.adapter.NearbyFellowsViewAdapter;
import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.web.bean.NearbyDriverListViewResult;
import com.vehicle.app.web.bean.WebCallBaseResult;
import com.vehicle.sdk.client.VehicleWebClient;

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

	private NearbyFellowsRefreshTask mFellowsRefreshTask = null;

	@SuppressWarnings("rawtypes")
	private List mListFellows = new ArrayList();

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

		mPullRefreshListView.setMode(Mode.PULL_FROM_END);

		this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				Toast.makeText(NearbyFellowListActivity.this, "Pull Up!", Toast.LENGTH_SHORT).show();

				if (null != mFellowsRefreshTask)
					return;

				mFellowsRefreshTask = new NearbyFellowsRefreshTask();
				mFellowsRefreshTask.execute();
			}

		});

		this.mPullRefreshListView.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Object user = mListFellows.get(position-1);
				if (SelfMgr.getInstance().isDriver() && user instanceof Vendor) {
					Vendor vendor = (Vendor) user;
					Intent intent = new Intent(getApplicationContext(), VendorInfoActivity.class);
					intent.putExtra(VendorInfoActivity.KEY_VENDORID, vendor.getId());
					intent.putExtra(VendorInfoActivity.KEY_ISNEARBY, true);
					startActivity(intent);
				} else if (!SelfMgr.getInstance().isDriver() && user instanceof Driver) {
					Driver driver = (Driver) user;
					Intent intent = new Intent(getApplicationContext(), DriverInfoActivity.class);
					intent.putExtra(DriverInfoActivity.KEY_DRIVERID, driver.getId());
					intent.putExtra(DriverInfoActivity.KEY_ISNEARBY, true);
					startActivity(intent);
				}
			}
		});

		this.mAdapter = new NearbyFellowsViewAdapter(this, this.mListFellows);
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setAdapter(mAdapter);
	}

	@Override
	protected void onStart() {
		super.onStart();
		initData();

		pageNum = 1;
	}

	@SuppressWarnings({ "unchecked" })
	private void initData() {
		this.mListFellows.clear();

		if (SelfMgr.getInstance().isDriver()) {
			this.mListFellows.addAll(SelfMgr.getInstance().getNearbyVendors());
		} else {
			this.mListFellows.addAll(SelfMgr.getInstance().getNearbyDrivers());
		}

		this.mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private int pageNum = 1;

	private class NearbyFellowsRefreshTask extends AsyncTask<Void, Void, WebCallBaseResult> {

		@Override
		protected WebCallBaseResult doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			pageNum++;
			try {
				// Simulate network access.
				if (SelfMgr.getInstance().isDriver()) {
					VehicleWebClient client = new VehicleWebClient();
					return client.NearbyVendorListView(1, -1, -1, pageNum, 121.56, 31.24, 4, 1, -1, -1);
				} else {
					Map<String, Driver> nearbyDrivers = SelfMgr.getInstance().searchNearbyDrivers(121.56, 31.24, 30);
					
					NearbyDriverListViewResult result = new NearbyDriverListViewResult();
					result.setCode(WebCallBaseResult.CODE_SUCCESS);
					result.setResult(nearbyDrivers);
					
					return result;
					//VehicleWebClient client = new VehicleWebClient();
					//return client.NearbyDriverListView(1, 121.56, 31.24);
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		protected void onPostExecute(final WebCallBaseResult result) {

			mFellowsRefreshTask = null;

			if (null == result) {
				System.err.println("can not search nearby fellows");
				return;
			}

			if (result.isSuccess()) {

				if (SelfMgr.getInstance().isDriver()) {
					List vendors = (List) result.getInfoBean();
					if (null != vendors) {
						mListFellows.addAll(vendors);
						SelfMgr.getInstance().addNearbyVendors(vendors);
					}
				} else {
					List drivers = (List) result.getInfoBean();
					if (null != drivers) {
						mListFellows.addAll(drivers);
						SelfMgr.getInstance().addNearbyDrivers(drivers);
					}
				}

				mAdapter.notifyDataSetChanged();
				mPullRefreshListView.onRefreshComplete();

			} else {
				System.out.println("earch nearby fellows failed:" + result.getCode() + "----" + result.getMessage());
			}
		}

		@Override
		protected void onCancelled() {
			mPullRefreshListView = null;
		}

	}
}
