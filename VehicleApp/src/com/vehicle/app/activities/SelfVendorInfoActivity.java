package com.vehicle.app.activities;

import com.vehicle.app.bean.SelfVendor;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfVendorInfoActivity extends Activity {

	private Button mBtnBack;
	private ImageView mIvhead;
	private TextView mTvName;
	private TextView mTvAddr;
	private TextView mTvLevel;
	private TextView mTvPhoneNum;
	private TextView mTvMobileNum;
	private TextView mTvPoints;
	
	private SelfVendor mSelfVendor;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_selfvendor);

		initView();

	}

	private void initView() {
		this.mBtnBack = (Button) this.findViewById(R.id.selfvendor_goback);

		this.mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SelfVendorInfoActivity.this.onBackPressed();

			}
		});

		this.mIvhead = (ImageView) this.findViewById(R.id.selfvendor_icon);
		this.mTvName = (TextView) this.findViewById(R.id.selfvendor_name);
		this.mTvAddr = (TextView) this.findViewById(R.id.selfvendor_addr);
		this.mTvLevel = (TextView) this.findViewById(R.id.selfvendor_level);
		this.mTvPhoneNum = (TextView) this.findViewById(R.id.selfvendor_phonenum);
		this.mTvMobileNum = (TextView) this.findViewById(R.id.selfvendor_mobilenum);
		this.mTvPoints = (TextView) this.findViewById(R.id.selfvendor_points);
	}

	private void initData() {

	}

	@Override
	public void onStart() {
		super.onStart();
		initData();
	}

	@Override
	public void onStop() {
		super.onStop();
	}
}
