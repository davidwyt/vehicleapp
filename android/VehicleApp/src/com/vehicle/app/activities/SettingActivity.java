package com.vehicle.app.activities;

import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.utils.StringUtil;
import com.vehicle.sdk.client.VehicleClient;

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
import android.widget.EditText;
import android.widget.TextView;

public class SettingActivity extends TemplateActivity {

	private EditText selfIdET;
	private EditText herIdET;
	private EditText serverET;
	private TextView tip;

	private FellowViewTask mFellowViewTask;

	private View mViewSettingStatus;
	private TextView mTextViewSettingStatus;
	private View mSettingMainForm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_setting);

		initView();
	}

	@Override
	public void onStart() {
		super.onStart();
		mFellowViewTask = null;
	}

	private void initView() {

		this.selfIdET = (EditText) this.findViewById(R.id.setting_selfID);
		this.herIdET = (EditText) this.findViewById(R.id.setting_herID);
		this.serverET = (EditText) this.findViewById(R.id.setting_serverurl);
		this.tip = (TextView) this.findViewById(R.id.setting_tip);

		this.selfIdET.setText(SelfMgr.getInstance().getId());
		this.herIdET.setText(ChatActivity.getCurrentFellowId());
		this.serverET.setText(VehicleClient.getRootServer());

		this.mViewSettingStatus = this.findViewById(R.id.setting_status);
		this.mTextViewSettingStatus = (TextView) this.findViewById(R.id.setting_status_message);
		this.mSettingMainForm = this.findViewById(R.id.setting_form);

		Button btn = (Button) this.findViewById(R.id.setting_ok);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View btn) {
				// TODO Auto-generated method stub
				String selfId = selfIdET.getText().toString();
				String herId = herIdET.getText().toString();
				String server = serverET.getText().toString();
				if (StringUtil.IsNullOrEmpty(selfId) || StringUtil.IsNullOrEmpty(herId)
						|| StringUtil.IsNullOrEmpty(server)) {
					tip.setText("your id/her id/serverurl can't be null");
					return;
				}

				// Constants.SERVERURL = server;

				ChatActivity.setCurrentFellowId(herId);
				VehicleClient.setRootServer(server);

				tip.setText("setting success");
			}
		});

		Button openFellowsButton = (Button) this.findViewById(R.id.setting_btn_openfellows);
		openFellowsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openFellows();
			}
		});
	}

	private void openFellows() {
		if (null != this.mFellowViewTask) {
			return;
		}

		if (SelfMgr.getInstance().isDriver()) {
			this.mTextViewSettingStatus.setText(R.string.status_getfavvendorlist);
		} else {
			this.mTextViewSettingStatus.setText(R.string.status_getvendorfellowlist);
		}

		showProgress(true);
		this.mFellowViewTask = new FellowViewTask();
		this.mFellowViewTask.execute((Void) null);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			this.mViewSettingStatus.setVisibility(View.VISIBLE);
			mViewSettingStatus.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mViewSettingStatus.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

			this.mSettingMainForm.setVisibility(View.VISIBLE);
			mSettingMainForm.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mSettingMainForm.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mViewSettingStatus.setVisibility(show ? View.VISIBLE : View.GONE);
			mSettingMainForm.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	public class FellowViewTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try {
				SelfMgr.getInstance().refreshFellows();
			} catch (Exception e) {
				e.printStackTrace();

				System.err.println("refresh fellows failed");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mFellowViewTask = null;
			showProgress(false);

			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MyFellowListActivity.class);
			startActivity(intent);
		}

		@Override
		protected void onCancelled() {
			mFellowViewTask = null;
			showProgress(false);
		}
	}
}
