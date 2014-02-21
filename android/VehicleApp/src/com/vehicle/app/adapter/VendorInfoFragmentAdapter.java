package com.vehicle.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vehicle.app.bean.Vendor;
import com.vehicle.app.fragments.VendorCommentsFragment;
import com.vehicle.app.fragments.VendorCouponFragment;
import com.vehicle.app.fragments.VendorDetailFragment;
import com.vehicle.app.fragments.VendorPromotionFragment;
import com.viewpagerindicator.IconPagerAdapter;

public class VendorInfoFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
	protected static final String[] CONTENT = new String[] { "商家介绍", "点评记录", "优惠券", "商家折扣", };

	private int mCount = CONTENT.length;
	private Vendor vendor;

	public VendorInfoFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {

		Fragment ret = null;
		
		switch (position) {
		case 0:
			ret = new VendorDetailFragment(vendor);
			break;
		case 1:
			ret = new VendorCommentsFragment(vendor);
			break;
		case 2:
			ret = new VendorCouponFragment(vendor);
			break;
		case 3:
			ret = new VendorPromotionFragment(vendor);
			break;
		}

		return ret;
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return VendorInfoFragmentAdapter.CONTENT[position % CONTENT.length];
	}

	@Override
	public int getIconResId(int index) {
		return 0;
	}

	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}
}