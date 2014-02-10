package com.vehicle.app.activities;

import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.StringUtil;

import cn.edu.sjtu.vehicleapp.R;
import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class SettingActivity extends Activity{
	
	private EditText selfIdET;
	private EditText herIdET;
	private EditText serverET;
	private TextView tip;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_setting);
		
		initView();
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		this.serverET.setText(Constants.SERVERURL);
	}
	
	private void initView()
	{
		//((RadioButton)this.findViewById(R.id.bar_rabtn_setting)).setChecked(true);
		
		this.selfIdET = (EditText)this.findViewById(R.id.setting_selfID);
		this.herIdET = (EditText)this.findViewById(R.id.setting_herID);
		this.serverET = (EditText)this.findViewById(R.id.setting_serverurl);
		this.tip = (TextView)this.findViewById(R.id.setting_tip);
		
		Button btn = (Button)this.findViewById(R.id.setting_ok);
		btn.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View btn) {
				// TODO Auto-generated method stub
				String selfId = selfIdET.getText().toString();
				String herId = herIdET.getText().toString();
				String server = serverET.getText().toString();
				if(StringUtil.IsNullOrEmpty(selfId) || StringUtil.IsNullOrEmpty(herId) || StringUtil.IsNullOrEmpty(server))
				{
					tip.setText("your id/her id/serverurl can't be null");
					return;
				}
				
				Constants.SERVERURL = server;
				
				tip.setText("setting success");
			}});
	}
}
