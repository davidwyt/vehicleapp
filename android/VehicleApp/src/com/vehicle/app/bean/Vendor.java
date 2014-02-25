package com.vehicle.app.bean;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.SerializedName;
import com.vehicle.app.utils.Constants;

public class Vendor implements Serializable {

	private static final long serialVersionUID = -1239603827677527705L;

	@SerializedName("id")
	private String id;

	protected String name;

	protected String avatar;

	@SerializedName("review_total")
	protected String reviewTotal;

	@SerializedName("view_total")
	protected String viewTotal;

	protected double score;

	@SerializedName("price")
	protected double priceScore;

	@SerializedName("technology")
	protected double technologyScore;

	@SerializedName("efficiency")
	protected double efficiencyScore;

	@SerializedName("receive")
	protected double receptionScore;

	@SerializedName("environment")
	protected double environmentScore;

	protected String cas;
	protected String address;

	@SerializedName("tel")
	protected String telePhone;

	protected String mobile;

	@SerializedName("cityCode")
	protected String cityCode;

	@SerializedName("point_x")
	protected float pointX;

	@SerializedName("point_y")
	protected float pointY;

	@SerializedName("answer_time")
	protected String answerTime;

	protected String introduction;

	private String lastMessage;
	private Date lastMessageTime;

	private String distance;

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
}
