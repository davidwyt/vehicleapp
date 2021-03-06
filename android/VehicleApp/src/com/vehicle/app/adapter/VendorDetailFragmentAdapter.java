package com.vehicle.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.vehicle.app.bean.VendorDetail;
import com.vehicle.app.fragments.RefreshableFragment;
import com.vehicle.app.fragments.VendorCommentsFragment;
import com.vehicle.app.fragments.VendorPromotionsFragment;
import com.vehicle.app.fragments.VendorDetailFragment;
import com.vehicle.app.fragments.VendorCouponsFragment;
import com.viewpagerindicator.IconPagerAdapter;

public class VendorDetailFragmentAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {
	protected static final String[] CONTENT = new String[] { "商户介绍", "点评记录", "优惠券", "商户折扣", };

	private int mCount = CONTENT.length;
	private VendorDetail vendor;

	public VendorDetailFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public void setVendorDetail(VendorDetail detail) {
		this.vendor = detail;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
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
			ret = new VendorCouponsFragment(vendor);
			break;
		case 3:
			ret = new VendorPromotionsFragment(vendor);
			break;
		}
		return ret;
	}

	@Override
	public int getCount() {
		return mCount;
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
	public CharSequence getPageTitle(int position) {
		return VendorDetailFragmentAdapter.CONTENT[position % CONTENT.length];
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