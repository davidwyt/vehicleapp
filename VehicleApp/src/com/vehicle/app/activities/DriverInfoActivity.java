package com.vehicle.app.activities;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DriverInfoActivity extends Activity{
	
	private Button mBtnBack;
	private ImageView mIvHead;
	private TextView mTvName;
	private TextView mTvAge;
	private TextView mTvSex;
	private TextView mTvPerInfo;
	private TextView mTvCarInfo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_driverinfo);
		initView();
		initData();
	}

	private void initView() {
		this.mBtnBack = (Button)this.findViewById(R.id.driverinfo_goback);
		this.mBtnBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				DriverInfoActivity.this.onBackPressed();
			}});
		
		this.mIvHead = (ImageView)this.findViewById(R.id.driverinfo_icon);
		this.mTvName = (TextView)this.findViewById(R.id.driverinfo_name);
		this.mTvAge = (TextView)this.findViewById(R.id.driverinfo_age);
		this.mTvSex = (TextView)this.findViewById(R.id.driverinfo_sex);
		this.mTvPerInfo = (TextView)this.findViewById(R.id.userinfo_driverinfo);
		this.mTvCarInfo = (TextView)this.findViewById(R.id.userinfo_carinfo);
	}
	
	private void initData(){
		
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
}
