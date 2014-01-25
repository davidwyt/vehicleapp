package com.vehicle.service.bean;

public class UserInfo {

	private String userId;
	private boolean isDriver;

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String id) {
		this.userId = id;
	}

	public boolean getIsDriver() {
		return this.isDriver;
	}

	public void setIsDriver(boolean isDriver) {
		this.isDriver = isDriver;
	}
}
