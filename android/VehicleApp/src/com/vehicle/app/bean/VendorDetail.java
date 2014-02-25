package com.vehicle.app.bean;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class VendorDetail {

	@SerializedName("Business")
	private Vendor vendor;

	@SerializedName("BusinessSpecials.list")
	private List<VendorPromotion> promotions;

	@SerializedName("Review.list")
	private List<Comment> reviews;

	@SerializedName("BusinessActivity.list")
	private List<VendorCoupon> coupons;

	@SerializedName("BusinessImages.list")
	private List<VendorImage> imgs;

	public Vendor getVendor() {
		return this.vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public List<VendorPromotion> getPromotions() {
		return this.promotions;
	}

	public void setPromotions(List<VendorPromotion> promotions) {
		this.promotions = promotions;
	}

	public List<Comment> getReviews() {
		return this.reviews;
	}

	public void setReviews(List<Comment> reviews) {
		this.reviews = reviews;
	}

	public List<VendorCoupon> getCoupons() {
		return this.coupons;
	}

	public void setCoupons(List<VendorCoupon> coupons) {
		this.coupons = coupons;
	}

	public List<VendorImage> getImgs() {
		return this.imgs;
	}

	public void setImgs(List<VendorImage> imgs) {
		this.imgs = imgs;
	}
}
