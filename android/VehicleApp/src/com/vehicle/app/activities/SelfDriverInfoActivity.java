package com.vehicle.app.activities;

import com.vehicle.app.bean.SelfDriver;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfDriverInfoActivity extends Activity {

	private Button mBtnBack;
	private ImageView mIvHead;
	private TextView mTvName;
	private TextView mTvAge;
	private TextView mTvSex;
	private TextView mTvPerInfo;
	private TextView mTvCarInfo;

	private SelfDriver mSelfDriver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_selfdriverinfo);
		initView();
		initData();
	}

	private void initView() {
		this.mBtnBack = (Button) this.findViewById(R.id.selfdriverinfo_goback);
		this.mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SelfDriverInfoActivity.this.onBackPressed();
			}
		});

		this.mIvHead = (ImageView) this.findViewById(R.id.selfdriverinfo_icon);
		this.mTvName = (TextView) this.findViewById(R.id.selfdriverinfo_name);
		this.mTvAge = (TextView) this.findViewById(R.id.selfdriverinfo_age);
		this.mTvSex = (TextView) this.findViewById(R.id.selfdriverinfo_sex);
		this.mTvPerInfo = (TextView) this.findViewById(R.id.selfdriver_driverinfo);
		this.mTvCarInfo = (TextView) this.findViewById(R.id.selfdriver_carinfo);
	}

	private void initData() {

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
