package com.vehicle.app.bean;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class FavoriteVendor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7291125086960043063L;

	@SerializedName("shop_id")
	private String id;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("add_date")
	private String addedDate;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddedDate() {
		return this.addedDate;
	}

	public void setAddedDate(String date) {
		this.addedDate = date;
	}
}
