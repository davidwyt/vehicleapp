package com.vehicle.app.bean;

import com.google.gson.annotations.SerializedName;

public class VendorPromotion {

	@SerializedName("activity_id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("content")
	private String content;

	@SerializedName("expire_date")
	private String expireDate;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getExpireDate() {
		return this.expireDate;
	}

	public void setExpireDate(String date) {
		this.expireDate = date;
	}
}
