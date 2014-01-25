package com.vehicle.imserver.utils;

import com.vehicle.service.bean.UserInfo;

public class Me {

	private UserInfo info = new UserInfo();

	private Me() {

	}

	private static class InstanceHolder {

		private static final Me instance = new Me();
	}

	public static Me getInstance() {
		return InstanceHolder.instance;
	}

	public void refreshInfo() {

	}

	public UserInfo getUserInfo() {
		return this.info;
	}
}
