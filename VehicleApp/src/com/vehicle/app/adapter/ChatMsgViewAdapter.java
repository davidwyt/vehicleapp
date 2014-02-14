package com.vehicle.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextPaint;
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

import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.PictureMessage;
import com.vehicle.app.msg.bean.TextMessage;

public class ChatMsgViewAdapter extends BaseAdapter {

	private List<IMessageItem> data;
	private LayoutInflater mInflater;

	public ChatMsgViewAdapter(Context context, List<IMessageItem> data) {
		this.data = data;
		mInflater = LayoutInflater.from(context);
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

		if (entity instanceof TextMessage) {

			TextMessage msg = (TextMessage) entity;

			if (SelfMgr.getInstance().IsSelf(msg.getSource())) {
				if (convertView == null || R.id.chatitem_msg_left != convertView.getId()) {
					convertView = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);
				}
			} else {
				if (convertView == null || R.id.chatitem_msg_right != convertView.getId()) {
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
		} else if (entity instanceof PictureMessage) {
			PictureMessage pic = (PictureMessage) entity;

			if (SelfMgr.getInstance().IsSelf(pic.getSource())) {
				if (convertView == null || R.id.chatitem_pic_left != convertView.getId()) {
					convertView = mInflater.inflate(R.layout.chatting_item_pic_left, null);
				}
			} else {
				if (convertView == null || R.id.chatitem_pic_right != convertView.getId()) {
					convertView = mInflater.inflate(R.layout.chatting_item_pic_right, null);
				}
			}

			TextView tvSendTime = (TextView) convertView.findViewById(R.id.chatpic_tv_sendtime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			tvSendTime.setText(sdf.format(new Date()));

			TextView tvUserName = (TextView) convertView.findViewById(R.id.chatpic_tv_username);
			tvUserName.setText(pic.getSource());
			TextPaint tp = tvUserName.getPaint();
			tp.setFakeBoldText(true);

			ImageView ivContent = (ImageView) convertView.findViewById(R.id.chatpic_tv_content);

			Bitmap bitmap = pic.getContent();

			if (null == bitmap) {
				bitmap = BitmapFactory.decodeFile(pic.getPath());
				pic.setContent(bitmap);
			}

			Bitmap content = Bitmap.createScaledBitmap(bitmap, parent.getWidth(), parent.getHeight(), true);

			ivContent.setImageBitmap(content);
		}

		return convertView;
	}

}
