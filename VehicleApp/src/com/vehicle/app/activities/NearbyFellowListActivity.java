package com.vehicle.app.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vehicle.app.adapter.NearbyFellowsViewAdapter;
import com.vehicle.app.bean.User;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class NearbyFellowListActivity extends Activity{

	private ListView mLVFellows;

	private BaseAdapter mAdapter;

	private List<User> mListFellows = new ArrayList<User>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_nearbyfellowlist);
		
		initView();
	}
	
	private void initView()
	{
		mLVFellows = (ListView) this.findViewById(R.id.nearbyfellows);

		List<User> users = new ArrayList<User>();
		for (int i = 0; i < 10; i++) {
			User user = new User();
			user.setAlias("user" + i);
			user.setId(i + "");
			user.setIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.chat_info));
			user.setLastMessage("this is my last messagesssssssssssssssssss");
			user.setLastMessageDate(new Date());

			users.add(user);

			mListFellows.add(user);
		}

		this.mAdapter = new NearbyFellowsViewAdapter(this, users);
		this.mLVFellows.setAdapter(mAdapter);
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
