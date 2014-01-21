package com.vehicle.app.activities;

import com.vehicle.app.utils.StringUtil;
import com.vehicle.sdk.client.VehicleWebClient;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity {

	private EditText etEmail;
	private EditText etUserName;
	private EditText etPwd;
	private EditText etCfmPwd;
	private TextView tvTip;
	private Button btnOK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		this.etEmail = (EditText) this.findViewById(R.id.reg_email);
		this.etUserName = (EditText) this.findViewById(R.id.reg_username);
		this.etPwd = (EditText) this.findViewById(R.id.reg_pwd);
		this.etCfmPwd = (EditText) this.findViewById(R.id.reg_cfmpwd);
		this.tvTip = (TextView) this.findViewById(R.id.reg_tip);

		this.btnOK = (Button) this.findViewById(R.id.reg_ok);
		this.btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				String strEmail = RegisterActivity.this.etEmail.getText().toString();
				String strUserName = RegisterActivity.this.etUserName.getText().toString();
				String strPwd = RegisterActivity.this.etPwd.getText().toString();
				String strCfmPwd = RegisterActivity.this.etCfmPwd.getText().toString();

				if (StringUtil.IsNullOrEmpty(strEmail) || StringUtil.IsNullOrEmpty(strUserName)
						|| StringUtil.IsNullOrEmpty(strPwd) || StringUtil.IsNullOrEmpty(strCfmPwd)) {
					
					String strTip = RegisterActivity.this.getResources().getString(R.string.reg_tip_null);
					RegisterActivity.this.tvTip.setText(strTip);

					return;
				}
				
				if(!StringUtil.IsEmail(strEmail))
				{
					String strTip = RegisterActivity.this.getResources().getString(R.string.reg_tip_invalidemail);
					RegisterActivity.this.tvTip.setText(strTip);

					return;
				}

				if (!(strPwd.length() >= 6 && strPwd.length() <= 20)
						|| !(strCfmPwd.length() >= 6 && strCfmPwd.length() <= 20)) {
					String strTip = RegisterActivity.this.getResources().getString(R.string.reg_tip_invalidpwd);
					RegisterActivity.this.tvTip.setText(strTip);

					return;
				}

				if (!strPwd.equals(strCfmPwd)) {
					String strTip = RegisterActivity.this.getResources().getString(R.string.reg_tip_notequalpwd);
					RegisterActivity.this.tvTip.setText(strTip);

					return;
				}
				
				VehicleWebClient client = new VehicleWebClient();
				client.Register();
			}

		});
	}
}
