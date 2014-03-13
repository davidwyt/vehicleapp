package com.vehicle.app.activities;

import cn.edu.sjtu.vehicleapp.R;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UpgradeActivity extends TemplateActivity implements OnClickListener {

	private Button mBtnUpgrade;
	private Button mBtnCancel;
	private Button mBtnInstanll;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_upgrade);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
