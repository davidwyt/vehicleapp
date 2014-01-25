package com.vehicle.service.bean;

import java.util.Map;

public class FollowshipDroppedNotification implements INotification{

	private String source;
	private String target;
	
	public FollowshipDroppedNotification()
	{
		
	}
	
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return Notifications.DropFollow.toString();
	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return String.format("%s unfollowed you", this.source);
	}

	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return this.source;
	}
	
	public void setSource(String source)
	{
		this.source = source;
	}

	@Override
	public String getTarget() {
		// TODO Auto-generated method stub
		return this.target;
	}
	
	public void setTarget(String target)
	{
		this.target = target;
	}

	@Override
	public Map<String, Object> getExtras() {
		// TODO Auto-generated method stub
		return null;
	}
}
