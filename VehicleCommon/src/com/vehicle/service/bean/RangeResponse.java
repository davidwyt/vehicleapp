package com.vehicle.service.bean;

import java.util.List;

public class RangeResponse extends BaseResponse {
	
	private List<LocateInfo> list;

	public List<LocateInfo> getList() {
		return list;
	}

	public void setList(List<LocateInfo> list) {
		this.list = list;
	}

}
