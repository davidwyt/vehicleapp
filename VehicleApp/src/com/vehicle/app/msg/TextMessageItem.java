package com.vehicle.app.msg;

import android.os.Parcel;
import android.os.Parcelable;

public class TextMessageItem implements IMessageItem, Parcelable {

	private String id;
	private String source;
	private String target;
	private long sentTime;
	private String content;
	private MessageFlag flag;
	private int msgType;

	public String getId() {
		return this.id;
	}

	public void setId(String iid) {
		this.id = iid;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String s) {
		this.source = s;
	}

	public String getTarget() {
		return this.target;
	}

	public void setTarget(String tar) {
		this.target = tar;
	}

	public long getSentTime() {
		return this.sentTime;
	}

	public void setSentTime(long sentD) {
		this.sentTime = sentD;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String c) {
		this.content = c;
	}

	@Override
	public MessageFlag getFlag() {
		// TODO Auto-generated method stub
		return this.flag;
	}

	public void setFlag(MessageFlag flag) {
		this.flag = flag;
	}

	public int getMsgType() {
		return this.msgType;
	}

	public void setMsgType(int type) {
		this.msgType = type;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		// TODO Auto-generated method stub
		parcel.writeString(id);
		parcel.writeString(source);
		parcel.writeString(target);
		parcel.writeLong(sentTime);
		parcel.writeString(content);
		parcel.writeString(flag.toString());
		parcel.writeInt(msgType);
	}

	public static final Parcelable.Creator<TextMessageItem> CREATOR = new Parcelable.Creator<TextMessageItem>() {

		@Override
		public TextMessageItem createFromParcel(Parcel parcel) {
			// TODO Auto-generated method stub
			TextMessageItem item = new TextMessageItem();

			item.setId(parcel.readString());
			item.setSource(parcel.readString());
			item.setTarget(parcel.readString());
			item.setSentTime(parcel.readLong());
			item.setContent(parcel.readString());
			item.setFlag(MessageFlag.valueOf(parcel.readString()));
			item.setMsgType(parcel.readInt());

			return item;
		}

		@Override
		public TextMessageItem[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TextMessageItem[size];
		}

	};

}
