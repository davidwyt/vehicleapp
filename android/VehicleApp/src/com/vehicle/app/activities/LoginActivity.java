package com.vehicle.app.activities;

import com.vehicle.app.bean.SelfDriver;
import com.vehicle.app.bean.SelfVendor;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.utils.LocationUtil;
import com.vehicle.app.web.bean.CarListViewResult;
import com.vehicle.app.web.bean.WebCallBaseResult;
import com.vehicle.sdk.client.VehicleWebClient;

import cn.edu.sjtu.vehicleapp.R;
import cn.jpush.android.api.JPushInterface;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

			LocationUtil.getCurLocation(getApplicationContext());

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
						self.setCars(((CarListViewResult)result).getInfoBean());
						
						SelfMgr.getInstance().setSelfDriver(self);
						
					} else {
						SelfVendor self = (SelfVendor) result.getInfoBean();
						SelfMgr.getInstance().setSelfVendor(self);
					}

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
			showProgress(false);

			if (null == result) {
				mPasswordView.setError(getString(R.string.error_network));
				mPasswordView.requestFocus();
				return;
			}

			if (result.isSuccess()) {
				// finish();

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
			showProgress(false);
		}
	}
}
