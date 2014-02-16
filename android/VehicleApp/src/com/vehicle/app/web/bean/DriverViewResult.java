package com.vehicle.app.web.bean;

import java.util.Map;

import com.vehicle.app.bean.Driver;

public class DriverViewResult extends WebCallBaseResult {

	private Map<String, Driver> result;

	@Override
	public Driver getInfoBean() {
		// TODO Auto-generated method stub
		// return isSuccess() ? JsonUtil.getSomeValue(getResult(), "Personal",
		// Driver.class) : null;
		return result.get("Personal");
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
