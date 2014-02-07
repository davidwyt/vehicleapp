package com.vehicle.app.activities;

import com.vehicle.app.mgrs.SelfMgr;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class NearbyMainActivity extends Activity implements OnCheckedChangeListener {

	private RadioGroup mRdGroup;
	private Button mButtonNearby;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nearbymain);
		initView();
	}

	private void initView() {
		this.mRdGroup = ((RadioGroup) this.findViewById(R.id.bottom_rdgroup));
		this.mRdGroup.setOnCheckedChangeListener(this);
		
		this.mButtonNearby = (Button)this.findViewById(R.id.nearbymain_roundnearby);
		
		Button btnMiddle = (Button)this.findViewById(R.id.bar_rabtn_middle);
		
		if(SelfMgr.getInstance().getIsDriver())
		{
			this.mButtonNearby.setBackgroundResource(R.drawable.selector_button_roundnearbyshops);
			btnMiddle.setBackgroundResource(R.drawable.selector_button_bottomdriver);
		}else
		{
			this.mButtonNearby.setBackgroundResource(R.drawable.selector_button_roundnearbydrivers);
			btnMiddle.setBackgroundResource(R.drawable.selector_button_bottomvendor);
		}
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		((RadioButton)this.findViewById(R.id.bar_rabtn_middle)).setChecked(true);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		
		if (R.id.bar_rabtn_message == checkedId) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), RecentContactListActivity.class);
			this.startActivity(intent);
		} else if (R.id.bar_rabtn_setting == checkedId) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), SettingActivity.class);
			this.startActivity(intent);
		}

	}
}
