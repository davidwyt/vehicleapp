package com.vehicle.app.web.bean;

import java.util.Map;

import com.vehicle.app.bean.SelfVendor;

public class VendorLoginResult extends WebCallBaseResult {

	private Map<String, SelfVendor> result;

	@Override
	public SelfVendor getInfoBean() {
		// TODO Auto-generated method stub
		// return isSuccess() ? JsonUtil.getSomeValue(getResult(), "Business",
		// SelfVendor.class) : null;
		return result.get("Business");
	}

	@Override
	public Map<String, SelfVendor> getResult() {
		// TODO Auto-generated method stub
		return result;
	}

	public void setResult(Map<String, SelfVendor> result) {
		this.result = result;
	}

}
