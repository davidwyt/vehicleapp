package com.vehicle.app.web.bean;

import com.google.gson.annotations.SerializedName;
import com.vehicle.app.bean.Driver;

public class DriverResultInternal {
	
	@SerializedName("Personal")
	private Driver driver;
	
	public Driver getDriver()
	{
		return this.driver;
	}
	
	public void setDriver(Driver driver)
	{
		this.driver = driver;
	}
}
