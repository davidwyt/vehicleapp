package com.vehicle.app.fragments;

import java.util.List;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.bean.Vendor;
import com.vehicle.app.bean.VendorPromotion;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class VendorPromotionFragment extends Fragment {

	private Vendor vendor;

	public VendorPromotionFragment(Vendor vendor) {
		this.vendor = vendor;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View parent = inflater.inflate(R.layout.layout_vendorpromotion, container, false);

		TableLayout table = (TableLayout) parent.findViewById(R.id.table_promotion);

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
				name.setText(promotion.getTitle());
				row.addView(name);

				TextView date = new TextView(getActivity());

				TableRow.LayoutParams dateLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						0.5f);
				name.setLayoutParams(dateLayout);
				date.setText(promotion.getContent());
				row.addView(date);

				ImageView img = new ImageView(getActivity());
				TableRow.LayoutParams imgLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						0.5f);
				name.setLayoutParams(imgLayout);

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
				View divider = new View(getActivity());
				divider.setLayoutParams(dividerLayout);
				divider.setBackgroundColor(getResources().getColor(R.color.azure));

				table.addView(divider, 2 * i + 1);
			}
		}
		return parent;
	}
}
