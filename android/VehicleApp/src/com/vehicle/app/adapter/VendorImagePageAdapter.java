package com.vehicle.app.adapter;

import java.util.List;

import com.vehicle.app.bean.VendorImage;
import com.vehicle.app.fragments.ImagePageFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class VendorImagePageAdapter extends FragmentStatePagerAdapter {

	List<VendorImage> imgs;

	public VendorImagePageAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public void setImgs(List<VendorImage> imgs) {
		System.out.println("imgs:" + (null == imgs ? "null" : ("not null : " + imgs.size())));
		this.imgs = imgs;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public Fragment getItem(int pos) {
		// TODO Auto-generated method stub
		System.out.println("in get item:" + pos);
		
		return null == imgs ? null : new ImagePageFragment(imgs.get(pos).getSrc());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == imgs ? 0 : imgs.size();
	}

}
