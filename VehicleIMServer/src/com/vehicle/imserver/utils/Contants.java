package com.vehicle.imserver.utils;

public class Contants {
	
	public static final String MESSAGE_FORMAT_SEPERATOR = "####";
	public static final String FILE_TRANSMISSION_ROOTPATH = "./ft/";
	
	public static final String HQL_SELECT_FOLLOWERS = "SELECT follower FROM com.vehicle.imserver.persistence.dao.Followship WHERE followee=:followee";
	public static final String HQL_SELECT_FOLLOWEES = "SELECT followee FROM com.vehicle.imserver.persistence.dao.Followship WHERE follower=:follower";
}
