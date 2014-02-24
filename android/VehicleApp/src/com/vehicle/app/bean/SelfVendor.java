package com.vehicle.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.vehicle.app.utils.Constants;

public class SelfVendor implements Serializable {

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

	private String name;

	@SerializedName("review_total")
	private String reviewTotal;

	@SerializedName("view_total")
	private String viewTotal;

	private String avatar;

	@SerializedName("avatar_status")
	private int avatarStatus;

	private float score;

	@SerializedName("price")
	private float priceScore;

	@SerializedName("technology")
	private float technologyScore;

	@SerializedName("efficiency")
	private float efficiencyScore;

	@SerializedName("receive")
	private float receptionScore;

	@SerializedName("environment")
	private float environmentScore;

	private String cas;
	private String address;

	@SerializedName("tel")
	private String telePhone;

	private String mobile;

	@SerializedName("area_code")
	private String cityCode;

	@SerializedName("point_x")
	private float pointX;

	@SerializedName("point_y")
	private float pointY;

	private String answerTime;

	private String introduction;

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

	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		if (null == avatar || avatar.isEmpty()) {
			this.avatar = Constants.URL_DEFAULTICON;
		} else {
			this.avatar = avatar;
		}
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReviewTotal() {
		return this.reviewTotal;
	}

	public void setReviewTotal(String num) {
		this.reviewTotal = num;
	}

	public String getViewTotal() {
		return this.viewTotal;
	}

	public void setViewTotal(String num) {
		this.viewTotal = num;
	}

	public float getScore() {
		return this.score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public float getPriceScore() {
		return this.priceScore;
	}

	public void setPriceScore(float price) {
		this.priceScore = price;
	}

	public float getTechnologyScore() {
		return this.technologyScore;
	}

	public void setTechnologyScore(float tech) {
		this.technologyScore = tech;
	}

	public float getEfficiencyScore() {
		return this.efficiencyScore;
	}

	public void setEfficiencyScore(float eff) {
		this.efficiencyScore = eff;
	}

	public float getReceptionScore() {
		return this.receptionScore;
	}

	public void setReceptioncore(float rev) {
		this.receptionScore = rev;
	}

	public float getEnvironmentScore() {
		return this.environmentScore;
	}

	public void setEnvironmentScore(float env) {
		this.environmentScore = env;
	}

	public String getCas() {
		return this.cas;
	}

	public void setCas(String cas) {
		this.cas = cas;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String addr) {
		this.address = addr;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephone() {
		return this.telePhone;
	}

	public void setTelephone(String tel) {
		this.telePhone = tel;
	}

	public String getCityCode() {
		return this.cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public float getPointX() {
		return this.pointX;
	}

	public void setPointX(float pointX) {
		this.pointX = pointX;
	}

	public float getPointY() {
		return this.pointY;
	}

	public void setPointY(float pointY) {
		this.pointY = pointY;
	}

	public String getAnswerTime() {
		return this.answerTime;
	}

	public void setAnswerTime(String ansTime) {
		this.answerTime = ansTime;
	}

	public String getIntroduction() {
		return this.introduction;
	}

	public void setIntroduction(String intro) {
		this.introduction = intro;
	}
}
