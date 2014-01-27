package com.vehicle.app.layout;

import com.vehicle.app.activities.ChatActivity;
import com.vehicle.app.activities.NearbyMainActivity;
import com.vehicle.app.activities.SettingActivity;

import cn.edu.sjtu.vehicleapp.R;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class BottomBarLayout extends LinearLayout implements View.OnClickListener{

	public BottomBarLayout(Context context) {
		super(context);
		create(context);
	}
	
	public BottomBarLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		create(context);
	}
	
	public BottomBarLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		create(context);
	}
	
	private void create(Context context)
	{
		View child = inflate(context, R.layout.layout_bottom_bar, null);
		
		this.addView(child);
		
		this.findViewById(R.id.bar_btn_message).setOnClickListener(this);
		this.findViewById(R.id.bar_btn_setting).setOnClickListener(this);
		this.findViewById(R.id.bar_btn_vendor).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		
		System.out.println("idddddddddddd:" + view.getId());
		
		Intent intent = new Intent();
		
		if(R.id.bar_btn_message == view.getId())
		{
			intent.setClass(getContext(), ChatActivity.class);
		}else if(R.id.bar_btn_setting == view.getId())
		{
			intent.setClass(getContext(), SettingActivity.class);
		}else if(R.id.bar_btn_vendor == view.getId())
		{
			intent.setClass(getContext(), NearbyMainActivity.class);
		}else
		{
			System.err.println("wrong id of button in the bottom bar");
		}
		
		this.getContext().startActivity(intent);
	}
}
