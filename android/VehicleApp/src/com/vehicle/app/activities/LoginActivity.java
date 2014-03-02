package com.vehicle.app.activities;

import java.io.InputStream;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.vehicle.app.bean.SelfDriver;
import com.vehicle.app.bean.SelfVendor;
import com.vehicle.app.bean.VendorDetail;
import com.vehicle.app.bean.VendorImage;
import com.vehicle.app.mgrs.BitmapCache;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.mgrs.TopMsgerMgr;
import com.vehicle.app.msg.worker.IMessageCourier;
import com.vehicle.app.msg.worker.OfflineMessageCourier;
import com.vehicle.app.msg.worker.WakeupMessageCourier;
import com.vehicle.app.utils.ActivityUtil;
import com.vehicle.app.utils.HttpUtil;
import com.vehicle.app.web.bean.CarListViewResult;
import com.vehicle.app.web.bean.VendorImgViewResult;
import com.vehicle.app.web.bean.VendorSpecViewResult;
import com.vehicle.app.web.bean.WebCallBaseResult;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.sdk.client.VehicleWebClient;

import cn.edu.sjtu.vehicleapp.R;
import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

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

		findViewById(R.id.login_signinbtn).setOnClickListener(new View.OnClickListener() {

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
	public void attemptLogin() {
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
				// Simulate network access.
				if (SelfMgr.getInstance().isDriver()) {
					VehicleWebClient webClient = new VehicleWebClient();
					result = webClient.DriverLogin(mEmail, mPassword);
				} else {
					VehicleWebClient webClient = new VehicleWebClient();
					result = webClient.VendorLogin(mEmail, mPassword);
				}

				if (null != result && result.isSuccess()) {
					if (SelfMgr.getInstance().isDriver()) {
						SelfDriver self = (SelfDriver) result.getInfoBean();

						VehicleWebClient webClient = new VehicleWebClient();
						result = webClient.CarListView(self.getId());
						self.setCars(((CarListViewResult) result).getInfoBean());

						SelfMgr.getInstance().setSelfDriver(self);

					} else {
						SelfVendor self = (SelfVendor) result.getInfoBean();
						VehicleWebClient webClient = new VehicleWebClient();
						VendorImgViewResult imgResult = webClient.VendorImgView(self.getId());
						if (null != imgResult) {
							self.setImgs(imgResult.getInfoBean());

							List<VendorImage> imgs = imgResult.getInfoBean();
							if (null != imgs) {
								for (VendorImage img : imgs) {
									String imgUrl = img.getSrc();
									InputStream input = HttpUtil.DownloadFile(imgUrl);
									Bitmap bitmap = BitmapFactory.decodeStream(input);
									BitmapCache.getInstance().put(imgUrl, bitmap);
								}
							}
						}

						VendorSpecViewResult specView = webClient.VendorSpecView(self.getId());
						if (null != specView && null != specView.getInfoBean()) {
							VendorDetail detail = specView.getInfoBean();
							self.setComments(detail.getReviews());
							self.setCoupons(detail.getCoupons());
							self.setPromotions(detail.getPromotions());
						}

						SelfMgr.getInstance().setSelfVendor(self);
					}

					IMessageCourier courier = new WakeupMessageCourier(getApplicationContext());
					courier.dispatch(null);

					IMessageCourier offCourier = new OfflineMessageCourier(getApplicationContext());
					offCourier.dispatch(null);

					SelfMgr.getInstance().refreshFellows();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

			return result;
		}

		@Override
		protected void onPostExecute(final WebCallBaseResult result) {
			mAuthTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mLoginStatusView, mLoginFormView, false);

			if (null == result) {
				mPasswordView.setError(getString(R.string.error_network));
				mPasswordView.requestFocus();
				return;
			}

			if (result.isSuccess()) {
				// finish();
				try {
					startUpdateLocation();
				} catch (Exception e) {
					e.printStackTrace();
				}

				JPushInterface.setAliasAndTags(getApplicationContext(), SelfMgr.getInstance().getId(), null);

				finish();
				
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), NearbyMainActivity.class);
				LoginActivity.this.startActivity(intent);

			} else {
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mLoginStatusView, mLoginFormView, false);
		}
	}
}
