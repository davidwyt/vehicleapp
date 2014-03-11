package com.vehicle.app.activities;

import java.util.Timer;
import java.util.TimerTask;

import com.vehicle.app.mgrs.SelfMgr;

import cn.edu.sjtu.vehicleapp.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

public class BeginActivity extends TemplateActivity {

	private View mBaK;

	private static final int DELAY_FADE = 4 * 1000;

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
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
