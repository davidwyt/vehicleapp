package com.vehicle.app.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.vehicle.app.utils.Constants;

public class Vendor implements Serializable {

	private static final long serialVersionUID = -1239603827677527705L;

	@SerializedName("id")
	private String id;

	private String name;

	private String avatar;

	@SerializedName("review_total")
	private String reviewTotal;

	@SerializedName("view_total")
	private String viewTotal;

	private double score;

	@SerializedName("price")
	private double priceScore;

	@SerializedName("technology")
	private double technologyScore;

	@SerializedName("efficiency")
	private double efficiencyScore;

	@SerializedName("receive")
	private double receptionScore;

	@SerializedName("environment")
	private double environmentScore;

	private String cas;
	private String address;

	@SerializedName("tel")
	private String telePhone;

	private String mobile;

	@SerializedName("cityCode")
	private String cityCode;

	@SerializedName("point_x")
	private float pointX;

	@SerializedName("point_y")
	private float pointY;

	@SerializedName("answer_time")
	private String answerTime;

	private String introduction;

	private String lastMessage;
	private Date lastMessageTime;

	private String distance;

	@SerializedName("BusinessSpecials.list")
	private List<VendorCoupon> coupons;

	@SerializedName("Review.list")
	private List<Comment> reviews;

	@SerializedName("BusinessActivity.list")
	private List<VendorPromotion> promotions;

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

	public double getScore() {
		return this.score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getPriceScore() {
		return this.priceScore;
	}

	public void setPriceScore(double price) {
		this.priceScore = price;
	}

	public double getTechnologyScore() {
		return this.technologyScore;
	}

	public void setTechnologyScore(double tech) {
		this.technologyScore = tech;
	}

	public double getEfficiencyScore() {
		return this.efficiencyScore;
	}

	public void setEfficiencyScore(double eff) {
		this.efficiencyScore = eff;
	}

	public double getReceptionScore() {
		return this.receptionScore;
	}

	public void setReceptioncore(double rev) {
		this.receptionScore = rev;
	}

	public double getEnvironmentScore() {
		return this.environmentScore;
	}

	public void setEnvironmentScore(double env) {
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

	public String getLastMessage() {
		return this.lastMessage;
	}

	public void setLastMessage(String lastMsg) {
		this.lastMessage = lastMsg;
	}

	public Date getLastMessageDate() {
		return this.lastMessageTime;
	}

	public void setLastMessageDate(Date lastMsgDate) {
		this.lastMessageTime = lastMsgDate;
	}

	public String getDistance() {
		return this.distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public List<VendorCoupon> getCoupons() {
		return this.coupons;
	}

	public void setCoupons(List<VendorCoupon> coupons) {
		this.coupons = coupons;
	}

	public List<Comment> getReviews() {
		return this.reviews;
	}

	public void setReviews(List<Comment> reviews) {
		this.reviews = reviews;
	}

	public List<VendorPromotion> getPromotions() {
		return this.promotions;
	}

	public void setPromotioms(List<VendorPromotion> promotions) {
		this.promotions = promotions;
	}
}
