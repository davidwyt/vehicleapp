package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileMultiTransmissionResponse extends BaseResponse {

	private long sentTime;
	private String token;

	@XmlElement
	public long getSentTime() {
		return this.sentTime;
	}

	public void setSentTime(long time) {
		this.sentTime = time;
	}

	@XmlElement
	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
