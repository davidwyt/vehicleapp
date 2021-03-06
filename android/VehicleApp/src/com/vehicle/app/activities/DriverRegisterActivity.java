package com.vehicle.app.activities;

import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.utils.ActivityUtil;
import com.vehicle.app.web.bean.DriverRegisterResult;
import com.vehicle.app.web.bean.WebCallBaseResult;
import com.vehicle.sdk.client.VehicleWebClient;

import cn.edu.sjtu.vehicleapp.R;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DriverRegisterActivity extends TemplateActivity {

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
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

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
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			this.mRegStatusMsgView.setText(this.getString(R.string.register_progress));
			ActivityUtil.showProgress(getApplicationContext(), mRegStatusView, mRegFormView, true);
			this.mRegTask = new RegisterTask();
			this.mRegTask.execute((Void) null);
		}
	}

	public class RegisterTask extends AsyncTask<Void, Void, DriverRegisterResult> {

		WebCallBaseResult loginResult = null;

		@Override
		protected DriverRegisterResult doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			DriverRegisterResult regResult = null;
			try {
				VehicleWebClient webClient = new VehicleWebClient();
				regResult = webClient.DriverRegister(mEmail, mUserName, mPassword);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (null != regResult && regResult.isSuccess()) {
				try {
					DriverRegisterActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mRegStatusMsgView.setText(getString(R.string.login_progress_signing_in));
						}
					});

					loginResult = SelfMgr.getInstance().doLogin(mUserName, mPassword, getApplicationContext());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return regResult;
		}

		@Override
		protected void onPostExecute(final DriverRegisterResult result) {

			DriverRegisterActivity.this.mRegTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mRegStatusView, mRegFormView, false);

			if (null == result) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_regfailed),
						Toast.LENGTH_LONG).show();
				return;
			}

			if (result.isSuccess()) {
				if (null == loginResult) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_loginfailed),
							Toast.LENGTH_LONG).show();
				} else if (!loginResult.isSuccess()) {
					if (loginResult.getCode() == 14170 || loginResult.getCode() == 14171) {
						Toast.makeText(getApplicationContext(), loginResult.getMessage(), Toast.LENGTH_LONG).show();
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
				} else {
					DriverRegisterActivity.this.finish();

					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), NearbyMainActivity.class);
					startActivity(intent);
				}
			} else {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.tip_regfailedformat, result.getMessage()), Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		protected void onCancelled() {
			DriverRegisterActivity.this.mRegTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mRegStatusView, mRegFormView, false);
		}
	}
}
