package com.vehicle.app.bean;

import com.google.gson.annotations.SerializedName;

public class VendorPromotion {

	@SerializedName("specials_id")
	private String specialsId;

	private String discount;

	@SerializedName("discount_range")
	private String discountRange;

	@SerializedName("expire_date")
	private String expireDate;

	public String getSpecialsId() {
		return this.specialsId;
	}

	public void setSpecialsId(String id) {
		this.specialsId = id;
	}

	public String getDiscount() {
		return this.discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getDiscountRange() {
		return this.discountRange;
	}

	public void setDiscountRange(String range) {
		this.discountRange = range;
	}

	public String getExpireDate() {
		return this.expireDate;
	}

	public void setExpireDate(String date) {
		this.expireDate = date;
	}
}
