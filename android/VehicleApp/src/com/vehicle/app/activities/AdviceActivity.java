package com.vehicle.app.activities;

import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.utils.StringUtil;
import com.vehicle.app.web.bean.WebCallBaseResult;
import com.vehicle.sdk.client.VehicleWebClient;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdviceActivity extends Activity {

	private EditText mETPhone;
	private EditText mETEmail;
	private EditText mETAdvice;
	private Button mBtnOK;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_advice);

		Button bak = (Button) this.findViewById(R.id.advice_btn_back);
		bak.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AdviceActivity.this.onBackPressed();
			}
		});

		this.mETPhone = (EditText) this.findViewById(R.id.advice_phone);
		this.mETEmail = (EditText) this.findViewById(R.id.advice_email);
		this.mETAdvice = (EditText) this.findViewById(R.id.advice_text);

		this.mBtnOK = (Button) this.findViewById(R.id.advice_ok);
		this.mBtnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendAdvice();
			}
		});
	}

	private void sendAdvice() {

		final String content = this.mETAdvice.getText().toString();
		final String email = this.mETEmail.getText().toString();
		final String phone = this.mETPhone.getText().toString();

		if (null == content || content.length() < 10) {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_contentnull),
					Toast.LENGTH_LONG).show();
			return;
		}

		if (null == email || email.isEmpty()) {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_emailnull), Toast.LENGTH_LONG)
					.show();

			return;
		}

		if (!StringUtil.IsEmail(email)) {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_emailinvalid),
					Toast.LENGTH_LONG).show();
			return;
		}

		if (null == phone || phone.isEmpty()) {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_phonenull), Toast.LENGTH_LONG)
					.show();
			return;
		}

		AsyncTask<Void, Void, WebCallBaseResult> asyncTask = new AsyncTask<Void, Void, WebCallBaseResult>() {

			@Override
			protected WebCallBaseResult doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				WebCallBaseResult result = null;

				try {
					VehicleWebClient webClient = new VehicleWebClient();
					result = webClient.CreateAdvice(SelfMgr.getInstance().getId(), email, phone, content);
				} catch (Exception e) {
					e.printStackTrace();
				}

				return result;
			}

			@Override
			protected void onPostExecute(WebCallBaseResult result) {
				if (null != result && result.isSuccess()) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_advicesuccess),
							Toast.LENGTH_LONG).show();
				} else {
					if (null != result) {
						Toast.makeText(getApplicationContext(),
								getResources().getString(R.string.tip_advicefailed, result.getMessage()),
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_advicefailed),
								Toast.LENGTH_LONG).show();
					}
				}
			}
		};

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			asyncTask.execute();
		}
	}
}
