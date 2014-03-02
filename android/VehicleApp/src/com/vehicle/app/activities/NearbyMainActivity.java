package com.vehicle.app.activities;

import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.mgrs.TopMsgerMgr;
import com.vehicle.app.utils.ActivityUtil;
import com.vehicle.app.utils.LocationUtil;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
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
	private Button mButtonGroupMsg;

	private NearbySearchTask mSearchTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nearbymain);
		initView();
		startUpdateLocation();
	}

	private void startUpdateLocation() {

	}

	private void initView() {
		this.mRdGroup = ((RadioGroup) this.findViewById(R.id.bottom_rdgroup));
		this.mRdGroup.setOnCheckedChangeListener(this);

		this.mButtonNearby = (Button) this.findViewById(R.id.nearbymain_roundnearby);
		this.mButtonNearby.setOnClickListener(this);

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
	}

	@Override
	protected void onStart() {
		super.onStart();
		mSearchTask = null;
		((RadioButton) this.findViewById(R.id.bar_rabtn_middle)).setChecked(true);
		TopMsgerMgr.getInstance().init(getApplicationContext(), SelfMgr.getInstance().getId());
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub

		if (R.id.bar_rabtn_message == checkedId) {
			openChatList();
		} else if (R.id.bar_rabtn_setting == checkedId) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), SettingHomeActivity.class);
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
		} else if (R.id.nearbymain_groupmsg == view.getId()) {
			startGroupMsg();
		}
	}

	private void startGroupMsg() {
		Intent intent = new Intent(this, GroupmsgNavActivity.class);
		this.startActivity(intent);
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

		ActivityUtil.showProgress(getApplicationContext(), mViewSearchNearbyStatus, mViewNearbyMainForm, true);
		this.mSearchTask = new NearbySearchTask();
		mSearchTask.execute((Void) null);
	}

	private class NearbySearchTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				Location loc = LocationUtil.getCurLocation(getApplicationContext());
				double latitude, longtitude;
				if (null == loc) {
					latitude = 31.24;
					longtitude = 121.56;
				} else {
					latitude = loc.getLatitude();
					longtitude = loc.getLongitude();
				}
				SelfMgr.getInstance().refreshNearby(longtitude, latitude);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mSearchTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mViewSearchNearbyStatus, mViewNearbyMainForm, false);

			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), NearbyFellowListActivity.class);
			startActivity(intent);
		}

		@Override
		protected void onCancelled() {
			mSearchTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mViewSearchNearbyStatus, mViewNearbyMainForm, false);
		}
	}

}
