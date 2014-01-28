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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class BottomBarLayout extends LinearLayout implements OnCheckedChangeListener{
	
	private RadioGroup mBottomRdGroup;
	
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
		
		this.mBottomRdGroup = (RadioGroup)this.findViewById(R.id.bottom_rdgroup);
		this.mBottomRdGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		int id = group.getCheckedRadioButtonId();
		
		Intent intent = new Intent();
		
		if(R.id.bar_rabtn_message == id)
		{
			intent.setClass(getContext(), ChatActivity.class);
		}else if(R.id.bar_rabtn_setting == id)
		{
			intent.setClass(getContext(), SettingActivity.class);
		}else if(R.id.bar_rabtn_vendor == id)
		{
			intent.setClass(getContext(), NearbyMainActivity.class);
		}else
		{
			System.err.println("wrong id of button in the bottom bar");
		}
		
		this.getContext().startActivity(intent);
		
	}
}
