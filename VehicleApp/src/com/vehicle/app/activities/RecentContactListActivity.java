package com.vehicle.app.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vehicle.app.adapter.UserViewAdapter;
import com.vehicle.app.bean.User;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RecentContactListActivity extends Activity implements OnCheckedChangeListener {

	private ListView lvUser;

	private BaseAdapter adapter;

	private List<User> listUser = new ArrayList<User>();

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

		((RadioButton)this.findViewById(R.id.bar_rabtn_message)).setChecked(true);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void initView() {
		lvUser = (ListView) this.findViewById(R.id.list_users);

		List<User> users = new ArrayList<User>();
		for (int i = 0; i < 10; i++) {
			User user = new User();
			user.setAlias("user" + i);
			user.setId(i + "");
			user.setIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.chat_info));
			user.setLastMessage("this is my last messagesssssssssssssssssss");
			user.setLastMessageDate(new Date());

			users.add(user);

			listUser.add(user);
		}

		this.adapter = new UserViewAdapter(this, users);
		this.lvUser.setAdapter(adapter);

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
		} else if (R.id.bar_rabtn_vendor == checkedId) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), NearbyMainActivity.class);
			this.startActivity(intent);
		}
	}
}
