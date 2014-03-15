package com.vehicle.app.activities;

import com.vehicle.app.bean.VendorDetail;
import com.vehicle.app.mgrs.ActivityManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.mgrs.UpgradeMgr;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class SettingHomeActivity extends TemplateActivity implements OnCheckedChangeListener, OnClickListener {

	private CommentsViewTask mCommentsViewTask;
	private FellowViewTask mFellowViewTask;
	private SelfViewTask mSelfViewTask;

	private View mViewSettingStatus;
	private TextView mTextViewSettingStatus;
	private View mSettingMainForm;
	private RadioGroup mRdGroup;

	private ImageView mIVMyInfo;
	private ImageView mIVMyFellows;
	private ImageView mIVMyComments;
	private ImageView mIVLogout;

	private ImageView mIVReturnFirst;
	private ImageView mIVAbout;
	private ImageView mIVApps;
	private ImageView mIVAdvice;

	private TextView mTVFellowTip;
	private TextView mTVMyInfo;
	private TextView mTVMyComments;

	private ImageView mTVUpgrade;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_settinghome);

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
			this.finish();
		}
	}

	private void openChatList() {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), RecentContactListActivity.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public void onStart() {
		super.onStart();
		mCommentsViewTask = null;
		mFellowViewTask = null;
		((RadioButton) this.findViewById(R.id.bar_rabtn_setting)).setChecked(true);
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

		this.mIVReturnFirst = (ImageView) this.findViewById(R.id.settings_returnfirst);
		this.mIVReturnFirst.setOnClickListener(this);

		this.mIVAbout = (ImageView) this.findViewById(R.id.settings_about);
		this.mIVAbout.setOnClickListener(this);

		this.mIVAdvice = (ImageView) this.findViewById(R.id.settings_advice);
		this.mIVAdvice.setOnClickListener(this);

		this.mIVApps = (ImageView) this.findViewById(R.id.settings_moreapp);
		this.mIVApps.setOnClickListener(this);

		mTVUpgrade = (ImageView) this.findViewById(R.id.settings_upgrade);
		this.mTVUpgrade.setOnClickListener(this);

		this.findViewById(R.id.settinghome_aboutusrow).setOnClickListener(this);
		this.findViewById(R.id.settinghome_advicerow).setOnClickListener(this);
		this.findViewById(R.id.settinghome_logoutrow).setOnClickListener(this);
		this.findViewById(R.id.settinghome_moreapprow).setOnClickListener(this);
		this.findViewById(R.id.settinghome_mycommrow).setOnClickListener(this);
		this.findViewById(R.id.settinghome_myfellowrow).setOnClickListener(this);
		this.findViewById(R.id.settinghome_myrow).setOnClickListener(this);
		this.findViewById(R.id.settinghome_turnbackrow).setOnClickListener(this);
		this.findViewById(R.id.settinghome_upgraderow).setOnClickListener(this);

		this.mTVFellowTip = (TextView) this.findViewById(R.id.settings_fellowtip);
		this.mTVMyInfo = (TextView) this.findViewById(R.id.settings_tv_myinfo);
		this.mTVMyComments = (TextView) this.findViewById(R.id.settings_tv_mycomments);

		if (SelfMgr.getInstance().isDriver()) {
			this.mTVFellowTip.setText(this.getResources().getString(R.string.tip_myvendors));
			this.mTVMyInfo.setText(this.getString(R.string.zh_mydriverinfo));
			this.mTVMyComments.setText(this.getString(R.string.zh_mycomments));
		} else {
			this.mTVFellowTip.setText(this.getResources().getString(R.string.tip_mydrivers));
			this.mTVMyInfo.setText(this.getString(R.string.zh_myvendorinfo));
			this.mTVMyComments.setText(this.getString(R.string.zh_drivercomments));
		}
	}

	private class SelfViewTask extends AsyncTask<Void, Void, WebCallBaseResult> {

		@Override
		protected WebCallBaseResult doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			WebCallBaseResult result = null;

			try {
				// Simulate network access.
				if (!SelfMgr.getInstance().isDriver()) {
					VehicleWebClient webClient = new VehicleWebClient();
					result = webClient.ViewVendorAllDetail(SelfMgr.getInstance().getId());

					if (null != result && result.isSuccess()) {
						VendorSpecViewResult vendorView = (VendorSpecViewResult) result;
						VendorDetail vendor = vendorView.getInfoBean();

						if (null != vendor) {
							SelfMgr.getInstance().setSelfVendorDetail(vendor);
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
		protected void onPostExecute(WebCallBaseResult result) {
			mSelfViewTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mViewSettingStatus, mSettingMainForm, false);

			if (null != result && result.isSuccess()) {
				Intent intent = new Intent(getApplicationContext(), VendorHomeActivity.class);
				intent.putExtra(VendorHomeActivity.KEY_PERSPECTIVE, VendorHomeActivity.PERSPECTIVE_SELF);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_viewselfvendorfailed),
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mSelfViewTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mViewSettingStatus, mSettingMainForm, false);
		}
	}

	private class FellowViewTask extends AsyncTask<Void, Void, Void> {

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
			ActivityUtil.showProgress(getApplicationContext(), mViewSettingStatus, mSettingMainForm, false);
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MyFellowListActivity.class);
			startActivity(intent);
		}

		@Override
		protected void onCancelled() {
			mFellowViewTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mViewSettingStatus, mSettingMainForm, false);
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
			ActivityUtil.showProgress(getApplicationContext(), mViewSettingStatus, mSettingMainForm, false);

			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MyCommentsActivity.class);
			startActivity(intent);
		}

		@Override
		protected void onCancelled() {
			mCommentsViewTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mViewSettingStatus, mSettingMainForm, false);
		}
	}

	private void openMyInfo() {
		if (SelfMgr.getInstance().isDriver()) {
			Intent intent = new Intent(this, DriverHomeActivity.class);
			intent.putExtra(DriverHomeActivity.KEY_PERSPECTIVE, DriverHomeActivity.PERSPECTIVE_SELF);
			this.startActivity(intent);
		} else {
			attemptViewSelfVendor();
		}
	}

	private void attemptViewSelfVendor() {
		if (null != this.mSelfViewTask) {
			return;
		}

		if (!SelfMgr.getInstance().isDriver()) {
			this.mTextViewSettingStatus.setText(R.string.tip_status_selfvendorview);
		} else {
		}

		ActivityUtil.showProgress(getApplicationContext(), mViewSettingStatus, mSettingMainForm, true);
		this.mSelfViewTask = new SelfViewTask();
		this.mSelfViewTask.execute((Void) null);
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

		ActivityUtil.showProgress(getApplicationContext(), mViewSettingStatus, mSettingMainForm, true);
		this.mFellowViewTask = new FellowViewTask();
		this.mFellowViewTask.execute((Void) null);
	}

	private void openMyComments() {
		if (null != this.mCommentsViewTask) {
			return;
		}

		this.mTextViewSettingStatus.setText(R.string.tip_retrievecomments);

		ActivityUtil.showProgress(getApplicationContext(), mViewSettingStatus, mSettingMainForm, true);
		this.mCommentsViewTask = new CommentsViewTask();
		this.mCommentsViewTask.execute((Void) null);
	}

	private void logout() {
		if (SelfMgr.getInstance().isLogin()) {
			try {
				String tip = this.getString(R.string.tip_logout, SelfMgr.getInstance().isDriver() ? SelfMgr
						.getInstance().getSelfDriver().getAlias() : SelfMgr.getInstance().getSelfVendor().getName());

				Toast.makeText(getApplicationContext(), tip, Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			ActivityManager.getInstance().finishAll();
			SelfMgr.getInstance().doLogout(getApplicationContext());
			Intent intent = new Intent(this, RoleSelectActivity.class);
			this.startActivity(intent);
		} else {
			SelfMgr.getInstance().doLogout(getApplicationContext());
			Intent intent = new Intent(this, LoginActivity.class);
			this.startActivity(intent);
		}
	}

	private void about() {
		Intent intent = new Intent(this, AboutActivity.class);
		this.startActivity(intent);
	}

	private void advice() {
		Intent intent = new Intent(this, AdviceActivity.class);
		this.startActivity(intent);
	}

	private void moreApp() {
		Intent intent = new Intent(this, RelativeAppsActivity.class);
		this.startActivity(intent);
	}

	private void returnFirst() {
		this.finish();

		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), NearbyMainActivity.class);
		startActivity(intent);
	}

	private void upgrade() {
		attemptUpgrade();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.settings_myinfo:
		case R.id.settinghome_myrow:
			openMyInfo();
			break;
		case R.id.settings_myfellows:
		case R.id.settinghome_myfellowrow:
			openMyFellows();
			break;
		case R.id.settings_mycomments:
		case R.id.settinghome_mycommrow:
			openMyComments();
			break;
		case R.id.settings_logout:
		case R.id.settinghome_logoutrow:
			logout();
			break;
		case R.id.settings_about:
		case R.id.settinghome_aboutusrow:
			about();
			break;
		case R.id.settings_advice:
		case R.id.settinghome_advicerow:
			advice();
			break;
		case R.id.settings_moreapp:
		case R.id.settinghome_moreapprow:
			moreApp();
			break;
		case R.id.settings_returnfirst:
		case R.id.settinghome_turnbackrow:
			returnFirst();
			break;
		case R.id.settings_upgrade:
		case R.id.settinghome_upgraderow:
			upgrade();
			break;
		}
	}

	private void attemptUpgrade() {
		if (UpgradeMgr.getInstance().isChecking()) {
			Toast.makeText(getApplicationContext(), this.getString(R.string.tip_ischecking), Toast.LENGTH_LONG).show();
			return;
		}
		UpgradeMgr.getInstance().attemptCheck(this, true);
	}

}
