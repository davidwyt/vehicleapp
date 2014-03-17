package com.vehicle.app.activities;

import com.vehicle.app.mgrs.ActivityManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.mgrs.UpgradeMgr;
import com.vehicle.app.msg.bean.SimpleLocation;
import com.vehicle.app.utils.ActivityUtil;

import cn.edu.sjtu.vehicleapp.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class NearbyMainActivity extends TemplateActivity implements OnCheckedChangeListener, OnClickListener {

	private RadioGroup mRdGroup;

	private View mViewNearbyMainForm;
	private View mViewSearchNearbyStatus;
	private TextView mTextViewSearchNearbyStatus;

	private Button mButtonNearby;
	private Button mButtonGroupMsg;

	private NearbySearchTask mSearchTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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

		if (!UpgradeMgr.getInstance().isChecked) {
			UpgradeMgr.getInstance().attemptCheck(this, false);
			UpgradeMgr.getInstance().isChecked = true;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void attempQuit() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle(this.getResources().getString(R.string.alert_zh));

		alertDialogBuilder
				.setMessage(getResources().getString(R.string.tip_quitconfig))
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.positive_zh),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try {
									ActivityManager.getInstance().finishAll();
									System.exit(0);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						})
				.setNegativeButton(getResources().getString(R.string.negative_zh),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			attempQuit();
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

		SimpleLocation location = SelfMgr.getInstance().getLocation();
		if (null == location) {
			Toast.makeText(getApplicationContext(), this.getString(R.string.tip_locationfailed), Toast.LENGTH_LONG)
					.show();
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
				SimpleLocation location = SelfMgr.getInstance().getLocation();
				SelfMgr.getInstance().refreshNearby(location.getLongitude(), location.getLatitude(), 1);
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
