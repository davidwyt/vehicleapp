package com.vehicle.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.activities.ImgViewActivity;
import com.vehicle.app.activities.MapCameraActivity;
import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.FileMessage;
import com.vehicle.app.msg.bean.SimpleLocation;
import com.vehicle.app.msg.bean.TextMessage;
import com.vehicle.app.msg.worker.AudioPlayer;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.ImageUtil;
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

	private static final int IMG_WIDTH = 128;
	private static final int IMG_HEIGHT = 128;

	@SuppressLint("SimpleDateFormat")
	public View getView(int position, View convertView, ViewGroup parent) {

		IMessageItem entity = data.get(position);
		if (entity.getMessageType() == IMessageItem.MESSAGE_TYPE_TEXT
				|| entity.getMessageType() == IMessageItem.MESSAGE_TYPE_LOCATION) {

			final TextMessage msg = (TextMessage) entity;

			if (msg.getMessageType() == IMessageItem.MESSAGE_TYPE_LOCATION) {
				if (SelfMgr.getInstance().IsSelf(msg.getSource())) {
					convertView = mInflater.inflate(R.layout.chatting_item_msg_location_left, null);
				} else {
					convertView = mInflater.inflate(R.layout.chatting_item_msg_location_right, null);
				}

			} else {
				if (SelfMgr.getInstance().IsSelf(msg.getSource())) {
					convertView = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);
				} else {
					convertView = mInflater.inflate(R.layout.chatting_item_msg_text_right, null);
				}
			}

			TextView tvSendTime = (TextView) convertView.findViewById(R.id.chatmsg_tv_sendtime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
			tvSendTime.setText(sdf.format(msg.getSentTime()));

			BaseInfo info = this.getBaseInfo(msg.getSource());

			TextView tvUserName = (TextView) convertView.findViewById(R.id.chatmsg_tv_username);
			tvUserName.setVisibility(View.GONE);
			tvUserName.setText(info.name);
			TextPaint tp = tvUserName.getPaint();
			tp.setFakeBoldText(true);

			ImageView ivHead = (ImageView) convertView.findViewById(R.id.chatmsg_iv_userhead);
			ImageUtil.RenderImageView(info.url, ivHead, -1, -1);

			if (IMessageItem.MESSAGE_TYPE_TEXT == msg.getMessageType()) {
				TextView tvContent = (TextView) convertView.findViewById(R.id.chatmsg_tv_chatcontent);
				tvContent.setText(msg.getContent());
			} else if (IMessageItem.MESSAGE_TYPE_LOCATION == msg.getMessageType()) {
				ImageView ivContent = (ImageView) convertView.findViewById(R.id.chatloc_iv_content);
				ivContent.setImageResource(R.drawable.icon_location);
				ivContent.setOnClickListener(new OnClickListener() {

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
						}
					}
				});
			}

		} else if (entity.getMessageType() == IMessageItem.MESSAGE_TYPE_IMAGE
				|| entity.getMessageType() == IMessageItem.MESSAGE_TYPE_AUDIO) {
			final FileMessage pic = (FileMessage) entity;

			if (SelfMgr.getInstance().IsSelf(pic.getSource())) {
				convertView = mInflater.inflate(R.layout.chatting_item_pic_left, null);
			} else {
				convertView = mInflater.inflate(R.layout.chatting_item_pic_right, null);
			}

			TextView tvSendTime = (TextView) convertView.findViewById(R.id.chatpic_tv_sendtime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
			tvSendTime.setText(sdf.format(pic.getSentTime()));

			BaseInfo info = this.getBaseInfo(pic.getSource());

			TextView tvUserName = (TextView) convertView.findViewById(R.id.chatpic_tv_username);
			tvUserName.setText(info.name);
			TextPaint tp = tvUserName.getPaint();
			tp.setFakeBoldText(true);
			tvUserName.setVisibility(View.GONE);

			ImageView ivHead = (ImageView) convertView.findViewById(R.id.chatpic_iv_userhead);
			ImageUtil.RenderImageView(info.url, ivHead, -1, -1);

			ImageView ivContent = (ImageView) convertView.findViewById(R.id.chatpic_tv_content);

			if (entity.getMessageType() == IMessageItem.MESSAGE_TYPE_AUDIO) {
				ivContent.setImageResource(R.drawable.icon_audio);

				ivContent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						try {
							String path = pic.getPath();

							File file = new File(path);
							if (file.isFile() && file.exists()) {
								AudioPlayer player = new AudioPlayer();
								player.play(path);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

			} else if (entity.getMessageType() == IMessageItem.MESSAGE_TYPE_IMAGE) {

				String path = pic.getPath();
				File file = new File(path);
				if (file.isFile() && file.exists()) {
					Bitmap bitmap = ImageUtil.decodeSampledBitmapFromFile(path, IMG_WIDTH, IMG_HEIGHT);
					ivContent.setTag(path);

					ivContent.setImageBitmap(bitmap);

					ivContent.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							// TODO Auto-generated method stub
							try {
								String path = (String) view.getTag();

								Intent intent = new Intent(context, ImgViewActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

								intent.putExtra(ImgViewActivity.KEY_IMGPATH, path);
								context.startActivity(intent);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		}

		return convertView;
	}

	private class BaseInfo {

		public BaseInfo(String name, String url) {
			this.name = name;
			this.url = url;
		}

		public String name;
		public String url;
	}

	private BaseInfo getBaseInfo(String id) {
		String name = context.getResources().getString(R.string.zh_unknown);
		String url = Constants.URL_DEFAULTICON;

		try {
			if (SelfMgr.getInstance().isDriver()) {
				if (SelfMgr.getInstance().IsSelf(id)) {
					name = SelfMgr.getInstance().getSelfDriver().getAlias();
					url = SelfMgr.getInstance().getSelfDriver().getAvatar();
				} else {
					Vendor vendor = SelfMgr.getInstance().getVendorInfo(id);
					if (null != vendor) {
						name = vendor.getName();
						url = vendor.getAvatar();
					}
				}
			} else {
				if (SelfMgr.getInstance().IsSelf(id)) {
					name = SelfMgr.getInstance().getSelfVendor().getName();
					url = SelfMgr.getInstance().getSelfVendor().getAvatar();
				} else {
					Driver driver = SelfMgr.getInstance().getDriverInfo(id);
					if (null != driver) {
						name = driver.getAlias();
						url = driver.getAvatar();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new BaseInfo(name, url);
	}
}
