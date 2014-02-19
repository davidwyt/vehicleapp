package com.vehicle.app.bean;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Comment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9055830979975028796L;

	@SerializedName("review_id")
	private String reviewId;

	@SerializedName("shop_id")
	private String vendorId;

	@SerializedName("member_id")
	private String driverId;

	@SerializedName("nick_name")
	private String alias;
	private String name;
	private String avatar;

	@SerializedName("main_project_id")
	private int mainProjectId;

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

	@SerializedName("add_date")
	private String addedDate;

	private String reviews;

	public String getReviewId() {
		return this.reviewId;
	}

	public void setReviewId(String id) {
		this.reviewId = id;
	}

	public String getVendorId() {
		return this.vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getDriverId() {
		return this.driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getName() {
		return this.name;
	}

	public void setname(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getMainProjectId() {
		return this.mainProjectId;
	}

	public void setMainProjectId(int id) {
		this.mainProjectId = id;
	}

	public float getScore() {
		return this.score;
	}

	public void setFloat(float score) {
		this.score = score;
	}

	public float getPriceScore() {
		return this.priceScore;
	}

	public void setPriceScore(float priceScore) {
		this.priceScore = priceScore;
	}

	public float getTechnologyScore() {
		return this.technologyScore;
	}

	public void setTechnologyScore(float s) {
		this.technologyScore = s;
	}

	public float getEfficiencyScore() {
		return this.efficiencyScore;
	}

	public void setEfficiencyScore(float score) {
		this.efficiencyScore = score;
	}

	public float getReceptionScore() {
		return this.receptionScore;
	}

	public void setReceptionScore(float reception) {
		this.receptionScore = reception;
	}

	public float getEnvironmentScore() {
		return this.environmentScore;
	}

	public void setEnvironmentScore(float socre) {
		this.environmentScore = score;
	}

	public String getAddedDate() {
		return this.addedDate;
	}

	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}

	public String getReviews() {
		return this.reviews;
	}

	public void setReviews(String reviews) {
		this.reviews = reviews;
	}
}
