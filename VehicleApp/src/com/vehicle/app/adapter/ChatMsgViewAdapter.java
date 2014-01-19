package com.vehicle.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;


import cn.edu.sjtu.vehicleapp.R;
import com.vehicle.app.bean.Message;
import com.vehicle.app.utils.UserUtil;

public class ChatMsgViewAdapter extends BaseAdapter {

	private List<Message> data;
	private LayoutInflater mInflater;

	public ChatMsgViewAdapter(Context context, List<Message> data) {
		this.data = data;
		mInflater = LayoutInflater.from(context);
	}

	public synchronized void addMsg(Message msg) {
		this.data.add(msg);
		
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		Message entity = data.get(position);

		if (convertView == null) {
			if (UserUtil.isSelf(entity.getSource())) {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_left, null);
			} else {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_right, null);
			}
		}

		TextView tvSendTime = (TextView) convertView
				.findViewById(R.id.tv_sendtime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		tvSendTime.setText(sdf.format(entity.getSentDate()));

		TextView tvUserName = (TextView) convertView
				.findViewById(R.id.tv_username);
		tvUserName.setText(entity.getSource());

		TextView tvContent = (TextView) convertView
				.findViewById(R.id.tv_chatcontent);
		tvContent.setText(entity.getContent());

		return convertView;
	}

}
