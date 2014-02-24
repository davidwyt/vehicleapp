package com.vehicle.app.bean;

import com.google.gson.annotations.SerializedName;

public class VendorImage {

	@SerializedName("member_id")
	private String memberId;
	
	private String src;
	private String synopsis;

	public String getMemberId() {
		return this.memberId;
	}

	public void setMemeberId(String id) {
		this.memberId = id;
	}

	public String getSrc() {
		return this.src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getSynopsis() {
		return this.synopsis;
	}

	public void setSynopsis(String syn) {
		this.synopsis = syn;
	}
}
