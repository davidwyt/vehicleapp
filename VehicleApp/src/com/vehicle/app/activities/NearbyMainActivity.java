package com.vehicle.app.activities;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class NearbyMainActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nearbymain);
		initView();
	}
	
	private void initView()
	{
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
}
