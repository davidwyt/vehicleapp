package com.vehicle.app.activities;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RoleSelectActivity extends Activity implements OnClickListener{

	private Button btnDriver;
	private Button btnShop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_roleselect);

		initView();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void initView() {
		this.btnDriver = (Button)this.findViewById(R.id.roleselect_driver);
		this.btnDriver.setOnClickListener(this);
		
		this.btnShop = (Button)this.findViewById(R.id.roleselect_shop);
		this.btnShop.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View view) {
		if(R.id.roleselect_driver == view.getId())
		{
			
		}else if(R.id.roleselect_shop == view.getId())
		{
			
		}
	}

}
