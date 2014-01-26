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

public class ShopInfoActivity extends Activity {

	private Button btnBack;
	private ImageView ivhead;
	private TextView tvName;
	private TextView tvAddr;
	private TextView tvLevel;
	private TextView tvPhoneNum;
	private TextView tvMobileNum;
	private TextView tvPoints;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setTitle("Setting");

		setContentView(R.layout.activity_shopinfo);

		initView();
		
		initData();
	}

	private void initView() {
		this.btnBack = (Button) this.findViewById(R.id.shopinfo_goback);

		this.btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShopInfoActivity.this.onBackPressed();

			}
		});
		
		this.ivhead = (ImageView) this.findViewById(R.id.shopinfo_icon);
		this.tvName = (TextView) this.findViewById(R.id.shopinfo_name);
		this.tvAddr = (TextView) this.findViewById(R.id.shopinfo_addr);
		this.tvLevel = (TextView) this.findViewById(R.id.shopinfo_level);
		this.tvPhoneNum = (TextView) this.findViewById(R.id.shopinfo_phonenum);
		this.tvMobileNum = (TextView) this.findViewById(R.id.shopinfo_mobilenum);
		this.tvPoints = (TextView) this.findViewById(R.id.shopinfo_points);
	}
	
	private void initData()
	{
		
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}
}
