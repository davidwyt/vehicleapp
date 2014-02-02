package com.vehicle.app.activities;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FollowshipInvitationActivity extends Activity implements OnClickListener {

	private Button mBtnShopDetail;
	private Button mBtnAccept;
	private Button mBtnRejected;
	private Button mBtnBak;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_followshipinvitation);
		initView();
	}

	private void initView() {
		this.mBtnShopDetail = (Button) this.findViewById(R.id.followshipinvitation_shopdetail);
		this.mBtnShopDetail.setOnClickListener(this);
		
		this.mBtnAccept = (Button) this.findViewById(R.id.followshipinvitation_accept);
		this.mBtnAccept.setOnClickListener(this);
		
		this.mBtnRejected = (Button) this.findViewById(R.id.followshipinvitation_reject);
		this.mBtnRejected.setOnClickListener(this);
		
		this.mBtnBak = (Button)this.findViewById(R.id.invitation_goback);
		this.mBtnBak.setOnClickListener(this);
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

		}else if(R.id.invitation_goback == view.getId())
		{
			this.onBackPressed();
		}else
		{
			System.err.println("invalid id of clicked button in followshipinvitation form");
		}
	}
}
