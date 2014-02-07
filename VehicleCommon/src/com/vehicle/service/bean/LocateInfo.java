package com.vehicle.service.bean;

import java.util.Date;

public class LocateInfo {
	
	private String ownerId;
	private double locateX;
	private double locateY;
	private Date time;
	
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public double getLocateX() {
		return locateX;
	}
	public void setLocateX(double locateX) {
		this.locateX = locateX;
	}
	public double getLocateY() {
		return locateY;
	}
	public void setLocateY(double locateY) {
		this.locateY = locateY;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
}
