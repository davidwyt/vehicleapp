package com.vehicle.app.activities;

import com.vehicle.app.utils.StringUtil;
import com.vehicle.app.web.bean.DriverRegisterResult;
import com.vehicle.sdk.client.VehicleWebClient;

import cn.edu.sjtu.vehicleapp.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DriverRegisterActivity extends Activity {

	private EditText mEmailET;
	private EditText mUserNameET;
	private EditText mPwdET;
	private EditText mCfmPwdET;
	private Button mOKBtn;
	private Button mBackBtn;

	private View mRegFormView;
	private View mRegStatusView;
	private TextView mRegStatusMsgView;

	private RegisterTask mRegTask = null;

	private String mEmail;
	private String mUserName;
	private String mPassword;
	private String mPwdCfm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register);

		initView();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void initView() {
		this.mEmailET = (EditText) this.findViewById(R.id.reg_email);
		this.mUserNameET = (EditText) this.findViewById(R.id.reg_username);
		this.mPwdET = (EditText) this.findViewById(R.id.reg_pwd);
		this.mCfmPwdET = (EditText) this.findViewById(R.id.reg_cfmpwd);

		this.mOKBtn = (Button) this.findViewById(R.id.reg_ok);
		this.mOKBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				attemptRegister();
			}

		});

		this.mBackBtn = (Button) this.findViewById(R.id.reg_goback);
		this.mBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				DriverRegisterActivity.this.onBackPressed();
			}
		});

		this.mRegFormView = this.findViewById(R.id.register_form);
		this.mRegStatusView = this.findViewById(R.id.register_status);
		this.mRegStatusMsgView = (TextView) this.findViewById(R.id.register_status_message);
	}

	private void attemptRegister() {

		if (null != this.mRegTask) {
			return;
		}

		this.mEmail = this.mEmailET.getText().toString();
		this.mUserName = this.mUserNameET.getText().toString();
		this.mPassword = this.mPwdET.getText().toString();
		this.mPwdCfm = this.mCfmPwdET.getText().toString();

		this.mEmailET.setError(null);
		this.mUserNameET.setError(null);
		this.mPwdET.setError(null);
		this.mCfmPwdET.setError(null);

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(this.mPwdCfm)) {
			this.mCfmPwdET.setError(this.getString(R.string.register_fieldnull));
			cancel = true;
			focusView = this.mCfmPwdET;
		} else if (!this.mPwdCfm.equals(this.mPassword)) {
			this.mCfmPwdET.setError(this.getString(R.string.register_invalid_pwdcfm));
			cancel = true;
			focusView = this.mCfmPwdET;
		}

		if (TextUtils.isEmpty(this.mPassword)) {
			this.mPwdET.setError(this.getString(R.string.register_fieldnull));
			cancel = true;
			focusView = this.mPwdET;
		} else if (this.mPassword.length() < 6 || this.mPassword.length() > 20) {
			this.mPwdET.setError(this.getString(R.string.register_invalid_pwdlen));
			cancel = true;
			focusView = this.mPwdET;
		}

		if (TextUtils.isEmpty(this.mUserName)) {
			this.mUserNameET.setError(this.getString(R.string.register_fieldnull));
			cancel = true;
			focusView = this.mUserNameET;
		}

		if (TextUtils.isEmpty(this.mEmail)) {
			this.mEmailET.setError(this.getString(R.string.register_fieldnull));
			cancel = true;
			focusView = this.mEmailET;
		} else if (!StringUtil.IsEmail(this.mEmail)) {
			this.mEmailET.setError(this.getString(R.string.register_invalid_email));
			cancel = true;
			focusView = this.mEmailET;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			this.mRegStatusMsgView.setText(this.getString(R.string.register_progress));
			this.showProgress(true);
			this.mRegTask = new RegisterTask();
			this.mRegTask.execute((Void) null);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			this.mRegStatusView.setVisibility(View.VISIBLE);
			this.mRegStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {

						@Override
						public void onAnimationEnd(Animator animation) {
							mRegStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

			this.mRegFormView.setVisibility(View.VISIBLE);
			this.mRegFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {

						@Override
						public void onAnimationEnd(Animator animation) {
							mRegFormView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
		} else {
			this.mRegStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			this.mRegFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	public class RegisterTask extends AsyncTask<Void, Void, DriverRegisterResult> {

		@Override
		protected DriverRegisterResult doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			try {
				VehicleWebClient webClient = new VehicleWebClient();
				return webClient.DriverRegister(mEmail, mUserName, mPassword);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return null;
			}
		}

		@Override
		protected void onPostExecute(final DriverRegisterResult result) {

			DriverRegisterActivity.this.mRegTask = null;
			DriverRegisterActivity.this.showProgress(false);
			
			if(null == result)
			{
				mEmailET.setError(getString(R.string.error_network));
				return;
			}
			
			if (result.isSuccess()) {
				DriverRegisterActivity.this.finish();
			} else {
				mEmailET.setError(result.getMessage());
			}
		}

		@Override
		protected void onCancelled() {

			DriverRegisterActivity.this.mRegTask = null;
			DriverRegisterActivity.this.showProgress(false);
		}
	}
}
