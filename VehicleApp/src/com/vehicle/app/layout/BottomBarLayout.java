package com.vehicle.app.layout;

import cn.edu.sjtu.vehicleapp.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class BottomBarLayout extends LinearLayout{

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
	}
}
