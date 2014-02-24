package com.vehicle.app.bean;

public class SelfDriver extends Driver {
	private static final long serialVersionUID = -8008365646929127682L;

	private String password;
	private String guid;

	private int integral;

	private String sid;

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String pwd) {
		this.password = pwd;
	}

	public String getGuid() {
		return this.guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public int getIntegral() {
		return this.integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getSid() {
		return this.sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

}
