package com.vehicle.app.adapter;

import java.util.List;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.utils.ImageUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NearbyFellowsViewAdapter extends BaseAdapter {

	private List<?> fellows;
	private LayoutInflater inflater;

	private final static int ICON_WIDTH = 64;
	private final static int ICON_HEIGHT = 64;

	public NearbyFellowsViewAdapter(Context context, List<?> fellows) {
		this.inflater = LayoutInflater.from(context);
		this.fellows = fellows;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.fellows.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return this.fellows.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup viewGroup) {
		// TODO Auto-generated method stub

		if (null == view) {
			view = this.inflater.inflate(R.layout.layout_nearbyfellow_item, null);
		}

		TextView tvAlias = (TextView) view.findViewById(R.id.nearbyfellow_tv_name);
		TextView tvFeature = (TextView) view.findViewById(R.id.nearbyfellow_tv_feature);
		TextView tvDistance = (TextView) view.findViewById(R.id.nearbyfellow_tv_distance);
		ImageView ivHead = (ImageView) view.findViewById(R.id.nearbyfellow_iv_head);

		Object fellow = this.fellows.get(pos);

		String name = "";
		String feature = "";
		String distance = "";
		String url = "";

		if (fellow instanceof Driver && !SelfMgr.getInstance().isDriver()) {

			Driver driver = (Driver) fellow;

			name = driver.getAlias();
			feature = driver.getIntroduction();

			distance = (double) (Math.round(driver.getDistance() * 100)) / 100
					+ inflater.getContext().getString(R.string.zh_kilometer);
			url = driver.getAvatar();

		} else if (fellow instanceof Vendor && SelfMgr.getInstance().isDriver()) {

			Vendor vendor = (Vendor) fellow;
			name = vendor.getName();
			feature = vendor.getAddress();
			distance = vendor.getDistance();
			url = vendor.getAvatar();

		} else {
			System.err.println("what the type of fellow...");
		}

		tvAlias.setText(name);

		tvFeature.setText(feature);

		tvDistance.setText(distance);

		ImageUtil.RenderImageView(url, ivHead, ICON_WIDTH, ICON_HEIGHT);

		return view;
	}

}
