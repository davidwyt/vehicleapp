package com.vehicle.app.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.StringUtil;

import cn.edu.sjtu.vehicleapp.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GroupmsgNavActivity extends Activity implements OnClickListener {

	private Button mAllFellows;
	private Button mNearby;

	private View mNavFormView;
	private View mNavStatusView;
	private TextView mNavStatusMessageView;

	private RefreshFellowsTask mRefreshTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_groupmsgnav);

		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		this.mAllFellows = (Button) this.findViewById(R.id.nav_allfellows);
		this.mAllFellows.setOnClickListener(this);

		this.mNearby = (Button) this.findViewById(R.id.nav_allnearby);
		this.mNearby.setOnClickListener(this);

		mNavFormView = findViewById(R.id.groupmsgnav_form);
		mNavStatusView = findViewById(R.id.groupmsgnav_status);
		mNavStatusMessageView = (TextView) findViewById(R.id.groupmsgnav_status_message);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mNavStatusView.setVisibility(View.VISIBLE);
			mNavStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mNavStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

			mNavFormView.setVisibility(View.VISIBLE);
			mNavFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mNavFormView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mNavStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mNavFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		updateBtn();
		mRefreshTask = null;
	}

	private void updateBtn() {
		if (SelfMgr.getInstance().isDriver()) {
			this.mAllFellows.setBackgroundResource(R.drawable.selector_btn_groupnav_allfavshops);
			this.mNearby.setBackgroundResource(R.drawable.selector_btn_groupnav_nearbyshops);
		} else {
			this.mAllFellows.setBackgroundResource(R.drawable.selector_btn_groupnav_vendorfellows);
			this.mNearby.setBackgroundResource(R.drawable.selector_btn_groupnav_nearbydrivers);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View view) {
		if (R.id.nav_allfellows == view.getId()) {
			attempOpenGroupMsg(false);
		} else if (R.id.nav_allnearby == view.getId()) {
			attempOpenGroupMsg(true);
		} else {
			System.err.println("not the valid button id in the groupmsgNav Form");
		}
	}

	private void attempOpenGroupMsg(boolean isNearby) {
		if (null != this.mRefreshTask) {
			return;
		}

		if (SelfMgr.getInstance().isDriver()) {
			if (isNearby) {
				this.mNavStatusMessageView.setText(R.string.nearbysearch_vendors);
			} else {
				this.mNavStatusMessageView.setText(R.string.status_getfavvendorlist);
			}
		} else {
			if (isNearby) {
				this.mNavStatusMessageView.setText(R.string.nearbysearch_dirvers);
			} else {
				this.mNavStatusMessageView.setText(R.string.status_getvendorfellowlist);
			}
		}

		showProgress(true);
		this.mRefreshTask = new RefreshFellowsTask(isNearby);
		mRefreshTask.execute((Void) null);
	}

	private class RefreshFellowsTask extends AsyncTask<Void, Void, Void> {

		public RefreshFellowsTask(boolean isNearby) {
			this.isNearby = isNearby;
		}

		boolean isNearby;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				if (isNearby) {
					SelfMgr.getInstance().refreshNearby();
				} else {
					SelfMgr.getInstance().refreshFellows();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mRefreshTask = null;
			showProgress(false);

			if (isNearby) {
				List<String> ids = new ArrayList<String>();

				if (SelfMgr.getInstance().isDriver()) {
					Collection<Vendor> vendors = SelfMgr.getInstance().getNearbyVendors();

					for (Vendor vendor : vendors) {
						ids.add(vendor.getId());
					}

				} else {
					Collection<Driver> drivers = SelfMgr.getInstance().getNearbyDrivers();
					for (Driver driver : drivers) {
						ids.add(driver.getId());
					}
				}

				if (ids.size() > 0) {
					Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
					intent.putExtra(ChatActivity.KEY_FELLOWID, StringUtil.JointString(ids, Constants.COMMA));
					intent.putExtra(ChatActivity.KEY_CHATSTYLE, ChatActivity.CHAT_STYLE_2NEARBY);
					startActivity(intent);
				} else {
					if (SelfMgr.getInstance().isDriver()) {
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_nonearbyvendors),
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_nonearbydrivers),
								Toast.LENGTH_LONG).show();
					}
				}
			} else {
				List<String> ids = new ArrayList<String>();

				if (SelfMgr.getInstance().isDriver()) {
					Collection<Vendor> vendors = SelfMgr.getInstance().getFavVendorMap().values();
					for (Vendor vendor : vendors) {
						ids.add(vendor.getId());
					}

				} else {
					Collection<Driver> drivers = SelfMgr.getInstance().getVendorFellowMap().values();
					for (Driver driver : drivers) {
						ids.add(driver.getId());
					}
				}

				if (ids.size() > 0) {

					Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
					intent.putExtra(ChatActivity.KEY_FELLOWID, StringUtil.JointString(ids, Constants.COMMA));
					intent.putExtra(ChatActivity.KEY_CHATSTYLE, ChatActivity.CHAT_STYLE_2FELLOWS);
					startActivity(intent);
				} else {
					if (SelfMgr.getInstance().isDriver()) {
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_nofavvendors),
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_novendorfellos),
								Toast.LENGTH_LONG).show();
					}
				}
			}

		}

		@Override
		protected void onCancelled() {
			mRefreshTask = null;
			showProgress(false);
		}
	}
}
