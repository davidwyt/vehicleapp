package com.vehicle.app.activities;

import com.vehicle.app.utils.ActivityUtil;
import com.vehicle.app.utils.StringUtil;
import com.vehicle.app.web.bean.DriverRegisterResult;
import com.vehicle.sdk.client.VehicleWebClient;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
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
			ActivityUtil.showProgress(getApplicationContext(), mRegStatusView, mRegFormView, true);
			this.mRegTask = new RegisterTask();
			this.mRegTask.execute((Void) null);
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
			ActivityUtil.showProgress(getApplicationContext(), mRegStatusView, mRegFormView, false);

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
			ActivityUtil.showProgress(getApplicationContext(), mRegStatusView, mRegFormView, false);
		}
	}
}
