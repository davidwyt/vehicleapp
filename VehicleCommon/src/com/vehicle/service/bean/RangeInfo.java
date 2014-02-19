package com.vehicle.service.bean;

public class RangeInfo {
	
	private String ownerId;
	private double distance;
	private long during;
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public long getDuring() {
		return during;
	}
	public void setDuring(long during) {
		this.during = during;
	}

}
