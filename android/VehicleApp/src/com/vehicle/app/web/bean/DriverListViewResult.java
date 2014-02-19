package com.vehicle.app.web.bean;

import java.util.Map;

import com.vehicle.app.bean.Driver;

public class DriverListViewResult extends WebCallBaseResult {

	private Map<String, Driver> result;

	@Override
	public Map<String, Driver> getInfoBean() {
		// TODO Auto-generated method stub
		// return isSuccess() ? JsonUtil.toKVMap(getResult(), String.class,
		// Driver.class) : null;
		return result;
	}

	@Override
	public Map<String, Driver> getResult() {
		// TODO Auto-generated method stub
		return result;
	}

	public void setResult(Map<String, Driver> result) {
		this.result = result;
	}
}
