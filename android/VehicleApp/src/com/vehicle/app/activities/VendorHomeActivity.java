package com.vehicle.app.activities;

import com.vehicle.app.adapter.VendorImagePageAdapter;
import com.vehicle.app.adapter.VendorInfoFragmentAdapter;

import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;
import com.viewpagerindicator.UnderlinePageIndicator;

import cn.edu.sjtu.vehicleapp.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;

public class VendorHomeActivity extends FragmentActivity {

	VendorInfoFragmentAdapter mTileAdapter;
	ViewPager mTilePager;
	TitlePageIndicator mTileIndicator;

	VendorImagePageAdapter mImgAdapter;
	ViewPager mImgPager;
	UnderlinePageIndicator mImgIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_vendorhome);

		mTileAdapter = new VendorInfoFragmentAdapter(getSupportFragmentManager());

		mTilePager = (ViewPager) findViewById(R.id.vendorhome_pager);
		mTilePager.setAdapter(mTileAdapter);

		TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.vendorhome_indicator);
		indicator.setViewPager(mTilePager);
		indicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
		indicator.setSelectedBold(true);

		mTileIndicator = indicator;

		mImgAdapter = new VendorImagePageAdapter(getSupportFragmentManager());
		mImgPager = (ViewPager) this.findViewById(R.id.vendorhome_imagepager);
		mImgPager.setAdapter(mImgAdapter);
		
		mImgIndicator = (UnderlinePageIndicator) this.findViewById(R.id.vendorhome_imageindicator);
		mImgIndicator.setViewPager(mImgPager);
	}
}
