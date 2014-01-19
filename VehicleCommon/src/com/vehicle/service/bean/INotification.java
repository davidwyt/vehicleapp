package com.vehicle.service.bean;

import java.util.Map;

public interface INotification {
	
	public String getTitle();
	public String getContent();
	
	public String getSource();
	public String getTarget();
	
	public Map<String, Object> getExtras();
}
