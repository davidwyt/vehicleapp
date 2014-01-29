package com.vehicle.app.activities;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class GroupmsgNavActivity extends Activity implements OnClickListener {

	private Button mAllFellows;
	private Button mNearby;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_groupmsgnav);

		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		this.mAllFellows = (Button) this.findViewById(R.id.nav_allfellows);
		this.mAllFellows.setOnClickListener(this);

		this.mNearby = (Button) this.findViewById(R.id.nav_allnearby);
		this.mNearby.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View view) {
		if (R.id.nav_allfellows == view.getId()) {

		} else if (R.id.nav_allnearby == view.getId()) {

		} else {
			System.err.println("not the valid button id in the groupmsgNav Form");
		}
	}

}
