package com.vehicle.app.bean;

import java.io.Serializable;
import java.util.Date;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class Vendor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1239603827677527705L;

	@SerializedName("id")
	private String id;

	private String name;

	private String avatar;

	@SerializedName("review_total")
	private String reviewTotal;

	@SerializedName("view_total")
	private String viewTotal;

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

	@SerializedName("cityCode")
	private String cityCode;

	@SerializedName("point_x")
	private float pointX;

	@SerializedName("point_y")
	private float pointY;

	@SerializedName("answer_time")
	private String answerTime;

	private String introduction;

	transient private Bitmap icon;
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
		this.avatar = avatar;
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

	public Bitmap getIcon() {
		return this.icon;
	}

	public void setIcon(Bitmap icon) {
		this.icon = icon;
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
