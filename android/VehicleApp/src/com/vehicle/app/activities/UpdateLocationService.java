package com.vehicle.app.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UpdateLocationService extends Service {
	private static Timer timer = new Timer();

	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		startService();
	}

	private void startService() {
		timer.scheduleAtFixedRate(new mainTask(), 0, 10000);
	}

	private class mainTask extends TimerTask {
		public void run() {
			try {
				System.out.println("in servicessssssssssssssss");
			} catch (Exception e) {

			}
		}
	}

	public void onDestroy() {
		super.onDestroy();
		if (null != timer) {
			try {
				timer.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
