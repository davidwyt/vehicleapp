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
	
	private Button btnBack;
	private ImageView ivHead;
	private TextView tvName;
	private TextView tvAge;
	private TextView tvSex;
	private TextView tvPerInfo;
	private TextView tvCarInfo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		initView();
		initData();
	}

	private void initView() {
		this.btnBack = (Button)this.findViewById(R.id.userinfo_goback);
		this.btnBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				DriverInfoActivity.this.onBackPressed();
			}});
		
		this.ivHead = (ImageView)this.findViewById(R.id.userinfo_icon);
		this.tvName = (TextView)this.findViewById(R.id.userinfo_name);
		this.tvAge = (TextView)this.findViewById(R.id.userinfo_age);
		this.tvSex = (TextView)this.findViewById(R.id.userinfo_sex);
		this.tvPerInfo = (TextView)this.findViewById(R.id.userinfo_driverinfo);
		this.tvCarInfo = (TextView)this.findViewById(R.id.userinfo_carinfo);
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
