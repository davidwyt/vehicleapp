package com.vehicle.app.activities;

import java.util.List;

import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.worker.IMessageCourier;
import com.vehicle.app.msg.worker.WakeupMessageCourier;
import com.vehicle.app.web.bean.WebCallBaseResult;
import com.vehicle.sdk.client.VehicleWebClient;

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
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class NearbyMainActivity extends Activity implements OnCheckedChangeListener, OnClickListener {

	private RadioGroup mRdGroup;

	private View mViewNearbyMainForm;
	private View mViewSearchNearbyStatus;
	private TextView mTextViewSearchNearbyStatus;

	private Button mButtonNearby;
	private Button mButtonNearbyList;
	private Button mButtonGroupMsg;

	private NearbySearchTask mSearchTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nearbymain);
		initView();
	}

	private void initView() {
		this.mRdGroup = ((RadioGroup) this.findViewById(R.id.bottom_rdgroup));
		this.mRdGroup.setOnCheckedChangeListener(this);

		this.mButtonNearby = (Button) this.findViewById(R.id.nearbymain_roundnearby);
		this.mButtonNearby.setOnClickListener(this);

		this.mButtonNearbyList = (Button) this.findViewById(R.id.nearbymain_nearbylist);
		this.mButtonNearbyList.setOnClickListener(this);

		this.mButtonGroupMsg = (Button) this.findViewById(R.id.nearbymain_groupmsg);
		this.mButtonGroupMsg.setOnClickListener(this);

		if (SelfMgr.getInstance().isDriver()) {
			this.mButtonNearby.setBackgroundResource(R.drawable.selector_button_roundnearbyshops);
		} else {
			this.mButtonNearby.setBackgroundResource(R.drawable.selector_button_roundnearbydrivers);
		}

		this.mViewNearbyMainForm = this.findViewById(R.id.nearbymain_form);
		this.mViewSearchNearbyStatus = this.findViewById(R.id.nearbysearch_status);
		this.mTextViewSearchNearbyStatus = (TextView) this.findViewById(R.id.nearbysearch_status_message);

		IMessageCourier courier = new WakeupMessageCourier(this.getApplicationContext());
		courier.dispatch(null);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mSearchTask = null;
		((RadioButton) this.findViewById(R.id.bar_rabtn_middle)).setChecked(true);
		// this.pageNum = 1;
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub

		if (R.id.bar_rabtn_message == checkedId) {
			openChatList();
		} else if (R.id.bar_rabtn_setting == checkedId) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), SettingActivity.class);
			this.startActivity(intent);
		}
	}

	private void openChatList() {
		
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), RecentContactListActivity.class);
		startActivity(intent);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (R.id.nearbymain_roundnearby == view.getId()) {
			searchNearby();
		} else if (R.id.nearbymain_nearbylist == view.getId()) {

		} else if (R.id.nearbymain_groupmsg == view.getId()) {

		}
	}

	private void searchNearby() {
		if (null != this.mSearchTask) {
			return;
		}

		if (SelfMgr.getInstance().isDriver()) {
			this.mTextViewSearchNearbyStatus.setText(R.string.nearbysearch_vendors);
		} else {
			this.mTextViewSearchNearbyStatus.setText(R.string.nearbysearch_dirvers);
		}

		showProgress(true);
		this.mSearchTask = new NearbySearchTask();
		mSearchTask.execute((Void) null);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			this.mViewSearchNearbyStatus.setVisibility(View.VISIBLE);
			mViewSearchNearbyStatus.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mViewSearchNearbyStatus.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

			this.mViewNearbyMainForm.setVisibility(View.VISIBLE);
			mViewNearbyMainForm.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mViewNearbyMainForm.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mViewSearchNearbyStatus.setVisibility(show ? View.VISIBLE : View.GONE);
			mViewNearbyMainForm.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	private class NearbySearchTask extends AsyncTask<Void, Void, WebCallBaseResult> {

		@Override
		protected WebCallBaseResult doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				if (SelfMgr.getInstance().isDriver()) {
					VehicleWebClient client = new VehicleWebClient();
					return client.NearbyVendorListView(1, -1, -1, 1, 121.56, 31.24, 4, 1, -1, -1);
				} else {
					VehicleWebClient client = new VehicleWebClient();
					return client.NearbyDriverListView(1, 121.56, 31.24);
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(final WebCallBaseResult result) {
			mSearchTask = null;
			showProgress(false);

			if (null == result) {
				System.err.println("can not search nearby fellows");
				return;
			}

			if (result.isSuccess()) {
				List<?> fellows = null;
				if (SelfMgr.getInstance().isDriver()) {
					fellows = (List<?>) result.getInfoBean();
					SelfMgr.getInstance().updateNearbyVendors((List<Vendor>) fellows);
				} else {
					fellows = (List<?>) result.getInfoBean();
					SelfMgr.getInstance().updateNearbyDrivers((List<Driver>) fellows);
				}

				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), NearbyFellowListActivity.class);
				startActivity(intent);

			} else {
				System.out.println("earch nearby fellows failed:" + result.getCode() + "----" + result.getMessage());
			}
		}

		@Override
		protected void onCancelled() {
			mSearchTask = null;
			showProgress(false);
		}
	}

}
