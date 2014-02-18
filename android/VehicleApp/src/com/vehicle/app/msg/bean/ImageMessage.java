package com.vehicle.app.msg.bean;


import com.vehicle.service.bean.NewFileNotification;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ImageMessage implements IMessageItem, Parcelable {

	private String token;
	private String name;
	private String source;
	private String target;
	private MessageFlag flag;
	private long sentTime;
	private Bitmap content;

	private String path;

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getToken() {

		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public MessageFlag getFlag() {
		// TODO Auto-generated method stub
		return flag;
	}

	public void setFlag(MessageFlag flag) {
		this.flag = flag;
	}

	public long getSentTime() {
		return this.sentTime;
	}

	public void setSentTime(long time) {
		this.sentTime = time;
	}

	public Bitmap getContent() {
		return this.content;
	}

	public void setContent(Bitmap bitmap) {
		this.content = bitmap;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(token);
		dest.writeString(name);
		dest.writeString(source);
		dest.writeString(target);
		dest.writeString(flag.toString());
		dest.writeLong(sentTime);
		//content.writeToParcel(dest, flags);
		dest.writeString(path);
	}

	public static final Parcelable.Creator<ImageMessage> CREATOR = new Parcelable.Creator<ImageMessage>() {

		@Override
		public ImageMessage createFromParcel(Parcel parcel) {
			// TODO Auto-generated method stub
			ImageMessage item = new ImageMessage();

			item.setToken(parcel.readString());
			item.setName(parcel.readString());
			item.setSource(parcel.readString());
			item.setTarget(parcel.readString());
			item.setFlag(MessageFlag.valueOf(parcel.readString()));
			item.setSentTime(parcel.readLong());
			//item.setContent(Bitmap.CREATOR.createFromParcel(parcel));
			item.setPath(parcel.readString());
			
			return item;
		}

		@Override
		public ImageMessage[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ImageMessage[size];
		}

	};

	@Override
	public void fromRawNotification(Object notification) {
		// TODO Auto-generated method stub
		if(!(notification instanceof NewFileNotification))
		{
			throw new IllegalArgumentException("notifcation not NewFileNotification");
		}
		
		NewFileNotification rawMsg = (NewFileNotification)notification;
		this.token = rawMsg.getToken();
		this.name = rawMsg.getFileName();
		this.source = rawMsg.getSource();
		this.target = rawMsg.getTarget();
		this.sentTime = rawMsg.getSentTime();
		this.flag = MessageFlag.UNREAD;
	}

	@Override
	public int getMessageType() {
		// TODO Auto-generated method stub
		return IMessageItem.MESSAGE_TYPE_IMAGE;
	}
}
