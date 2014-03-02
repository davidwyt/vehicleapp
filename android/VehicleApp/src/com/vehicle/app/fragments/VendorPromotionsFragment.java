package com.vehicle.app.fragments;

import java.util.List;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.bean.VendorPromotion;
import com.vehicle.app.bean.VendorDetail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class VendorPromotionsFragment extends RefreshableFragment {

	private VendorDetail vendor;

	public VendorPromotionsFragment(VendorDetail vendor) {
		this.vendor = vendor;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View parent = inflater.inflate(R.layout.layout_vendorpromotions, container, false);

		return parent;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		View parent = this.getView();
		if(null == parent)
			return;
		
		TableLayout table = (TableLayout) parent.findViewById(R.id.table_coupon);

		List<VendorPromotion> promotions = vendor.getPromotions();

		if (null != promotions) {
			for (int i = 0; i < promotions.size(); i++) {

				VendorPromotion promotion = promotions.get(i);

				TableRow row = new TableRow(getActivity());

				TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
						TableRow.LayoutParams.WRAP_CONTENT);
				row.setLayoutParams(lp);

				TableRow.LayoutParams nameLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						1.0f);
				TextView name = new TextView(getActivity());
				name.setLayoutParams(nameLayout);
				name.setText(promotion.getDiscount());
				name.setGravity(Gravity.CENTER);
				row.addView(name);

				TableRow.LayoutParams rangeeLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						1.0f);
				TextView range = new TextView(getActivity());
				range.setLayoutParams(rangeeLayout);
				range.setText(promotion.getDiscountRange());
				range.setGravity(Gravity.CENTER);
				row.addView(range);

				TextView date = new TextView(getActivity());
				TableRow.LayoutParams dateLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						1.0f);
				date.setLayoutParams(dateLayout);
				date.setText(promotion.getExpireDate());
				date.setGravity(Gravity.CENTER);
				row.addView(date);

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
