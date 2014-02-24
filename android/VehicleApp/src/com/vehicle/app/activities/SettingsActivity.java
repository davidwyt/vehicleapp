package com.vehicle.app.activities;

import com.vehicle.app.mgrs.SelfMgr;

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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnCheckedChangeListener, OnClickListener {

	private CommentsViewTask mCommentsViewTask;
	private FellowViewTask mFellowViewTask;

	private View mViewSettingStatus;
	private TextView mTextViewSettingStatus;
	private View mSettingMainForm;
	private RadioGroup mRdGroup;

	private ImageView mIVMyInfo;
	private ImageView mIVMyFellows;
	private ImageView mIVMyComments;
	private ImageView mIVLogout;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_settings);

		initView();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub

		if (R.id.bar_rabtn_message == checkedId) {
			openChatList();
		} else if (R.id.bar_rabtn_middle == checkedId) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), NearbyMainActivity.class);
			this.startActivity(intent);
		}
	}

	private void openChatList() {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), RecentContactListActivity.class);
		startActivity(intent);

	}

	@Override
	public void onStart() {
		super.onStart();
		mCommentsViewTask = null;
		mFellowViewTask = null;
		((RadioButton) this.findViewById(R.id.bar_rabtn_setting)).setChecked(true);
	}

	private void initView() {

		this.mViewSettingStatus = this.findViewById(R.id.settings_status);
		this.mTextViewSettingStatus = (TextView) this.findViewById(R.id.settings_status_message);
		this.mSettingMainForm = this.findViewById(R.id.settings_form);
		this.mRdGroup = ((RadioGroup) this.findViewById(R.id.bottom_rdgroup));
		this.mRdGroup.setOnCheckedChangeListener(this);

		this.mIVMyComments = (ImageView) this.findViewById(R.id.settings_mycomments);
		this.mIVMyComments.setOnClickListener(this);

		this.mIVMyFellows = (ImageView) this.findViewById(R.id.settings_myfellows);
		this.mIVMyFellows.setOnClickListener(this);

		this.mIVMyInfo = (ImageView) this.findViewById(R.id.settings_myinfo);
		this.mIVMyInfo.setOnClickListener(this);

		this.mIVLogout = (ImageView) this.findViewById(R.id.settings_logout);
		this.mIVLogout.setOnClickListener(this);
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

	public class CommentsViewTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try {
				SelfMgr.getInstance().refreshComments();
			} catch (Exception e) {
				e.printStackTrace();

				System.err.println("refresh comments failed");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mCommentsViewTask = null;
			showProgress(false);

			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MyCommentsActivity.class);
			startActivity(intent);
		}

		@Override
		protected void onCancelled() {
			mCommentsViewTask = null;
			showProgress(false);
		}
	}

	private void openMyInfo() {
		if (SelfMgr.getInstance().isDriver()) {
			Intent intent = new Intent(this, SelfDriverInfoActivity.class);
			this.startActivity(intent);
		} else {
			Intent intent = new Intent(this, SelfVendorInfoActivity.class);
			this.startActivity(intent);
		}
	}

	private void openMyFellows() {
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

	private void openMyComments() {
		if (null != this.mCommentsViewTask) {
			return;
		}

		this.mTextViewSettingStatus.setText(R.string.tip_retrievecomments);

		showProgress(true);
		this.mCommentsViewTask = new CommentsViewTask();
		this.mCommentsViewTask.execute((Void) null);
	}

	private void logout() {

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.settings_myinfo:
			openMyInfo();
			break;
		case R.id.settings_myfellows:
			openMyFellows();
			break;
		case R.id.settings_mycomments:
			openMyComments();
			break;
		case R.id.settings_logout:
			logout();
			break;
		}
	}
}
