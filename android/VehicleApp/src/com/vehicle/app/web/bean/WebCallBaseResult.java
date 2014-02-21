package com.vehicle.app.web.bean;

public abstract class WebCallBaseResult {

	public static final int CODE_SUCCESS = 10000;

	private int code;
	private String message;

	public int getCode() {
		return code;
	}

	public void setCode(int c) {
		this.code = c;
	}

	public boolean isSuccess() {
		return CODE_SUCCESS == this.code;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String msg) {
		this.message = msg;
	}

	public abstract Object getInfoBean();
	public abstract Object getResult();
}
