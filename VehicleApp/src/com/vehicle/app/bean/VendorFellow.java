package com.vehicle.app.bean;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class VendorFellow implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -820951044260841844L;

	@SerializedName("member_id")
	private String id;

	@SerializedName("nick_name")
	private String alias;

	@SerializedName("add_date")
	private String addedDate;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAddedDate() {
		return this.addedDate;
	}

	public void setAddedDate(String time) {
		this.addedDate = time;
	}
}
