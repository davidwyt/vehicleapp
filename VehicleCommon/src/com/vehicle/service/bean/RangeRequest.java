package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RangeRequest implements IRequest {
	
	private double centerX;
	private double centerY;
	private int range;
	
	@XmlElement
	public double getCenterX() {
		return centerX;
	}
	public void setCenterX(double centerX) {
		this.centerX = centerX;
	}
	
	@XmlElement
	public double getCenterY() {
		return centerY;
	}
	public void setCenterY(double centerY) {
		this.centerY = centerY;
	}
	@XmlElement
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}

}
