package com.vehicle.app.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class PictureMessageItem implements IMessageItem, Parcelable{
	
	private String token;
	private String name;
	private String source;
	private String target;
	private MessageFlag flag;
	private long sentTime;
	private Bitmap content;

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

	public long getSentTime()
	{
		return this.sentTime;
	}
	
	public void setSentTime(long time)
	{
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
		content.writeToParcel(dest, flags);
	}
	
	public static final Parcelable.Creator<PictureMessageItem> CREATOR = new Parcelable.Creator<PictureMessageItem>() {

		@Override
		public PictureMessageItem createFromParcel(Parcel parcel) {
			// TODO Auto-generated method stub
			PictureMessageItem item = new PictureMessageItem();
			
			item.setToken(parcel.readString());
			item.setName(parcel.readString());
			item.setSource(parcel.readString());
			item.setTarget(parcel.readString());
			item.setFlag(MessageFlag.valueOf(parcel.readString()));
			item.setSentTime(parcel.readLong());
			item.setContent(Bitmap.CREATOR.createFromParcel(parcel));
			
			return item;
		}

		@Override
		public PictureMessageItem[] newArray(int size) {
			// TODO Auto-generated method stub
			return new PictureMessageItem[size];
		}

	};
}
