package com.vehicle.app.web.bean;

import java.util.Map;

import com.vehicle.app.bean.Vendor;

public class VendorViewResult extends WebCallBaseResult {

	private Map<String, Vendor> result;

	@Override
	public Vendor getInfoBean() {
		// TODO Auto-generated method stub
		// return isSuccess() ? JsonUtil.getSomeValue(getResult(), "Business",
		// Vendor.class) : null;
		return result.get("Business");
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
