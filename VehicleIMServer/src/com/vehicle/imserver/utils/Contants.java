package com.vehicle.imserver.utils;

import java.util.Properties;

public class Contants {

	public static final String MESSAGE_FORMAT_SEPERATOR = "####";

	private static final String FILE_TRANSMISSION_ROOTPATH_WINDOWS = "D:\\temp\\";
	private static final String FILE_TRANSMISSION_ROOTPATH_LINUX = "/usr/local/tomcat6.0/webapps/VehicleIMServer/file/";

	public static final String HQL_SELECT_OFFLINE = "FROM com.vehicle.imserver.dao.bean.OfflineMessage WHERE target=:target and senttime>:senttime";
	public static final String HQL_SELECT_FOLLOWERS = "SELECT follower FROM com.vehicle.imserver.dao.bean.Followship WHERE followee=:followee";
	public static final String HQL_SELECT_FOLLOWEES = "SELECT followee FROM com.vehicle.imserver.dao.bean.Followship WHERE follower=:follower";
	public static final String HQL_DEL_OFFLINE = "DELETE from com.vehicle.imserver.dao.bean.OfflineMessage WHERE target=:target";

	public static String getFileRootPath() {
		Properties prop = System.getProperties();

		String os = prop.getProperty("os.name");

		if (os.startsWith("win") || os.startsWith("Win")) {
			return FILE_TRANSMISSION_ROOTPATH_WINDOWS;
		} else {
			return FILE_TRANSMISSION_ROOTPATH_LINUX;
		}
	}
}
