package com.vehicle.app.web.bean;

import java.util.List;
import java.util.Map;

import com.vehicle.app.bean.FavoriteVendor;

public class FavVendorListViewResult extends WebCallBaseResult {

	private Map<String, List<FavoriteVendor>> result;

	@Override
	public List<FavoriteVendor> getInfoBean() {
		// TODO Auto-generated method stub
		// return isSuccess() ? JsonUtil.getSomeValueList(getResult(),
		// "Favorite.list", FavoriteVendor.class) : null;
		return result.get("Favorite.list");
	}

	@Override
	public Map<String, List<FavoriteVendor>> getResult() {
		// TODO Auto-generated method stub
		return result;
	}

	public void setResult(Map<String, List<FavoriteVendor>> result) {
		this.result = result;
	}
}
