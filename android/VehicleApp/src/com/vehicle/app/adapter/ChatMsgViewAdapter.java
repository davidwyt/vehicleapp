package com.vehicle.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

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
import com.vehicle.app.utils.FileUtil;
import com.vehicle.app.utils.ImageUtil;
import com.vehicle.app.utils.JsonUtil;
import com.vehicle.app.utils.URLUtil;

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
		if (entity.getMessageType() == IMessageItem.MESSAGE_TYPE_TEXT
				|| entity.getMessageType() == IMessageItem.MESSAGE_TYPE_LOCATION) {

			final TextMessage msg = (TextMessage) entity;

			if (SelfMgr.getInstance().IsSelf(msg.getSource())) {
				convertView = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);
			} else {
				convertView = mInflater.inflate(R.layout.chatting_item_msg_text_right, null);
			}

			TextView tvSendTime = (TextView) convertView.findViewById(R.id.chatmsg_tv_sendtime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
			tvSendTime.setText(sdf.format(msg.getSentTime()));

			BaseInfo info = this.getBaseInfo(msg.getSource());

			TextView tvUserName = (TextView) convertView.findViewById(R.id.chatmsg_tv_username);
			tvUserName.setText(info.name);
			TextPaint tp = tvUserName.getPaint();
			tp.setFakeBoldText(true);

			ImageView ivHead = (ImageView) convertView.findViewById(R.id.chatmsg_iv_userhead);
			ImageUtil.RenderImageView(info.url, ivHead, -1, -1);

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

			ImageView ivHead = (ImageView) convertView.findViewById(R.id.chatpic_iv_userhead);
			ImageUtil.RenderImageView(info.url, ivHead, -1, -1);

			ImageView ivContent = (ImageView) convertView.findViewById(R.id.chatpic_tv_content);
			final byte[] byteArray = pic.getContent();

			if (entity.getMessageType() == IMessageItem.MESSAGE_TYPE_AUDIO) {
				ivContent.setImageResource(R.drawable.icon_audio);

				ivContent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						try {
							String path = pic.getPath();

							if (null != path && !path.isEmpty()) {

							} else {
								path = getAudioTempPath();
								FileUtil.WriteBytes(byteArray, path);
							}

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
				Bitmap bitmap = null;
				String path = pic.getPath();
				if (null != path && !path.isEmpty()) {
					File file = new File(path);
					if (file.isFile() && file.exists()) {
						try {
							bitmap = BitmapFactory.decodeFile(path);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					// path = getImgTempPath();
					// FileUtil.WriteBytes(pic.getContent(), path);

					try {
						byte[] bytes = pic.getContent();
						if (null != bytes) {
							bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				/**
				 * if (null != byteArray) { try { System.out.println("length :"
				 * + byteArray.length); String path = getImgTempPath();
				 * 
				 * //bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
				 * byteArray.length); } catch (Exception e) {
				 * e.printStackTrace(); } } if (null == bitmap) { bitmap =
				 * BitmapFactory.decodeFile(pic.getPath()); }
				 */

				ivContent.setTag(bitmap);
				Bitmap content = scaleBitmap(bitmap);

				ivContent.setImageBitmap(content);

				ivContent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub

						Bitmap bitmap = (Bitmap) view.getTag();
						if (null == bitmap)
							return;

						Intent intent = new Intent(context, ImgViewActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

						ByteArrayOutputStream boas = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, boas);

						intent.putExtra(ImgViewActivity.KEY_IMGBYTES, boas.toByteArray());
						context.startActivity(intent);
					}
				});
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

	private Bitmap scaleBitmap(Bitmap bitmap) {
		if (null == bitmap) {
			System.out.println("bitmap is null");
			return null;
		}
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
		if (bitmap.getWidth() < 128 && bitmap.getHeight() < 128) {
			return bitmap;
		} else {
			return Bitmap.createScaledBitmap(bitmap, 128, 128, true);
		}
	}

	private String getAudioTempPath() {
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		String name = UUID.randomUUID().toString() + ".3gp";
		return URLUtil.UrlAppend(path, "Audio", name);
	}
}
