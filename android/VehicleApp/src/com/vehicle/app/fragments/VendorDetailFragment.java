package com.vehicle.app.fragments;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.activities.MapCameraActivity;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.bean.VendorDetail;
import com.vehicle.app.msg.bean.SimpleLocation;
import com.vehicle.app.utils.ImageUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public final class VendorDetailFragment extends RefreshableFragment {

	private VendorDetail vendorDetail;

	public VendorDetailFragment(VendorDetail vendor) {
		this.vendorDetail = vendor;
	}

	private LayoutInflater inflater;

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;

		View parent = inflater.inflate(R.layout.layout_vendorinfo, container, false);
		
		ImageView ivHead = (ImageView) parent.findViewById(R.id.vendor_icon);

		final Vendor vendor = vendorDetail.getVendor();

		String url = vendor.getAvatar();
		ImageUtil.RenderImageView(url, ivHead, -1, -1);

		TextView tvName = (TextView) parent.findViewById(R.id.vendor_name);
		tvName.getPaint().setFakeBoldText(true);
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
		String strPhone = "";
		if (null != vendor.getMobile() && !vendor.getMobile().isEmpty() && !vendor.getMobile().equalsIgnoreCase("null")) {
			strPhone += vendor.getMobile();
		}

		if (null != vendor.getTelephone() && !vendor.getTelephone().isEmpty()
				&& !vendor.getTelephone().equalsIgnoreCase("null")) {
			strPhone += vendor.getTelephone();
		}

		tvMobile.setText(strPhone);

		TextView tvAddr = (TextView) parent.findViewById(R.id.vendor_address);
		tvAddr.setText(vendor.getAddress());

		TextView tvIntro = (TextView) parent.findViewById(R.id.vendor_introduction);
		tvIntro.setText(vendor.getIntroduction());

		View vDial = parent.findViewById(R.id.vendor_phonelayout);
		View tvMapLoc = parent.findViewById(R.id.vendor_addresslayout);

		vDial.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					String phone = vendor.getMobile();
					if (null == phone || phone.isEmpty()) {
						phone = vendor.getTelephone();
					}
					if (null != phone && !phone.isEmpty()) {
						Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://" + phone));
						startActivity(intent);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		tvMapLoc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					SimpleLocation loc = new SimpleLocation();
					loc.setLatitude(vendor.getPointX());
					loc.setLongitude(vendor.getPointY());

					Intent intent = new Intent(inflater.getContext(), MapCameraActivity.class);
					intent.putExtra(MapCameraActivity.KEY_POSITION, loc);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		return parent;
	}

	@Override
	public void refresh() {
		View parent = this.getView();
		if (null == parent) {
			System.out.println("view is null");
			return;
		}
		
		ImageView ivHead = (ImageView) parent.findViewById(R.id.vendor_icon);

		final Vendor vendor = vendorDetail.getVendor();

		String url = vendor.getAvatar();
		ImageUtil.RenderImageView(url, ivHead, -1, -1);

		TextView tvName = (TextView) parent.findViewById(R.id.vendor_name);
		tvName.getPaint().setFakeBoldText(true);
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
		String strPhone = "";
		if (null != vendor.getMobile() && !vendor.getMobile().isEmpty() && !vendor.getMobile().equalsIgnoreCase("null")) {
			strPhone += vendor.getMobile();
		}

		if (null != vendor.getTelephone() && !vendor.getTelephone().isEmpty()
				&& !vendor.getTelephone().equalsIgnoreCase("null")) {
			strPhone += vendor.getTelephone();
		}

		tvMobile.setText(strPhone);

		TextView tvAddr = (TextView) parent.findViewById(R.id.vendor_address);
		tvAddr.setText(vendor.getAddress());

		TextView tvIntro = (TextView) parent.findViewById(R.id.vendor_introduction);
		tvIntro.setText(vendor.getIntroduction());

		View vDial = parent.findViewById(R.id.vendor_phonelayout);
		View tvMapLoc = parent.findViewById(R.id.vendor_addresslayout);

		vDial.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					String phone = vendor.getMobile();
					if (null == phone || phone.isEmpty()) {
						phone = vendor.getTelephone();
					}
					if (null != phone && !phone.isEmpty()) {
						Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://" + phone));
						startActivity(intent);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		tvMapLoc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					SimpleLocation loc = new SimpleLocation();
					loc.setLatitude(vendor.getPointX());
					loc.setLongitude(vendor.getPointY());

					Intent intent = new Intent(inflater.getContext(), MapCameraActivity.class);
					intent.putExtra(MapCameraActivity.KEY_POSITION, loc);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
