package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileTransmissionResponse extends BaseResponse {

	private long sentTime;
	private String token;

	public long getSentTime() {
		return this.sentTime;
	}

	public void setSentTime(long time) {
		this.sentTime = time;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
