package com.vehicle.app.web.bean;

import java.util.List;
import java.util.Map;

import com.vehicle.app.bean.VendorImage;

public class VendorImgViewResult extends WebCallBaseResult {

	private Map<String, List<VendorImage>> result;

	@Override
	public List<VendorImage> getInfoBean() {
		// TODO Auto-generated method stub
		return result.get("BusinessImages.list");
	}

	@Override
	public Map<String, List<VendorImage>> getResult() {
		// TODO Auto-generated method stub
		return result;
	}

}
