package com.vehicle.app.adapter;

import java.util.Date;
import java.util.List;

import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.SelfMgr;

import cn.edu.sjtu.vehicleapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecentContactListViewAdapter extends BaseAdapter {

	private List<?> users;
	private LayoutInflater inflater;

	public RecentContactListViewAdapter(Context context, List<?> users) {
		this.inflater = LayoutInflater.from(context);
		this.users = users;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return users.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return users.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int pos, View view, ViewGroup viewgroup) {
		// TODO Auto-generated method stub
		if (null == view) {
			view = this.inflater.inflate(R.layout.layout_user_item, null);
		}

		TextView tvAlias = (TextView) view.findViewById(R.id.useritem_tv_username);
		TextView tvLastMessage = (TextView) view.findViewById(R.id.useritem_tv_lastmessage);
		TextView tvLastMessageDate = (TextView) view.findViewById(R.id.useritem_tv_lastmessagetime);
		ImageView ivHead = (ImageView) view.findViewById(R.id.useritem_iv_userhead);

		Object user = this.users.get(pos);
		if (SelfMgr.getInstance().isDriver() && user instanceof Vendor) {
			Vendor vendor = (Vendor) user;

			tvAlias.setText(vendor.getName());

			tvLastMessage.setText(vendor.getLastMessage());

			tvLastMessageDate.setText((new Date()).getSeconds() + "");

			ivHead.setImageBitmap(vendor.getIcon());

		} else if (!SelfMgr.getInstance().isDriver() && user instanceof Driver) {
			Driver driver = (Driver) user;

			tvAlias.setText(driver.getAlias());
			
			System.out.println("driver name:" + driver.getName());
			
			tvLastMessage.setText(driver.getLastMessage());

			tvLastMessageDate.setText((new Date()).getSeconds() + "");

			ivHead.setImageBitmap(driver.getIcon());
		} else {
			System.out.println("what the type of user:" + user.getClass().toString());
		}

		return view;
	}

}
