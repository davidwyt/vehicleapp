package com.vehicle.app.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vehicle.app.adapter.UserViewAdapter;
import com.vehicle.app.bean.User;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

public class UserListActivity extends Activity {

	private ListView lvUser;
	//private Button btnMessage;
	//private Button btnVendor;
	//private Button btnSetting;

	private BaseAdapter adapter;
	
	private List<User> listUser = new ArrayList<User>();
	
	@Override
	protected void onCreate(Bundle bundle) {

		super.onCreate(bundle);
		setContentView(R.layout.activity_userlist);
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
		lvUser = (ListView) this.findViewById(R.id.list_users);
		//btnMessage = (Button) this.findViewById(R.id.bar_btn_message);
		//btnVendor = (Button) this.findViewById(R.id.bar_btn_vendor);
		//btnSetting = (Button) this.findViewById(R.id.bar_btn_setting);

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
	}
}
