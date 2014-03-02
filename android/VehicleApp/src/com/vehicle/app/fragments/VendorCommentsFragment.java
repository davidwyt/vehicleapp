package com.vehicle.app.fragments;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.adapter.CommentsViewAdapter;
import com.vehicle.app.bean.VendorDetail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@SuppressLint("ValidFragment")
public class VendorCommentsFragment extends RefreshableFragment {

	private VendorDetail vendor;

	public VendorCommentsFragment(VendorDetail vendor) {
		this.vendor = vendor;
	}

	private LayoutInflater inflater;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;

		View parent = inflater.inflate(R.layout.layout_vendorcomments, container, false);
		return parent;
	}
	
	public void refresh() {
		View parent = this.getView();
		if(null == parent)
			return;
		
		ListView commentsView = (ListView) parent.findViewById(R.id.vendorcomments);
		CommentsViewAdapter adapter = new CommentsViewAdapter(inflater, vendor.getReviews(), false);
		commentsView.setAdapter(adapter);
	}

}
