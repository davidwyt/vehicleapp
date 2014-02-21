package com.vehicle.app.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.StringUtil;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.content.Intent;
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

			List<String> ids = new ArrayList<String>();

			if (SelfMgr.getInstance().isDriver()) {
				Collection<Vendor> vendors = SelfMgr.getInstance().getFavVendorDetailMap().values();
				for (Vendor vendor : vendors) {
					ids.add(vendor.getId());
				}

			} else {
				Collection<Driver> drivers = SelfMgr.getInstance().getVendorFellowDetailMap().values();
				for (Driver driver : drivers) {
					ids.add(driver.getId());
				}
			}
			
			Intent intent = new Intent(this, ChatActivity.class);
			intent.putExtra(ChatActivity.KEY_FELLOWID, StringUtil.JointString(ids, Constants.COMMA));
			intent.putExtra(ChatActivity.KEY_CHATSTYLE, ChatActivity.CHAT_STYLE_2ONE);
			this.startActivity(intent);
		} else if (R.id.nav_allnearby == view.getId()) {

			List<String> ids = new ArrayList<String>();

			if (SelfMgr.getInstance().isDriver()) {
				Collection<Vendor> vendors = SelfMgr.getInstance().getNearbyVendors();

				for (Vendor vendor : vendors) {
					ids.add(vendor.getId());
				}

			} else {
				Collection<Driver> drivers = SelfMgr.getInstance().getNearbyDrivers();
				for (Driver driver : drivers) {
					ids.add(driver.getId());
				}
			}

			Intent intent = new Intent(this, ChatActivity.class);
			intent.putExtra(ChatActivity.KEY_FELLOWID, StringUtil.JointString(ids, Constants.COMMA));
			intent.putExtra(ChatActivity.KEY_CHATSTYLE, ChatActivity.CHAT_STYLE_2ONE);
			this.startActivity(intent);
		} else {
			System.err.println("not the valid button id in the groupmsgNav Form");
		}
	}
}
