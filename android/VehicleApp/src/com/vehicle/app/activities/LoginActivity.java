package com.vehicle.app.activities;

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
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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

	public static final String KEY_AUDOLOGIN = "com.vehicle.app.login.key.autolog";

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

		mLogButton = (Button)findViewById(R.id.login_signinbtn);
		mLogButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		mRegButton = (Button) findViewById(R.id.login_regisiter);

		mLoginTitle = (ImageView) this.findViewById(R.id.login_title);

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

		boolean isAuto = false;
		Bundle bundle = this.getIntent().getExtras();
		if (null != bundle) {
			isAuto = bundle.getBoolean(KEY_AUDOLOGIN, false);
		}

		RoleInfo info = null;
		try {
			DBManager db = new DBManager(this.getApplicationContext());
			info = db.selectLastOnBoard();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (isAuto && null != info) {
			this.mEmailView.setText(info.getUserName());
			this.mPasswordView.setText(info.getPassword());

			enableEdit(false);
			SelfMgr.getInstance().clearFellows();
			SelfMgr.getInstance().setIsDriver(info.getRoleType() == RoleInfo.ROLETYPE_DRIVER ? true : false);

			mAuthTask = null;
			attemptLogin();
		} else {
			enableEdit(true);
		}
	}
	
	private void enableEdit(boolean enable)
	{
		this.mEmailView.setEnabled(enable);
		this.mPasswordView.setEnabled(enable);
		this.mLogButton.setClickable(enable);
		this.mRegButton.setClickable(enable);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
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
		/**
		 * else if (!StringUtil.IsEmail(mEmail)) {
		 * mEmailView.setError(getString(R.string.error_invalid_email));
		 * focusView = mEmailView; cancel = true; }
		 */

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			enableEdit(true);
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

		try {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			this.mAMapLocationManager.setGpsEnable(true);
			mAMapLocationManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 5000, 10, myListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class MyLocationListener implements AMapLocationListener {

		double lastLat = -1.0d;
		double lastLnt = -1.0d;
		long lastUpdateTime = 0;

		private void updateLocation(Location loc) {
			try {
				// Location loc =
				// LocationUtil.getCurLocation(getApplicationContext());
				double lat, lnt;
				if (null == loc) {
					lat = 31.24;
					lnt = 121.56;
				} else {
					lat = loc.getLatitude();
					lnt = loc.getLongitude();
				}

				VehicleClient client = new VehicleClient(SelfMgr.getInstance().getId());
				client.UpdateLocation(SelfMgr.getInstance().getId(), lnt, lat);
				/**
				 * long cur = new Date().getTime(); if (0 !=
				 * Double.compare(lastLat, lat) && 0 != Double.compare(lastLnt,
				 * lnt) || (cur - lastUpdateTime) >= 5 * 60 * 1000) {
				 * VehicleClient client = new
				 * VehicleClient(SelfMgr.getInstance().getId());
				 * client.UpdateLocation(SelfMgr.getInstance().getId(), lnt,
				 * lat); lastLnt = lnt; lastLat = lat; lastUpdateTime = cur; }
				 */
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

			if (null == result) {
				enableEdit(true);
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_loginfailed),
						Toast.LENGTH_LONG).show();
				return;
			}

			if (result.isSuccess()) {
				// finish();
				try {
					startUpdateLocation();
				} catch (Exception e) {
					e.printStackTrace();
				}

				finish();

				try {
					DBManager db = new DBManager(getApplicationContext());
					db.updateLastOnboard(mEmail, mPassword, SelfMgr.getInstance().isDriver() ? RoleInfo.ROLETYPE_DRIVER
							: RoleInfo.ROLETYPE_VENDOR, true);
				} catch (Exception e) {
					e.printStackTrace();
				}

				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), NearbyMainActivity.class);
				LoginActivity.this.startActivity(intent);

			} else {
				enableEdit(true);
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.tip_loginfailedformat, result.getMessage()),
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			enableEdit(true);
			ActivityUtil.showProgress(getApplicationContext(), mLoginStatusView, mLoginFormView, false);
		}
	}
}
