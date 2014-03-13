package com.vehicle.app.activities;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.vehicle.app.adapter.MyFellowsViewAdapter;
import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.bean.VendorDetail;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.utils.ActivityUtil;
import com.vehicle.app.web.bean.VendorSpecViewResult;
import com.vehicle.app.web.bean.WebCallBaseResult;
import com.vehicle.sdk.client.VehicleWebClient;

import cn.edu.sjtu.vehicleapp.R;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyFellowListActivity extends TemplateActivity {

	private PullToRefreshListView mPullRefreshListView;

	private TextView mTitle;

	private BaseAdapter mAdapter;

	private View mMyFormView;
	private View mMyStatusView;
	private TextView mMyStatusMessageView;

	private ViewMyFellowTask mViewFellowTask = null;

	@SuppressWarnings("rawtypes")
	private List mListFellows = new ArrayList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_myfellowlist);

		initView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initView() {

		this.mMyFormView = this.findViewById(R.id.myfellows_form);
		this.mMyStatusView = this.findViewById(R.id.myfellow_status);
		this.mMyStatusMessageView = (TextView) this.findViewById(R.id.myfellow_status_message);

		mPullRefreshListView = (PullToRefreshListView) this.findViewById(R.id.myfellows);
		this.mTitle = (TextView) this.findViewById(R.id.myfellowlist_title_nametv);

		if (SelfMgr.getInstance().isDriver()) {
			this.mTitle.setText(this.getString(R.string.title_myfavvendor));
		} else {
			this.mTitle.setText(this.getString(R.string.title_myvendorfellow));
		}

		Button bakBtn = (Button) this.findViewById(R.id.myfellowlist_goback);
		bakBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
				finish();
			}
		});

		mPullRefreshListView.setMode(Mode.DISABLED);

		this.mPullRefreshListView.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Object user = mListFellows.get(position - 1);
				if (SelfMgr.getInstance().isDriver() && user instanceof Vendor) {
					Vendor vendor = (Vendor) user;
					if (null != vendor)
						attempViewFellow(vendor.getId());

				} else if (!SelfMgr.getInstance().isDriver() && user instanceof Driver) {
					Driver driver = (Driver) user;
					Intent intent = new Intent(getApplicationContext(), DriverHomeActivity.class);
					intent.putExtra(DriverHomeActivity.KEY_PERSPECTIVE, DriverHomeActivity.PERSPECTIVE_FELLOW);
					intent.putExtra(DriverHomeActivity.KEY_DRIVERINFOID, driver.getId());
					startActivity(intent);
				}
			}
		});

		this.mAdapter = new MyFellowsViewAdapter(this, this.mListFellows);
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setAdapter(mAdapter);
	}

	private void startVendorHome(String id) {
		Intent intent = new Intent(getApplicationContext(), VendorHomeActivity.class);
		intent.putExtra(VendorHomeActivity.KEY_VENDORID, id);
		intent.putExtra(VendorHomeActivity.KEY_PERSPECTIVE, VendorHomeActivity.PERSPECTIVE_FELLOW);
		startActivity(intent);
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
			this.mListFellows.addAll(SelfMgr.getInstance().getFavVendorMap().values());
		} else {
			this.mListFellows.addAll(SelfMgr.getInstance().getVendorFellowMap().values());
		}

		this.mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void attempViewFellow(String id) {

		if (SelfMgr.getInstance().isDriver()) {
			if (SelfMgr.getInstance().isFavVendorDetailExist(id)) {
				startVendorHome(id);
				return;
			}
		}

		if (null != this.mViewFellowTask)
			return;

		if (SelfMgr.getInstance().isDriver()) {
			mMyStatusMessageView.setText(R.string.tip_myfellowvendorstatustext);
		} else {
		}

		ActivityUtil.showProgress(getApplicationContext(), mMyStatusView, mMyFormView, true);
		mViewFellowTask = new ViewMyFellowTask();
		mViewFellowTask.execute(id);
	}

	private class ViewMyFellowTask extends AsyncTask<String, Void, WebCallBaseResult> {
		private String fellowId;

		@Override
		protected WebCallBaseResult doInBackground(String... params) {
			// TODO: attempt authentication against a network service.
			fellowId = params[0];
			WebCallBaseResult result = null;
			try {
				// Simulate network access.
				if (SelfMgr.getInstance().isDriver()) {
					VehicleWebClient webClient = new VehicleWebClient();
					result = webClient.ViewVendorAllDetail(fellowId);

					if (null != result && result.isSuccess()) {
						VendorSpecViewResult vendorView = (VendorSpecViewResult) result;
						VendorDetail vendor = vendorView.getInfoBean();

						if (null != vendor) {
							SelfMgr.getInstance().updateFavVendorDetail(vendor);
						}
					}

				} else {

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(final WebCallBaseResult result) {
			mViewFellowTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mMyStatusView, mMyFormView, false);

			if (null != result && result.isSuccess()) {
				startVendorHome(fellowId);
			} else {
				Toast.makeText(MyFellowListActivity.this,
						getResources().getString(R.string.tip_loadvendordetailfailed), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mViewFellowTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mMyStatusView, mMyFormView, false);
		}
	}
}
