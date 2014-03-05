package com.vehicle.app.bean;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.vehicle.app.utils.StringUtil;

public class Comment implements Serializable {

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

	@SerializedName("add_date")
	private String addedDate;

	private String reviews;

	private String imgNamesS;
	private String imgNamesM;
	private String imgNamesL;

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

	public double getScore() {
		return this.score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getPriceScore() {
		return this.priceScore;
	}

	public void setPriceScore(double priceScore) {
		this.priceScore = priceScore;
	}

	public double getTechnologyScore() {
		return this.technologyScore;
	}

	public void setTechnologyScore(double s) {
		this.technologyScore = s;
	}

	public double getEfficiencyScore() {
		return this.efficiencyScore;
	}

	public void setEfficiencyScore(double score) {
		this.efficiencyScore = score;
	}

	public double getReceptionScore() {
		return this.receptionScore;
	}

	public void setReceptionScore(double reception) {
		this.receptionScore = reception;
	}

	public double getEnvironmentScore() {
		return this.environmentScore;
	}

	public void setEnvironmentScore(double score) {
		this.environmentScore = score;
	}

	public String getAddedDate() {
		return this.addedDate;
	}

	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}

	public String getReviews() {
		return StringUtil.filterHtml(this.reviews);
	}

	public void setReviews(String reviews) {
		this.reviews = reviews;
	}

	public String getImgNamesS() {
		return this.imgNamesS;
	}

	public void setImgNamesS(String names) {
		this.imgNamesS = names;
	}

	public String getImgNamesM() {
		return this.imgNamesM;
	}

	public void setImgNamesM(String names) {
		this.imgNamesM = names;
	}

	public String getImgNamesL() {
		return this.imgNamesL;
	}

	public void setImgNamesL(String names) {
		this.imgNamesL = names;
	}
}
