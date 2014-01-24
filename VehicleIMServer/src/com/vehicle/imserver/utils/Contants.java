package com.vehicle.imserver.utils;

public class Contants {
	
	public static final String MESSAGE_FORMAT_SEPERATOR = "####";
	public static final String FILE_TRANSMISSION_ROOTPATH = "D:\\temp\\";
	
	public static final String HQL_SELECT_OFFLINE="FROM com.vehicle.imserver.dao.bean.OfflineMessage WHERE target=:target and senttime>:senttime";
	public static final String HQL_SELECT_FOLLOWERS = "SELECT follower FROM com.vehicle.imserver.dao.bean.Followship WHERE followee=:followee";
	public static final String HQL_SELECT_FOLLOWEES = "SELECT followee FROM com.vehicle.imserver.dao.bean.Followship WHERE follower=:follower";
	public static final String HQL_DEL_OFFLINE="DELETE from com.vehicle.imserver.dao.bean.OfflineMessage WHERE target=:target";
}
