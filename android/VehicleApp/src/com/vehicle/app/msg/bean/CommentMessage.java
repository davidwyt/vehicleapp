package com.vehicle.app.msg.bean;

public class CommentMessage implements IMessageItem {

	private String selfId;
	private String vendorId;

	private int mainProjectId;

	private double priceScore;

	private double technologyScore;

	private double efficiencyScore;

	private double receptionScore;

	private double environmentScore;

	private String comment;

	public int getMainProjectId() {
		return this.mainProjectId;
	}

	public void setMainProjectId(int id) {
		this.mainProjectId = id;
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

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return this.selfId;
	}

	public void setSource(String src) {
		this.selfId = src;
	}

	@Override
	public String getTarget() {
		// TODO Auto-generated method stub
		return this.vendorId;
	}

	public void setTarget(String tar) {
		this.vendorId = tar;
	}

	@Override
	public MessageFlag getFlag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getSentTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMessageType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void fromRawNotification(Object notification) {
		// TODO Auto-generated method stub

	}

}
