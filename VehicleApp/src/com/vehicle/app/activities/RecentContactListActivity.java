package com.vehicle.app.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vehicle.app.adapter.UserViewAdapter;
import com.vehicle.app.bean.Driver;
import com.vehicle.app.mgrs.SelfMgr;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RecentContactListActivity extends Activity implements OnCheckedChangeListener {

	private ListView mLVUsers;

	private BaseAdapter mAdapter;

	private List<Driver> mListUser = new ArrayList<Driver>();

	private RadioGroup mRdGroup;

	@Override
	protected void onCreate(Bundle bundle) {

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(bundle);
		setContentView(R.layout.activity_recentcontactlist);
		initView();
	}

	@Override
	protected void onStart() {
		super.onStart();

		((RadioButton) this.findViewById(R.id.bar_rabtn_message)).setChecked(true);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void initView() {

		for (int i = 0; i < 10; i++) {
			Driver user = new Driver();
			user.setAlias("user" + i);
			user.setId(SelfMgr.getInstance().getSelfDriver().getId());
			user.setIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.chat_info));
			user.setLastMessage("this is my last messagesssssssssssssssssss");
			user.setLastMessageDate(new Date());

			mListUser.add(user);
		}

		this.mAdapter = new UserViewAdapter(this, mListUser);

		this.mLVUsers = (ListView) this.findViewById(R.id.list_users);
		this.mLVUsers.setAdapter(this.mAdapter);
		this.mLVUsers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Driver user = (Driver) mAdapter.getItem(position);
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ChatActivity.class);
				intent.putExtra("com.vehicle.app.activities.fellowId", user.getId());

				RecentContactListActivity.this.startActivity(intent);
			}
		});

		this.mRdGroup = (RadioGroup) this.findViewById(R.id.bottom_rdgroup);
		this.mRdGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if (R.id.bar_rabtn_setting == checkedId) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), SettingActivity.class);
			this.startActivity(intent);
		} else if (R.id.bar_rabtn_middle == checkedId) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), NearbyMainActivity.class);
			this.startActivity(intent);
		}
	}
}
