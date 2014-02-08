package com.vehicle.app.mgrs;

import com.vehicle.app.bean.Driver;

public class SelfMgr {

	private static class InstanceHolder {
		private static SelfMgr instance = new SelfMgr();
	}

	public static SelfMgr getInstance() {
		return InstanceHolder.instance;
	}

	private boolean isDriver;

	public boolean getIsDriver() {
		return this.isDriver;
	}

	public void setIsDriver(boolean isDriver) {
		this.isDriver = isDriver;
	}

	private Driver selfDriver;

	public Driver getSelfDriver() {
		return this.selfDriver;
	}

	public void setSelfDriver(Driver self) {
		this.selfDriver = self;
	}

	public String getId() {
		return isDriver ? selfDriver.getId() : selfDriver.getId();
	}

	public boolean IsSelf(String id) {
		return this.getId().equals(id);
	}
}
