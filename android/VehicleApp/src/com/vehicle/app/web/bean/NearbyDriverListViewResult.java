package com.vehicle.app.web.bean;

import java.util.Collection;
import java.util.Map;

import com.vehicle.app.bean.Driver;

public class NearbyDriverListViewResult extends WebCallBaseResult {

	private Map<String, Driver> result;

	@Override
	public Collection<Driver> getInfoBean() {
		// TODO Auto-generated method stub
		return this.result.values();
	}

	@Override
	public Map<String, Driver> getResult() {
		// TODO Auto-generated method stub
		return this.result;
	}

	public void setResult(Map<String, Driver> result) {
		this.result = result;
	}
}
