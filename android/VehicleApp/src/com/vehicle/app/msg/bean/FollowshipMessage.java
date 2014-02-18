package com.vehicle.app.msg.bean;

public class FollowshipMessage implements IMessageItem {

	private String source;
	private String target;

	private MessageFlag flag;
	private long sentTime;

	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String getTarget() {
		// TODO Auto-generated method stub
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public MessageFlag getFlag() {
		// TODO Auto-generated method stub
		return this.flag;
	}

	public void setFlag(MessageFlag flag) {
		this.flag = flag;
	}

	@Override
	public long getSentTime() {
		// TODO Auto-generated method stub
		return this.sentTime;
	}

	public void setSentTime(long time) {
		this.sentTime = time;
	}

	@Override
	public int getMessageType() {
		// TODO Auto-generated method stub
		return IMessageItem.MESSAGE_TYPE_FOLLOW;
	}

	@Override
	public void fromRawNotification(Object notification) {
		// TODO Auto-generated method stub

	}

}
