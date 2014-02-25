package com.vehicle.app.mgrs;


import cn.edu.sjtu.vehicleapp.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class VendorBarLayout extends LinearLayout {

	public VendorBarLayout(Context context) {
		super(context);
		create(context, null);
	}

	public VendorBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		create(context, attrs);
	}

	public VendorBarLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		create(context, attrs);
	}

	private void create(Context context, AttributeSet attrs) {
		View child = inflate(context, R.layout.layout_vendor_bar, null);

		this.addView(child);
	}
}
