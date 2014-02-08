package com.vehicle.service.bean;

import java.util.HashMap;
import java.util.Map;

public class NewFileNotification implements INotification {

	private String token;
	private String source;
	private String target;
	private String fileName;

	public NewFileNotification(String source, String target, String token,
			String fileName) {
		this.source = source;
		this.target = target;
		this.token = token;
		this.fileName = fileName;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return Notifications.NewFile.toString();
	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return String.format("%s sent you a file", source);
	}

	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String getTarget() {
		// TODO Auto-generated method stub
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getToken() {
		return this.token;
	}

	public void setTokean(String token) {
		this.token = token;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String name) {
		this.fileName = name;
	}

	@Override
	public Map<String, Object> getExtras() {
		// TODO Auto-generated method stub
		Map<String, Object> extras = new HashMap<String, Object>();
		extras.put("token", token);
		return extras;
	}
}
