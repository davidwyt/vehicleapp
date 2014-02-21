package com.vehicle.app.fragments;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.BitmapCache;
import com.vehicle.app.msg.worker.ImageViewBitmapLoader;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public final class VendorDetailFragment extends Fragment {

	private Vendor vendor;

	public VendorDetailFragment(Vendor vendor) {
		this.vendor = vendor;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View parent = inflater.inflate(R.layout.layout_vendorinfo, container, false);

		ImageView ivHead = (ImageView) parent.findViewById(R.id.vendor_icon);

		String url = vendor.getAvatar();
		Bitmap bitmap = BitmapCache.getInstance().get(url);

		if (null != bitmap) {
			ivHead.setImageBitmap(bitmap);
		} else {
			ivHead.setTag(R.id.TAGKEY_BITMAP_URL, url);
			ImageViewBitmapLoader loader = new ImageViewBitmapLoader(ivHead);
			loader.load();
		}

		TextView tvName = (TextView) parent.findViewById(R.id.vendor_name);
		tvName.setText(vendor.getName());

		TextView tvBusinessTime = (TextView) parent.findViewById(R.id.vendor_businesstime);
		tvBusinessTime.setText(vendor.getAnswerTime());

		RatingBar rbScore = (RatingBar) parent.findViewById(R.id.ratingbar_score);
		rbScore.setRating((float) vendor.getScore());

		TextView tvReception = (TextView) parent.findViewById(R.id.vendor_reception);
		tvReception.setText(vendor.getReceptionScore() + "");

		TextView tvEnv = (TextView) parent.findViewById(R.id.vendor_environment);
		tvEnv.setText(vendor.getEnvironmentScore() + "");

		TextView tvEffic = (TextView) parent.findViewById(R.id.vendor_efficiency);
		tvEffic.setText(vendor.getEfficiencyScore() + "");

		TextView tvTech = (TextView) parent.findViewById(R.id.vendor_technology);
		tvTech.setText(vendor.getTechnologyScore() + "");

		TextView tvPrice = (TextView) parent.findViewById(R.id.vendor_price);
		tvPrice.setText(vendor.getPriceScore() + "");

		TextView tvMobile = (TextView) parent.findViewById(R.id.vendor_mobilenum);
		tvMobile.setText(vendor.getMobile());

		TextView tvAddr = (TextView) parent.findViewById(R.id.vendor_address);
		tvAddr.setText(vendor.getAddress());

		TextView tvIntro = (TextView) parent.findViewById(R.id.vendor_introduction);
		tvIntro.setText(vendor.getIntroduction());

		return parent;
	}
}
