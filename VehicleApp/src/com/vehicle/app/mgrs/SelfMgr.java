package com.vehicle.app.mgrs;

import com.vehicle.app.bean.Driver;

public class SelfMgr {

	private static class InstanceHolder{
		private static SelfMgr instance = new SelfMgr();
	}
	
	public static SelfMgr getInstance()
	{
		return InstanceHolder.instance;
	}
	
	private Driver selfDriver;

	public Driver getSelfDriver() {
		return this.selfDriver;
	}

	public void setSelfDriver(Driver self) {
		this.selfDriver = self;
	}
}
