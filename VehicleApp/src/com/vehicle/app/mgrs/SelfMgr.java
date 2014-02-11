package com.vehicle.app.mgrs;

import com.vehicle.app.bean.SelfDriver;
import com.vehicle.app.bean.SelfVendor;

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

	private SelfDriver selfDriver;
	private SelfVendor selfVendor;

	public SelfDriver getSelfDriver() {
		return this.selfDriver;
	}

	public void setSelfDriver(SelfDriver self) {
		this.selfDriver = self;
	}

	public SelfVendor getSelfVendor() {
		return this.selfVendor;
	}

	public void setSelfVendor(SelfVendor self) {
		this.selfVendor = self;
	}

	public String getId() {
		return isDriver ? selfDriver.getId() : selfVendor.getId();
	}

	public boolean IsSelf(String id) {
		return this.getId().equals(id);
	}
}
