package com.vehicle.sdk.client;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			VehicleClient client = new VehicleClient("345");
			client.SendMessage("123", "hello girl");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
