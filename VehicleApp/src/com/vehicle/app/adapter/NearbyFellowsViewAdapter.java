package com.vehicle.app.adapter;

import java.util.List;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.bean.Driver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NearbyFellowsViewAdapter extends BaseAdapter{

	private List<Driver> fellows;
	private LayoutInflater inflater;
	
	public NearbyFellowsViewAdapter(Context context, List<Driver> fellows)
	{
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
		
		Driver user = this.fellows.get(pos);
		if(null == view)
		{
			view = this.inflater.inflate(R.layout.layout_nearbyfellow_item, null);
		}
		
		TextView tvAlias = (TextView)view.findViewById(R.id.nearbyfellow_tv_name);
		tvAlias.setText(user.getAlias());
		
		TextView tvAddress = (TextView)view.findViewById(R.id.nearbyfellow_tv_address);
		tvAddress.setText(user.getLastMessage());
		
		TextView tvDistance = (TextView)view.findViewById(R.id.nearbyfellow_tv_distance);
		tvDistance.setText(user.getLastMessageDate().getSeconds()+"");
		
		ImageView ivHead = (ImageView)view.findViewById(R.id.nearbyfellow_iv_head);
		ivHead.setImageBitmap(user.getIcon());
		
		return view;
	}

}
