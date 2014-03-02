package com.vehicle.app.bean;

public class RoleInfo {
	private String userName;
	private String password;
	private int roleType;
	private boolean isAutoLog;

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String name) {
		this.userName = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String pwd) {
		this.password = pwd;
	}

	public int getRoleType() {
		return this.roleType;
	}

	public void setRoleType(int role) {
		this.roleType = role;
	}

	public boolean getIsAutoLog() {
		return this.isAutoLog;
	}

	public void setIsAutoLog(boolean isAuto) {
		this.isAutoLog = isAuto;
	}
}
