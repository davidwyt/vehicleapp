package com.vehicle.app.web.bean;

import java.util.List;
import java.util.Map;

import com.vehicle.app.bean.Driver;

public class NearbyDriverListViewResult extends WebCallBaseResult {

	private Map<String, List<Driver>> result;

	@Override
	public List<Driver> getInfoBean() {
		// TODO Auto-generated method stub
		return result.get("NearbyDriver.list");
	}

	@Override
	public Map<String, List<Driver>> getResult() {
		// TODO Auto-generated method stub
		return this.result;
	}

	public void setResult(Map<String, List<Driver>> result) {
		this.result = result;
	}
}
