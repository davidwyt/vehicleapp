package com.vehicle.imserver.utils;

public class Contants {
	
	public static final String MESSAGE_FORMAT_SEPERATOR = "####";
	public static final String FILE_TRANSMISSION_ROOTPATH = "D:\\temp\\";
	public static final String NOTIFICATION_NEWFILE_CONTENT = "%s sent to you a file";
	public static final String NOTIFICATION_NEWFILE_TITLE = "NEWFILE";
	
	
	public static final String HQL_SELECT_FOLLOWERS = "SELECT follower FROM com.vehicle.imserver.dao.bean.Followship WHERE followee=:followee";
	public static final String HQL_SELECT_FOLLOWEES = "SELECT followee FROM com.vehicle.imserver.dao.bean.Followship WHERE follower=:follower";
}
