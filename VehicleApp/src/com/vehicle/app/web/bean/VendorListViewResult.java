package com.vehicle.app.web.bean;

import java.util.Map;

import com.vehicle.app.bean.Vendor;

public class VendorListViewResult extends WebCallBaseResult {

	private Map<String, Vendor> result;

	@Override
	public Map<String, Vendor> getInfoBean() {
		// TODO Auto-generated method stub
		// return isSuccess() ? JsonUtil.toKVMap(getResult(), String.class,
		// Vendor.class) : null;
		return result;
	}

	@Override
	public Map<String, Vendor> getResult() {
		// TODO Auto-generated method stub
		return result;
	}

	public void setResult(Map<String, Vendor> result) {
		this.result = result;
	}
}
