package com.vehicle.app.activities;

import com.vehicle.app.mgrs.ActivityManager;

import android.app.Activity;
import android.os.Bundle;

public class TemplateActivity extends Activity {

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
