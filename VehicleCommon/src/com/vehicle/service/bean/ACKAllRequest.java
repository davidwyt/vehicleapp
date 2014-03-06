package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ACKAllRequest implements IRequest {
	private String memberId;

	@XmlElement
	public String getMemberId() {
		return this.memberId;
	}

	public void setMemberId(String id) {
		this.memberId = id;
	}
}
