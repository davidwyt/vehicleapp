package com.vehicle.service.bean;

public class RangeRequest implements IRequest {
	
	private double startX;
	private double endX;
	private double startY;
	private double endY;
	public double getStartX() {
		return startX;
	}
	public void setStartX(double startX) {
		this.startX = startX;
	}
	public double getEndX() {
		return endX;
	}
	public void setEndX(double endX) {
		this.endX = endX;
	}
	public double getStartY() {
		return startY;
	}
	public void setStartY(double startY) {
		this.startY = startY;
	}
	public double getEndY() {
		return endY;
	}
	public void setEndY(double endY) {
		this.endY = endY;
	}

}
