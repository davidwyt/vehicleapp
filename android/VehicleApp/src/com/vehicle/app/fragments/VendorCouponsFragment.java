package com.vehicle.app.fragments;

import java.util.List;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.bean.VendorDetail;
import com.vehicle.app.bean.VendorCoupon;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class VendorCouponsFragment extends RefreshableFragment {

	private VendorDetail vendor;

	public VendorCouponsFragment(VendorDetail vendor) {
		this.vendor = vendor;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View parent = inflater.inflate(R.layout.layout_vendorcoupons, container, false);
		return parent;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		View parent = this.getView();
		if(null == parent)
			return;
		
		TableLayout table = (TableLayout) parent.findViewById(R.id.table_promotion);
		List<VendorCoupon> coupons = vendor.getCoupons();

		if (null != coupons) {
			for (int i = 0; i < coupons.size(); i++) {

				VendorCoupon coupon = coupons.get(i);

				TableRow row = new TableRow(getActivity());

				TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
						TableRow.LayoutParams.WRAP_CONTENT);
				row.setLayoutParams(lp);

				TableRow.LayoutParams nameLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						1.0f);
				TextView name = new TextView(getActivity());
				name.setGravity(Gravity.CENTER);
				name.setLayoutParams(nameLayout);
				name.setText(coupon.getTitle());
				row.addView(name);

				TextView date = new TextView(getActivity());

				TableRow.LayoutParams dateLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						0.5f);
				date.setLayoutParams(dateLayout);
				date.setText(coupon.getExpireDate());
				date.setGravity(Gravity.CENTER);
				row.addView(date);

				ImageView img = new ImageView(getActivity());
				TableRow.LayoutParams imgLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						0.5f);
				img.setLayoutParams(imgLayout);

				img.setImageResource(R.drawable.icon_viewdetail);

				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub

					}
				});

				row.addView(img);
				row.setPadding(1, 1, 1, 1);

				table.addView(row, 2 * i);

				TableRow.LayoutParams dividerLayout = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
				dividerLayout.topMargin = 2;
				
				View divider = new View(getActivity());
				divider.setLayoutParams(dividerLayout);
				divider.setBackgroundColor(getResources().getColor(R.color.azure));

				table.addView(divider, 2 * i + 1);
			}
		}
	}
}
