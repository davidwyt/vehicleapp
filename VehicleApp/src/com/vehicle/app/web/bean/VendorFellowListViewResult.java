package com.vehicle.app.web.bean;

import java.util.List;
import java.util.Map;

import com.vehicle.app.bean.VendorFellow;

public class VendorFellowListViewResult extends WebCallBaseResult {

	private Map<String, List<VendorFellow>> result;

	@Override
	public List<VendorFellow> getInfoBean() {
		// TODO Auto-generated method stub
		// return isSuccess() ? JsonUtil.getSomeValueList(getResult(),
		// "Favorite.list", VendorFellow.class) : null;
		return result.get("Favorite.list");
	}

	@Override
	public Map<String, List<VendorFellow>> getResult() {
		// TODO Auto-generated method stub
		return result;
	}

	public void setResult(Map<String, List<VendorFellow>> result) {
		this.result = result;
	}
}
