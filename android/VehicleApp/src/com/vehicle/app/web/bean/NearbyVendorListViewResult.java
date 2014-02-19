package com.vehicle.app.web.bean;

import java.util.List;
import java.util.Map;

import com.vehicle.app.bean.Vendor;

public class NearbyVendorListViewResult extends WebCallBaseResult {

	private Map<String, List<Vendor>> result;

	@Override
	public List<Vendor> getInfoBean() {
		// TODO Auto-generated method stub
		// return isSuccess() ? JsonUtil.getSomeValueList(getResult(),
		// "MemberBusinessProject.list", Vendor.class) : null;
		return result.get("MemberBusinessProject.list");
	}

	@Override
	public Map<String, List<Vendor>> getResult() {
		// TODO Auto-generated method stub
		return result;
	}

	public void setResult(Map<String, List<Vendor>> result) {
		this.result = result;
	}
}
