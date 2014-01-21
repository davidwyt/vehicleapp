package com.vehicle.app.adapter;

import java.util.List;

import com.vehicle.app.bean.User;
import cn.edu.sjtu.vehicleapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserViewAdapter extends BaseAdapter {
	
	private List<User> users;
	private LayoutInflater inflater;
	
	public UserViewAdapter(Context context, List<User> users)
	{
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

	@Override
	public View getView(int pos, View view, ViewGroup viewgroup) {
		// TODO Auto-generated method stub
		User user = this.users.get(pos);
		if(null == view)
		{
			view = this.inflater.inflate(R.layout.layout_user_item, null);
		}
		
		TextView tvAlias = (TextView)view.findViewById(R.id.useritem_tv_username);
		tvAlias.setText(user.getAlias());
		
		TextView tvLastMessage = (TextView)view.findViewById(R.id.useritem_tv_lastmessage);
		tvLastMessage.setText(user.getLastMessage());
		
		TextView tvLastMessageDate = (TextView)view.findViewById(R.id.useritem_tv_lastmessagetime);
		tvLastMessageDate.setText(user.getLastMessageDate().getSeconds()+"");
		
		ImageView ivHead = (ImageView)view.findViewById(R.id.useritem_iv_userhead);
		ivHead.setImageBitmap(user.getIcon());
		
		return view;
	}

}
