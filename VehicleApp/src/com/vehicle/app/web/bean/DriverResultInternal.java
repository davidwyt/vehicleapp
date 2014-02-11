package com.vehicle.app.web.bean;

import com.google.gson.annotations.SerializedName;
import com.vehicle.app.bean.SelfDriver;

public class DriverResultInternal {
	
	@SerializedName("Personal")
	private SelfDriver driver;
	
	public SelfDriver getDriver()
	{
		return this.driver;
	}
	
	public void setDriver(SelfDriver driver)
	{
		this.driver = driver;
	}
}
