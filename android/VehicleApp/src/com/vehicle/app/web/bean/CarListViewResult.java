package com.vehicle.app.web.bean;

import java.util.List;
import java.util.Map;

import com.vehicle.app.bean.Car;

public class CarListViewResult extends WebCallBaseResult {

	private Map<String, List<Car>> result;

	@Override
	public List<Car> getInfoBean() {
		// TODO Auto-generated method stub
		// return isSuccess() ? JsonUtil.getSomeValueList(getResult(),
		// "Car.list", Car.class) : null;

		return result.get("Car.list");
	}

	@Override
	public Map<String, List<Car>> getResult() {
		// TODO Auto-generated method stub
		return result;
	}

	public void setResult(Map<String, List<Car>> result) {
		this.result = result;
	}
}
