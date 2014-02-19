package com.vehicle.app.bean;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Car implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5055005236789445113L;

	@SerializedName("personal_cars_id")
	private String id;

	@SerializedName("car_brand_name")
	private String brandName;

	@SerializedName("car_type_name")
	private String typeName;

	@SerializedName("car_plate")
	private String plate;

	@SerializedName("buy_date")
	private String buyDate;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBrandName() {
		return this.brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getPlate() {
		return this.plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getBuyDate() {
		return this.buyDate;
	}

	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
}
