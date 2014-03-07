package com.vehicle.app.msg.bean;

import com.vehicle.service.bean.NewFileNotification;

import android.os.Parcel;
import android.os.Parcelable;

public class FileMessage implements IMessageItem, Parcelable {

	private String token;
	private String source;
	private String target;
	private MessageFlag flag;
	private long sentTime;
	private int msgType;

	private String path;

	public int getMsgType() {
		return this.msgType;
	}

	public void setMsgType(int type) {
		this.msgType = type;
	}

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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(token);
		dest.writeString(source);
		dest.writeString(target);
		dest.writeString(flag.toString());
		dest.writeLong(sentTime);
		dest.writeString(path);
		dest.writeInt(msgType);
	}

	public static final Parcelable.Creator<FileMessage> CREATOR = new Parcelable.Creator<FileMessage>() {

		@Override
		public FileMessage createFromParcel(Parcel parcel) {
			// TODO Auto-generated method stub
			FileMessage item = new FileMessage();

			item.setToken(parcel.readString());
			item.setSource(parcel.readString());
			item.setTarget(parcel.readString());
			item.setFlag(MessageFlag.valueOf(parcel.readString()));
			item.setSentTime(parcel.readLong());
			item.setPath(parcel.readString());
			item.setMsgType(parcel.readInt());
			return item;
		}

		@Override
		public FileMessage[] newArray(int size) {
			// TODO Auto-generated method stub
			return new FileMessage[size];
		}

	};

	@Override
	public void fromRawNotification(Object notification) {
		// TODO Auto-generated method stub
		if (!(notification instanceof NewFileNotification)) {
			throw new IllegalArgumentException("notifcation not NewFileNotification");
		}

		NewFileNotification rawMsg = (NewFileNotification) notification;
		this.token = rawMsg.getToken();
		this.source = rawMsg.getSource();
		this.target = rawMsg.getTarget();
		this.sentTime = rawMsg.getSentTime();
		this.flag = MessageFlag.UNREAD;
		this.msgType = rawMsg.getMsgType();
		this.path = rawMsg.getFileName();
	}

	@Override
	public int getMessageType() {
		// TODO Auto-generated method stub
		return this.msgType;
	}
}
