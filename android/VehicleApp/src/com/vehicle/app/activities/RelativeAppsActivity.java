package com.vehicle.app.activities;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RelativeAppsActivity extends Activity implements OnClickListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_moreapp);

		Button bak = (Button) this.findViewById(R.id.moreapp_btn_back);
		bak.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RelativeAppsActivity.this.onBackPressed();
				finish();
			}
		});

		this.findViewById(R.id.moreapp_gas).setOnClickListener(this);
		this.findViewById(R.id.moreapp_jy).setOnClickListener(this);
		this.findViewById(R.id.moreapp_lt).setOnClickListener(this);
		this.findViewById(R.id.moreapp_thisicon).setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.moreapp_gas:
			openApp();
			break;
		case R.id.moreapp_jy:
			openApp();
			break;
		case R.id.moreapp_lt:
			openApp();
			break;
		case R.id.moreapp_thisicon:
			openApp();
			break;
		}
	}

	private void openApp() {
		Uri uri = Uri.parse("http://ac0086.com/app");
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(it);
	}
}
