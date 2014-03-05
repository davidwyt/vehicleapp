package com.vehicle.app.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.vehicle.app.mgrs.ActivityManager;

public class FragmentTemplateActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().pushActivity(this);
	}

	@Override
	public void finish() {
		super.finish();
		ActivityManager.getInstance().popActivity(this);
	}
}
