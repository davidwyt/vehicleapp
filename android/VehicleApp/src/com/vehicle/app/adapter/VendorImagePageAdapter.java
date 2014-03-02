package com.vehicle.app.adapter;

import java.util.List;

import com.vehicle.app.bean.VendorImage;
import com.vehicle.app.fragments.ImagePageFragment;
import com.vehicle.app.fragments.RefreshableFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class VendorImagePageAdapter extends FragmentStatePagerAdapter {

	List<VendorImage> imgs;

	public VendorImagePageAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public void setImgs(List<VendorImage> imgs) {
		this.imgs = imgs;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		Object obj = super.instantiateItem(container, position);
		if (obj instanceof RefreshableFragment) {
			RefreshableFragment fragment = (RefreshableFragment) obj;
			fragment.refresh();
		}

		return obj;
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
