package com.vehicle.app.fragments;

import java.util.List;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.activities.ImgViewActivity;
import com.vehicle.app.bean.VendorDetail;
import com.vehicle.app.bean.VendorCoupon;
import com.vehicle.app.utils.StringUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class VendorCouponsFragment extends Fragment {

	private VendorDetail vendor;

	public VendorCouponsFragment(VendorDetail vendor) {
		this.vendor = vendor;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View parent = inflater.inflate(R.layout.layout_vendorcoupons, container, false);
		
		TableLayout table = (TableLayout) parent.findViewById(R.id.table_promotion);
		List<VendorCoupon> coupons = vendor.getCoupons();

		if (null != coupons) {
			for (int i = 0; i < coupons.size(); i++) {

				final VendorCoupon coupon = coupons.get(i);

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
						if (StringUtil.IsNullOrEmpty(coupon.getContent())) {
							Toast.makeText(getActivity(), getActivity().getString(R.string.tip_nocoupondetail),
									Toast.LENGTH_LONG).show();
							return;
						}

						Intent intent = new Intent(inflater.getContext(), ImgViewActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra(ImgViewActivity.KEY_IMGURL, coupon.getContent());
						inflater.getContext().startActivity(intent);
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
		return parent;
	}

}
