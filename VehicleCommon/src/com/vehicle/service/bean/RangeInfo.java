package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RangeInfo {

	private String ownerId;
	private double distance;
	private long during;

	@XmlElement
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@XmlElement
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@XmlElement
	public long getDuring() {
		return during;
	}

	public void setDuring(long during) {
		this.during = during;
	}

}
