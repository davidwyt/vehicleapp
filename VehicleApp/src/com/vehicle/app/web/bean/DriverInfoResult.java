package com.vehicle.app.web.bean;

import com.google.gson.annotations.SerializedName;

public class DriverInfoResult extends WebCallBaseResult {

	@SerializedName("result")
	private DriverResultInternal result;

	@Override
	public Object getResult() {
		// TODO Auto-generated method stub
		return this.result;
	}

	public void setResult(DriverResultInternal result) {
		this.result = result;
	}

}
