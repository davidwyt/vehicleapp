package com.vehicle.app.web.bean;

import com.google.gson.annotations.SerializedName;
import com.vehicle.app.bean.Driver;

public class DriverLoginResult extends WebCallBaseResult{

	@SerializedName("result")
	private DriverResultInternal result;
	
	@Override
	public Object getResult() {
		// TODO Auto-generated method stub
		return result;
	}
	
	public void setResult(DriverResultInternal result)
	{
		this.result = result;
	}
	
	public Driver getDriverInfo()
	{
		return this.result.getDriver();
	}
}
