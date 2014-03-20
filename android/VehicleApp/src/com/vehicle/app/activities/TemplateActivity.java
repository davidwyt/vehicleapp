package com.vehicle.app.activities;

import java.util.List;

import com.baidu.mobstat.StatService;
import com.vehicle.app.mgrs.ActivityManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.utils.AuthUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

public class TemplateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ActivityManager.getInstance().pushActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}

	@Override
	public void finish() {
		try {
			super.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ActivityManager.getInstance().popActivity(this);
	}

	@Override
	public void startActivity(Intent intent) {
		List<ResolveInfo> activities = this.getPackageManager().queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		if (null != activities && activities.size() > 0) {
			String oper = activities.get(0).activityInfo.name;
			if (SelfMgr.getInstance().isDriver() && !SelfMgr.getInstance().isLogin() && AuthUtil.isReqAuth(oper)) {
				SelfMgr.getInstance().clearAll();
				Intent logIntent = new Intent(this.getApplicationContext(), LoginActivity.class);
				super.startActivity(logIntent);
				return;
			}
		}

		super.startActivity(intent);
	}
}
