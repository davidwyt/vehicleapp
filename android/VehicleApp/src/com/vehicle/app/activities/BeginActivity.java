package com.vehicle.app.activities;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.sdk.client.VehicleClient;

import cn.edu.sjtu.vehicleapp.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

public class BeginActivity extends TemplateActivity {

	private View mBaK;

	private static final int DELAY_FADE = 5 * 1000;

	public static final String KEY_AUDOLOGIN = "com.vehicle.app.begin.key.autolog";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_begin);

		this.mBaK = this.findViewById(R.id.activity_begin);
	}

	@Override
	protected void onStart() {
		super.onStart();

		this.mBaK.setVisibility(View.VISIBLE);

		startUpdateLocation();

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				try {
					fade();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		timer.schedule(task, DELAY_FADE);

		SelfMgr.getInstance().doLogout(getApplicationContext());
	}

	LocationManagerProxy mAMapLocationManager = null;
	AMapLocationListener myListener = new MyLocationListener();

	private void startUpdateLocation() {

		if (null != this.mAMapLocationManager) {
			try {
				mAMapLocationManager.removeUpdates(myListener);
				mAMapLocationManager.destory();
				mAMapLocationManager = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("thread in ..............");

		try {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			this.mAMapLocationManager.setGpsEnable(true);
			mAMapLocationManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 5000, 10, myListener);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class UpdateLocTask extends AsyncTask<Void, Void, Void> {

		private double lnt;
		private double lat;

		public UpdateLocTask(double lnt, double lat) {
			this.lnt = lnt;
			this.lat = lat;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				VehicleClient client = new VehicleClient(SelfMgr.getInstance().getId());
				client.UpdateLocation(SelfMgr.getInstance().getId(), lnt, lat);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private class MyLocationListener implements AMapLocationListener {

		long lastUpdateTime = 0;
		private UpdateLocTask updateLocTask = null;
		double lastLat = 0.0d;
		double lastLnt = 0.0d;

		private void updateLocation(Location loc) {
			try {

				final double lat;
				final double lnt;
				if (null == loc) {
					return;
				} else {
					lat = loc.getLatitude();
					lnt = loc.getLongitude();
				}

				System.out.println("location " + lat + " " + lnt);

				SelfMgr.getInstance().updateLocation(lat, lnt);

				if ((!SelfMgr.getInstance().isLogin() && SelfMgr.getInstance().isDriver())
						|| !SelfMgr.getInstance().isDriver()) {
					System.out.println(" not login");
					return;
				}

				long cur = new Date().getTime();

				if (cur - lastUpdateTime < 30 * 1000) {
					return;
				}

				if (Math.abs(lastLat - lat) < 0.000001 && Math.abs(lastLnt - lnt) < 0.000001
						&& cur - lastUpdateTime < 300 * 1000) {
					return;
				}

				updateLocTask = new UpdateLocTask(lnt, lat);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					updateLocTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					updateLocTask.execute();
				}

				this.lastUpdateTime = cur;
				this.lastLat = lat;
				this.lastLnt = lnt;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onLocationChanged(Location loc) {
			// TODO Auto-generated method stub
			updateLocation(loc);
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(AMapLocation loc) {
			// TODO Auto-generated method stub
			updateLocation(loc);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void fade() {
		this.mBaK.setVisibility(View.VISIBLE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			this.mBaK.animate().setDuration(shortAnimTime).alpha(1).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mBaK.setVisibility(View.GONE);
				}
			});
		} else {
			this.mBaK.setVisibility(View.GONE);
		}

		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), RoleSelectActivity.class);
		System.out.println("action:" + RoleSelectActivity.class.getCanonicalName());
		BeginActivity.this.startActivity(intent);
		BeginActivity.this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
