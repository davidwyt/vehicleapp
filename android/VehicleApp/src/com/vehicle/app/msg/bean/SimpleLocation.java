package com.vehicle.app.msg.bean;

import java.io.Serializable;

public class SimpleLocation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8710168035526740604L;
	private double longitude;
	private double latitude;

	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}
