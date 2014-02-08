package com.vehicle.app.activities;

import java.util.Timer;
import java.util.TimerTask;

import cn.edu.sjtu.vehicleapp.R;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class BeginActivity extends Activity {

	private View mBaK;

	private static final int DELAY_FADE = 4 * 1000;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_begin);

		this.mBaK = this.findViewById(R.id.activity_begin);
		
		/**
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
		builder.statusBarDrawable = R.drawable.jpush_notification_icon;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;
		builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;
		JPushInterface.setPushNotificationBuilder(1, builder);
		*/
	}

	@Override
	protected void onStart() {
		super.onStart();

		this.mBaK.setVisibility(View.VISIBLE);

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				fade();
			}
		};

		timer.schedule(task, DELAY_FADE);
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
		BeginActivity.this.startActivity(intent);

		BeginActivity.this.finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
