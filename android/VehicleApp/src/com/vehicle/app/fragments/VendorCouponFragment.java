package com.vehicle.app.fragments;

import java.util.List;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.bean.Vendor;
import com.vehicle.app.bean.VendorCoupon;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class VendorCouponFragment extends Fragment {

	private Vendor vendor;

	public VendorCouponFragment(Vendor vendor) {
		this.vendor = vendor;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View parent = inflater.inflate(R.layout.layout_vendorcoupon, container, false);

		TableLayout table = (TableLayout) parent.findViewById(R.id.table_coupon);

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
				name.setLayoutParams(nameLayout);
				name.setText(coupon.getDiscount());
				row.addView(name);

				TableRow.LayoutParams rangeeLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						1.0f);
				TextView range = new TextView(getActivity());
				range.setLayoutParams(rangeeLayout);
				range.setText(coupon.getDiscountRange());
				row.addView(range);

				TextView date = new TextView(getActivity());

				TableRow.LayoutParams dateLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						1.0f);
				name.setLayoutParams(dateLayout);
				date.setText(coupon.getExpireDate());
				row.addView(date);

				table.addView(row, 2 * i);

				TableRow.LayoutParams dividerLayout = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
				View divider = new View(getActivity());
				divider.setLayoutParams(dividerLayout);
				divider.setBackgroundColor(getResources().getColor(R.color.azure));

				table.addView(divider, 2 * i + 1);
			}
		}
		return parent;
	}
}
