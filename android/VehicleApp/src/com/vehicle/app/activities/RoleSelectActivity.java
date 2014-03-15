package com.vehicle.app.activities;

import com.vehicle.app.mgrs.SelfMgr;

import cn.edu.sjtu.vehicleapp.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class RoleSelectActivity extends TemplateActivity implements OnClickListener {

	private Button btnDriver;
	private Button btnShop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_roleselect);

		super.onCreate(savedInstanceState);

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
		this.btnDriver = (Button) this.findViewById(R.id.roleselect_driver);
		this.btnDriver.setOnClickListener(this);

		this.btnShop = (Button) this.findViewById(R.id.roleselect_shop);
		this.btnShop.setOnClickListener(this);

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
	public void onClick(View view) {

		if (R.id.roleselect_driver == view.getId()) {
			SelfMgr.getInstance().doLogout(this.getApplicationContext());
			SelfMgr.getInstance().setIsDriver(true);

			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), NearbyMainActivity.class);
			this.startActivity(intent);
		} else if (R.id.roleselect_shop == view.getId()) {
			SelfMgr.getInstance().doLogout(this.getApplicationContext());
			SelfMgr.getInstance().setIsDriver(false);

			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), LoginActivity.class);
			this.startActivity(intent);
		}
	}
}
