package com.vehicle.app.layout;

import cn.edu.sjtu.vehicleapp.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class VendorLongBarLayout extends LinearLayout {

	public VendorLongBarLayout(Context context) {
		super(context);
		create(context, null);
	}

	public VendorLongBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		create(context, attrs);
	}

	public VendorLongBarLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		create(context, attrs);
	}

	private void create(Context context, AttributeSet attrs) {
		View child = inflate(context, R.layout.layout_vendor_longbar, null);

		this.addView(child);
	}
}
