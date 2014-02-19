package com.vehicle.service.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class RangeResponse extends BaseResponse {
	
	private List<RangeInfo> list;

	@XmlElement
	public List<RangeInfo> getList() {
		return list;
	}

	public void setList(List<RangeInfo> list) {
		this.list = list;
	}

}
