package com.vehicle.app.adapter;

import java.util.List;

import com.vehicle.app.bean.VendorImage;
import com.vehicle.app.fragments.ImagePageFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class VendorImagePageAdapter extends FragmentPagerAdapter {

	List<VendorImage> imgs;

	public VendorImagePageAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public void setImgs(List<VendorImage> imgs) {
		this.imgs = imgs;
	}

	@Override
	public Fragment getItem(int pos) {
		// TODO Auto-generated method stub
		return null == imgs ? null : new ImagePageFragment(imgs.get(pos).getSrc());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == imgs ? 0 : imgs.size();
	}

}