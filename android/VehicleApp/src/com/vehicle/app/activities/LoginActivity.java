package com.vehicle.app.activities;

import java.util.Date;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.vehicle.app.bean.RoleInfo;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.utils.ActivityUtil;
import com.vehicle.app.web.bean.WebCallBaseResult;
import com.vehicle.sdk.client.VehicleClient;

import cn.edu.sjtu.vehicleapp.R;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends TemplateActivity {

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private ImageView mLoginTitle;

	private Button mRegButton;
	private Button mLogButton;

	private Button mBakButton;

	private CheckBox mCBSave;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_login);

		super.onCreate(savedInstanceState);

		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.login_email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.login_password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.driverlogin_login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		mLogButton = (Button) findViewById(R.id.login_signinbtn);
		mLogButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		this.mBakButton = (Button) findViewById(R.id.login_bak);
		this.mBakButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		mRegButton = (Button) findViewById(R.id.login_regisiter);

		mLoginTitle = (ImageView) this.findViewById(R.id.login_title);

		mCBSave = (CheckBox) this.findViewById(R.id.login_checkbox_savepwd);
	}

	private void updateUI() {
		if (SelfMgr.getInstance().isDriver()) {
			mLoginTitle.setBackgroundResource(R.drawable.icon_driverlogintitle);
			mRegButton.setBackgroundResource(R.drawable.selector_btn_driverreg);

			mRegButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), DriverRegisterActivity.class);

					LoginActivity.this.startActivity(intent);
				}
			});
		} else {
			mLoginTitle.setBackgroundResource(R.drawable.icon_shoplogintitle);
			mRegButton.setBackgroundResource(R.drawable.selector_btn_shopreg);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		RoleInfo info = null;
		try {
			DBManager db = new DBManager(this.getApplicationContext());
			info = db.selectLastOnBoard(SelfMgr.getInstance().isDriver() ? RoleInfo.ROLETYPE_DRIVER
					: RoleInfo.ROLETYPE_VENDOR);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null != info && info.getIsAutoLog()) {
			this.mEmailView.setText(info.getUserName());
			this.mPasswordView.setText(info.getPassword());
			this.mCBSave.setChecked(true);
		} else {
			this.mCBSave.setChecked(false);
		}

		updateUI();
		mAuthTask = null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	private void attemptLogin() {
		if (mAuthTask != null) {

			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 6) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			ActivityUtil.showProgress(getApplicationContext(), mLoginStatusView, mLoginFormView, true);
			// showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);

		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */

	LocationManagerProxy mAMapLocationManager = null;
	AMapLocationListener myListener = new MyLocationListener();

	private void startUpdateLocation() {

		if (null != this.mAMapLocationManager) {
			try {
				mAMapLocationManager.removeUpdates(myListener);
				mAMapLocationManager.destory();
				mAMapLocationManager = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("thread in ..............");
		try {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			this.mAMapLocationManager.setGpsEnable(true);
			mAMapLocationManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 5000, 10, myListener);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class StartUpdateLocThread extends Thread {

		public void run() {
			try {
				Looper.prepare();
				startUpdateLocation();
				Looper.loop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class UpdateLocTask extends AsyncTask<Void, Void, Void> {

		private double lnt;
		private double lat;

		public UpdateLocTask(double lnt, double lat) {
			this.lnt = lnt;
			this.lat = lat;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				VehicleClient client = new VehicleClient(SelfMgr.getInstance().getId());
				client.UpdateLocation(SelfMgr.getInstance().getId(), lnt, lat);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private class MyLocationListener implements AMapLocationListener {

		long lastUpdateTime = 0;
		private UpdateLocTask updateLocTask = null;
		double lastLat = 0.0d;
		double lastLnt = 0.0d;

		private void updateLocation(Location loc) {
			try {

				final double lat;
				final double lnt;
				if (null == loc) {
					return;
				} else {
					lat = loc.getLatitude();
					lnt = loc.getLongitude();
				}

				System.out.println("location " + lat + " " + lnt);

				SelfMgr.getInstance().updateLocation(lat, lnt);

				if ((!SelfMgr.getInstance().isLogin() && SelfMgr.getInstance().isDriver())
						|| !SelfMgr.getInstance().isDriver()) {
					return;
				}

				long cur = new Date().getTime();

				if (cur - lastUpdateTime < 30 * 1000) {
					return;
				}

				if (Math.abs(lastLat - lat) < 0.000001 && Math.abs(lastLnt - lnt) < 0.000001
						&& cur - lastUpdateTime < 300 * 1000) {
					return;
				}

				updateLocTask = new UpdateLocTask(lnt, lat);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					updateLocTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					updateLocTask.execute();
				}

				this.lastUpdateTime = cur;
				this.lastLat = lat;
				this.lastLnt = lnt;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onLocationChanged(Location loc) {
			// TODO Auto-generated method stub
			updateLocation(loc);
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(AMapLocation loc) {
			// TODO Auto-generated method stub
			updateLocation(loc);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, WebCallBaseResult> {
		@Override
		protected WebCallBaseResult doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			WebCallBaseResult result = null;
			try {
				result = SelfMgr.getInstance().doLogin(mEmail, mPassword, getApplicationContext());
			} catch (Exception e) {
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(final WebCallBaseResult result) {
			mAuthTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mLoginStatusView, mLoginFormView, false);

			if (null != result && result.isSuccess()) {

				finish();

				try {
					DBManager db = new DBManager(getApplicationContext());
					db.updateLastOnboard(mEmail, mPassword, SelfMgr.getInstance().isDriver() ? RoleInfo.ROLETYPE_DRIVER
							: RoleInfo.ROLETYPE_VENDOR, mCBSave.isChecked());
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (!SelfMgr.getInstance().isDriver()) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), NearbyMainActivity.class);
					LoginActivity.this.startActivity(intent);
				}

			} else if (null == result) {
				Toast.makeText(getApplicationContext(), getString(R.string.tip_loginfailed), Toast.LENGTH_LONG).show();
			} else {
				if (null != result && (result.getCode() == 14170 || result.getCode() == 14171)) {
					Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_LONG).show();
				} else {
					if (SelfMgr.getInstance().isDriver()) {
						Toast.makeText(getApplicationContext(),
								getResources().getString(R.string.tip_loginfailedformat_driver), Toast.LENGTH_LONG)
								.show();
					} else {
						Toast.makeText(getApplicationContext(),
								getResources().getString(R.string.tip_loginfailedformat_vendor), Toast.LENGTH_LONG)
								.show();

					}
				}
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mLoginStatusView, mLoginFormView, false);
		}
	}
}
