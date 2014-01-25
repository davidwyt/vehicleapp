package com.vehicle.app.activities;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FollowshipInvitationActivity extends Activity implements OnClickListener {

	private Button btnShopDetail;
	private Button btnAccept;
	private Button btnRejected;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_followshipinvitation);
		initView();
	}

	private void initView() {
		this.btnShopDetail = (Button) this.findViewById(R.id.followshipinvitation_shopdetail);
		this.btnShopDetail.setOnClickListener(this);
		
		this.btnAccept = (Button) this.findViewById(R.id.followshipinvitation_accept);
		this.btnAccept.setOnClickListener(this);
		
		this.btnRejected = (Button) this.findViewById(R.id.followshipinvitation_reject);
		this.btnRejected.setOnClickListener(this);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View view) {
		if (R.id.followshipinvitation_shopdetail == view.getId()) {

		} else if (R.id.followshipinvitation_accept == view.getId()) {

		} else if (R.id.followshipinvitation_reject == view.getId()) {

		}
	}
}
