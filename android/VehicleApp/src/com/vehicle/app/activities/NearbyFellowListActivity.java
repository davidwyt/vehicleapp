package com.vehicle.app.activities;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.vehicle.app.adapter.NearbyFellowsViewAdapter;
import com.vehicle.app.bean.Comment;
import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.BitmapCache;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.HttpUtil;
import com.vehicle.app.web.bean.NearbyDriverListViewResult;
import com.vehicle.app.web.bean.VendorViewResult;
import com.vehicle.app.web.bean.WebCallBaseResult;
import com.vehicle.sdk.client.VehicleWebClient;

import cn.edu.sjtu.vehicleapp.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NearbyFellowListActivity extends Activity {

	private PullToRefreshListView mPullRefreshListView;

	private ImageView mTitle;

	private BaseAdapter mAdapter;

	private View mNearbyFormView;
	private View mNearbyStatusView;
	private TextView mNearbyStatusMessageView;

	private NearbyFellowsRefreshTask mFellowsRefreshTask = null;
	private ViewFellowTask mViewFellowTask = null;

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
		this.mNearbyFormView = this.findViewById(R.id.nearbyfellows_form);
		this.mNearbyStatusView = this.findViewById(R.id.nearbyfellow_status);
		this.mNearbyStatusMessageView = (TextView) this.findViewById(R.id.nearbyfellow_status_message);

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
				Object user = mListFellows.get(position - 1);
				if (SelfMgr.getInstance().isDriver() && user instanceof Vendor) {
					Vendor vendor = (Vendor) user;
					attempViewFellow(vendor.getId());
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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mNearbyStatusView.setVisibility(View.VISIBLE);
			mNearbyStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mNearbyStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

			mNearbyFormView.setVisibility(View.VISIBLE);
			mNearbyFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mNearbyFormView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mNearbyStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mNearbyStatusView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	private void attempViewFellow(String id) {
		if (null != this.mViewFellowTask)
			return;

		if (SelfMgr.getInstance().isDriver()) {
			mNearbyStatusMessageView.setText(R.string.nearbyvendorstatustext);
		} else {
		}

		showProgress(true);
		mViewFellowTask = new ViewFellowTask();
		mViewFellowTask.execute(id);
	}

	private int pageNum = 1;

	private class NearbyFellowsRefreshTask extends AsyncTask<Void, Void, WebCallBaseResult> {

		@Override
		protected WebCallBaseResult doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			pageNum++;
			try {
				if (SelfMgr.getInstance().isDriver()) {
					VehicleWebClient client = new VehicleWebClient();
					return client.NearbyVendorListView(1, -1, -1, pageNum, 121.56, 31.24, 4, 1, -1, -1);
				} else {
					Map<String, Driver> nearbyDrivers = SelfMgr.getInstance().searchNearbyDrivers(121.56, 31.24, 30);

					NearbyDriverListViewResult result = new NearbyDriverListViewResult();
					result.setCode(WebCallBaseResult.CODE_SUCCESS);
					result.setResult(nearbyDrivers);

					return result;
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

	public class ViewFellowTask extends AsyncTask<String, Void, WebCallBaseResult> {
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
					result = webClient.VendorSpecView(fellowId);

					if (null != result && result.isSuccess()) {
						VendorViewResult vendorView = (VendorViewResult) result;
						Vendor vendor = vendorView.getInfoBean();

						SelfMgr.getInstance().updateNearbyVendor(vendor);
						List<Comment> comments = vendor.getReviews();
						if (null != comments && comments.size() > 0) {
							for (Comment comment : comments) {
								String imgs = comment.getImgNamesM();
								String[] names = imgs.split(Constants.IMGNAME_DIVIDER);

								for (String name : names) {
									if (null != name && name.length() > 0) {
										String imgUrl = Constants.getMiddleVendorImg(name);
										InputStream input = HttpUtil.DownloadFile(imgUrl);
										Bitmap bitmap = BitmapFactory.decodeStream(input);
										BitmapCache.getInstance().put(imgUrl, bitmap);
									}
								}
							}
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
			showProgress(false);

			if (null != result && result.isSuccess()) {

				Intent intent = new Intent(getApplicationContext(), VendorInfoActivity.class);
				intent.putExtra(VendorInfoActivity.KEY_VENDORID, fellowId);
				intent.putExtra(VendorInfoActivity.KEY_ISNEARBY, true);
				startActivity(intent);
			} else {
				Toast.makeText(NearbyFellowListActivity.this,
						getResources().getString(R.string.tip_viewnearbyvendorfailed), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mViewFellowTask = null;
			showProgress(false);
		}
	}
}
