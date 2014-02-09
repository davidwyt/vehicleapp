package com.vehicle.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.bean.IMessageItem;
import com.vehicle.app.bean.TextMessageItem;
import com.vehicle.app.bean.PictureMessageItem;
import com.vehicle.app.mgrs.SelfMgr;

public class ChatMsgViewAdapter extends BaseAdapter {

	private List<IMessageItem> data;
	private LayoutInflater mInflater;
	private Context mContext;
	
	public ChatMsgViewAdapter(Context context, List<IMessageItem> data) {
		this.data = data;
		mInflater = LayoutInflater.from(context);
		this.mContext = context;
	}

	public synchronized void addChatItem(IMessageItem item) {
		this.data.add(item);
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

	@SuppressLint("SimpleDateFormat")
	public View getView(int position, View convertView, ViewGroup parent) {

		IMessageItem entity = data.get(position);

		if (entity instanceof TextMessageItem) {

			TextMessageItem msg = (TextMessageItem) entity;

			if (convertView == null || R.id.chatitem_msg != convertView.getId()) {
				if (SelfMgr.getInstance().IsSelf(msg.getSource())) {
					convertView = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);
				} else {
					convertView = mInflater.inflate(R.layout.chatting_item_msg_text_right, null);
				}
			}

			TextView tvSendTime = (TextView) convertView.findViewById(R.id.chatmsg_tv_sendtime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			tvSendTime.setText(sdf.format(msg.getSentTime()));

			TextView tvUserName = (TextView) convertView.findViewById(R.id.chatmsg_tv_username);
			tvUserName.setText(msg.getSource());

			TextView tvContent = (TextView) convertView.findViewById(R.id.chatmsg_tv_chatcontent);
			tvContent.setText(msg.getContent());
		} else if (entity instanceof PictureMessageItem) {
			PictureMessageItem pic = (PictureMessageItem) entity;
			if (convertView == null || R.id.chatitem_pic != convertView.getId()) {
				if (SelfMgr.getInstance().IsSelf(pic.getSource())) {
					convertView = mInflater.inflate(R.layout.chatting_item_pic_left, null);
				} else {
					convertView = mInflater.inflate(R.layout.chatting_item_pic_right, null);
				}
			}

			TextView tvSendTime = (TextView) convertView.findViewById(R.id.chatpic_tv_sendtime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			tvSendTime.setText(sdf.format(new Date()));

			TextView tvUserName = (TextView) convertView.findViewById(R.id.chatpic_tv_username);
			tvUserName.setText(pic.getSource());

			ImageView ivContent = (ImageView) convertView.findViewById(R.id.chatpic_tv_content);

			ivContent.setImageBitmap(pic.getContent());
			//ivContent.setImageBitmap(BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.icon_bakground));
		}

		return convertView;
	}

}
