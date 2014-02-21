package com.vehicle.app.adapter;

import com.vehicle.app.fragments.ImagePageFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class VendorImagePageAdapter extends FragmentPagerAdapter{

	public VendorImagePageAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return new ImagePageFragment();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

}
