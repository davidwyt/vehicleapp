package com.vehicle.app.web.bean;

import java.util.Map;

import com.vehicle.app.bean.SelfDriver;

public class DriverLoginResult extends WebCallBaseResult {

	private Map<String, SelfDriver> result;

	@Override
	public SelfDriver getInfoBean() {
		// TODO Auto-generated method stub
		return result.get("Personal");
		// return isSuccess() ? JsonUtil.getSomeValue(getResult(), "Personal",
		// SelfDriver.class) : null;
	}

	@Override
	public Map<String, SelfDriver> getResult() {
		return this.result;
	}

	public void setResult(Map<String, SelfDriver> result) {
		this.result = result;
	}

}
