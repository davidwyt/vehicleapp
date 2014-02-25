package com.vehicle.app.bean;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SelfVendor extends Vendor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6369655167032880306L;

	@SerializedName("member_id")
	private String id;

	@SerializedName("nick_name")
	private String alias;

	private String email;
	private String password;
	private String guid;

	@SerializedName("log_count")
	private String logCount;

	private int integral;

	@SerializedName("last_log_date")
	private String lastLoginDate;
	
	@SerializedName("avatar_status")
	private int avatarStatus;

	private List<Comment> comments;

	private List<VendorPromotion> promotions;

	private List<VendorCoupon> coupons;

	private List<VendorImage> imgs;

	public List<VendorPromotion> getPromotions() {
		return this.promotions;
	}

	public void setPromotions(List<VendorPromotion> promotions) {
		this.promotions = promotions;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> reviews) {
		this.comments = reviews;
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

	public int getAvatarStatus() {
		return this.avatarStatus;
	}

	public void setAvatarStatus(int avaStatus) {
		this.avatarStatus = avaStatus;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String pwd) {
		this.password = pwd;
	}

	public String getGuid() {
		return this.guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getLogCount() {
		return this.logCount;
	}

	public void setLogCount(String logCount) {
		this.logCount = logCount;
	}

	public int getIntegral() {
		return this.integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(String date) {
		this.lastLoginDate = date;
	}
	
}
