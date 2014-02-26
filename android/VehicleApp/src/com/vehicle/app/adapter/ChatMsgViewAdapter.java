package com.vehicle.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.activities.ImgViewActivity;
import com.vehicle.app.activities.MapCameraActivity;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.FileMessage;
import com.vehicle.app.msg.bean.SimpleLocation;
import com.vehicle.app.msg.bean.TextMessage;
import com.vehicle.app.utils.JsonUtil;

public class ChatMsgViewAdapter extends BaseAdapter {

	private List<IMessageItem> data;
	private LayoutInflater mInflater;

	private Context context;

	public ChatMsgViewAdapter(Context context, List<IMessageItem> data) {
		this.data = data;
		mInflater = LayoutInflater.from(context);
		this.context = context;
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
		if (entity.getMessageType() == IMessageItem.MESSAGE_TYPE_TEXT) {

			final TextMessage msg = (TextMessage) entity;

			if (SelfMgr.getInstance().IsSelf(msg.getSource())) {
				convertView = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);
			} else {
				convertView = mInflater.inflate(R.layout.chatting_item_msg_text_right, null);
			}

			TextView tvSendTime = (TextView) convertView.findViewById(R.id.chatmsg_tv_sendtime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			tvSendTime.setText(sdf.format(msg.getSentTime()));

			TextView tvUserName = (TextView) convertView.findViewById(R.id.chatmsg_tv_username);
			tvUserName.setText(msg.getSource());

			TextView tvContent = (TextView) convertView.findViewById(R.id.chatmsg_tv_chatcontent);
			if (IMessageItem.MESSAGE_TYPE_TEXT == msg.getMessageType()) {
				tvContent.setText(msg.getContent());
			} else if (IMessageItem.MESSAGE_TYPE_LOCATION == msg.getMessageType()) {
				tvContent.setBackgroundResource(R.drawable.icon_location);
				tvContent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						try {
							Intent intent = new Intent(context, MapCameraActivity.class);
							SimpleLocation loc = JsonUtil.fromJson(msg.getContent(), SimpleLocation.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.putExtra(MapCameraActivity.KEY_POSITION, loc);
							context.startActivity(intent);
						} catch (Exception e) {
							e.printStackTrace();
							// Toast.makeText(context, e.getMessage() +
							// e.getLocalizedMessage(),
							// Toast.LENGTH_LONG).show();
						}
					}
				});
			}

		} else if (entity.getMessageType() == IMessageItem.MESSAGE_TYPE_IMAGE) {
			FileMessage pic = (FileMessage) entity;

			if (SelfMgr.getInstance().IsSelf(pic.getSource())) {
				convertView = mInflater.inflate(R.layout.chatting_item_pic_left, null);
			} else {
				convertView = mInflater.inflate(R.layout.chatting_item_pic_right, null);
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
			ivContent.setTag(bitmap);
			Bitmap content = scaleBitmap(bitmap);

			ivContent.setImageBitmap(content);

			ivContent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					Bitmap bitmap = (Bitmap) view.getTag();

					Intent intent = new Intent(context, ImgViewActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					ByteArrayOutputStream boas = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, boas);

					intent.putExtra(ImgViewActivity.KEY_IMGBYTES, boas.toByteArray());
					context.startActivity(intent);
				}
			});
		} else if (entity.getMessageType() == IMessageItem.MESSAGE_TYPE_AUDIO) {

		}

		return convertView;
	}

	private Bitmap scaleBitmap(Bitmap bitmap) {

		if (null == bitmap)
			return null;

		/**
		 * DisplayMetrics dm = context.getResources().getDisplayMetrics(); int
		 * screenWidth = dm.widthPixels; int screenHeight = dm.heightPixels;
		 * 
		 * int picWidth = bitmap.getWidth(); int picHeight = bitmap.getHeight();
		 * 
		 * int scaledWidth = picWidth; int scaledHeight = picHeight;
		 * 
		 * if (picWidth > screenWidth * 3 / 2 || picHeight > screenHeight / 2) {
		 * if (picWidth > screenWidth * 3 / 2) { scaledWidth = screenWidth * 3 /
		 * 2; scaledHeight = scaledWidth * picHeight / picWidth; } else {
		 * scaledHeight = screenHeight / 2; scaledWidth = scaledHeight *
		 * picWidth / picHeight; } }
		 */
		return Bitmap.createScaledBitmap(bitmap, 128, 128, true);
	}
}
