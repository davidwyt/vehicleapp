package com.vehicle.imserver.utils;

import java.util.Properties;

import com.vehicle.imserver.dao.bean.MessageType;

public class Contants {

	public static final String MESSAGE_FORMAT_SEPERATOR = "####";

	private static final String FILE_TRANSMISSION_ROOTPATH_WINDOWS_OTHERS = "D:\\temp\\";
	private static final String FILE_TRANSMISSION_ROOTPATH_WINDOWS_AUDIO = "D:\\temp\\audio\\";
	private static final String FILE_TRANSMISSION_ROOTPATH_WINDOWS_IMG = "D:\\temp\\img\\";

	private static final String FILE_TRANSMISSION_ROOTPATH_LINUX_AUDIO = "/usr/local/tomcat6.0/webapps/VehicleIMServer/file/audio";
	private static final String FILE_TRANSMISSION_ROOTPATH_LINUX_IMG = "/usr/local/tomcat6.0/webapps/VehicleIMServer/file/img";
	private static final String FILE_TRANSMISSION_ROOTPATH_LINUX_OTHERS = "/usr/local/tomcat6.0/webapps/VehicleIMServer/file/";

	private static final String COMMENTFILE_WATERMARK_WINDOWS = "D:\\temp\\watermark\\watermark.png";
	private static final String COMMENTFILE_WATERMARK_LINUX = "/usr/local/tomcat6.0/webapps/VehicleIMServer/watermark/watermark.png";

	public static final String HQL_SELECT_OFFLINE = "FROM com.vehicle.imserver.dao.bean.OfflineMessage WHERE target=:target and senttime>:senttime";
	public static final String HQL_SELECT_FOLLOWERS = "SELECT follower FROM com.vehicle.imserver.dao.bean.Followship WHERE followee=:followee";
	public static final String HQL_SELECT_FOLLOWEES = "SELECT followee FROM com.vehicle.imserver.dao.bean.Followship WHERE follower=:follower";
	public static final String HQL_DEL_OFFLINE = "DELETE from com.vehicle.imserver.dao.bean.OfflineMessage WHERE target=:target";
	public static final String HQL_SELECT_NEWFOLLOWINV = "FROM com.vehicle.imserver.dao.bean.FollowshipInvitation WHERE (target=:target and status=:requested) or (source=:source and (status=:accepted or status=:rejected))";
	public static final String HQL_UPDATE_NEWREQFOLLOWINV = "UPDATE com.vehicle.imserver.dao.bean.FollowshipInvitation SET status=:received WHERE target=:target and status=:requested";
	public static final String HQL_UPDATE_NEWDONFOLLOWINV = "UPDATE com.vehicle.imserver.dao.bean.FollowshipInvitation SET status=:done WHERE source=:source and (status=:accepted or status=:rejected)";

	public static final String HQL_SELECT_ALLNEWMESSAGE = "FROM com.vehicle.imserver.dao.bean.Message WHERE target=:target and status=:sent";
	public static final String HQL_SELECT_ALLNEWFILE = "FROM com.vehicle.imserver.dao.bean.FileTransmission WHERE target=:target and status=:sent";
	public static final String HQL_UPDATE_ALLNEWMESSAGE = "UPDATE com.vehicle.imserver.dao.bean.Message SET status=:received WHERE target=:target and status=:sent";

	public static final String HQL_SELECT_LATESTVERSION = "FROM com.vehicle.imserver.dao.bean.VersionInfo ORDER BY majorVersion DESC, minorVersion DESC";

	public static final int SMALLIMG_WIDTH = 150;
	public static final int SMALLIMG_HEIGHT = 150;
	public static final int MIDDLEIMG_WIDTH = 480;
	public static final int MIDDLEIMG_HEIGHT = 220;
	public static final int LARGEIMG_WIDTH = 750;
	public static final int LARGEIMG_HEIGHT = 750;

	public static final int IMG_TYPE_SMALL = 1;
	public static final int IMG_TYPE_MIDDLE = 2;
	public static final int IMG_TYPE_LARGE = 3;

	private static final String FILE_COMMENT_ROOTPATH_LINUX = "/wwwroot/ac0086.com/uploads/pics/";

	public static String getOS() {
		Properties prop = System.getProperties();

		String os = prop.getProperty("os.name");

		return os;
	}

	public static String getFileRootPath(int type) {

		String os = getOS();

		if (os.startsWith("win") || os.startsWith("Win")) {
			if (type == MessageType.AUDIO.ordinal())
				return FILE_TRANSMISSION_ROOTPATH_WINDOWS_AUDIO;
			else if (type == MessageType.IMAGE.ordinal()) {
				return FILE_TRANSMISSION_ROOTPATH_WINDOWS_IMG;
			} else {
				return FILE_TRANSMISSION_ROOTPATH_WINDOWS_OTHERS;
			}
		} else {
			if (type == MessageType.AUDIO.ordinal())
				return FILE_TRANSMISSION_ROOTPATH_LINUX_AUDIO;
			else if (type == MessageType.IMAGE.ordinal()) {
				return FILE_TRANSMISSION_ROOTPATH_LINUX_IMG;
			} else {
				return FILE_TRANSMISSION_ROOTPATH_LINUX_OTHERS;
			}
		}
	}

	public static String getImgSourcePath(int type) {
		String os = getOS();

		String path = "";
		if (os.startsWith("win") || os.startsWith("Win")) {
			path = FILE_TRANSMISSION_ROOTPATH_WINDOWS_IMG;
		} else {
			path = FILE_TRANSMISSION_ROOTPATH_LINUX_IMG;
		}

		if (IMG_TYPE_SMALL == type) {
			return FileUtil.AppendPath(path, "small");
		} else if (IMG_TYPE_MIDDLE == type) {
			return FileUtil.AppendPath(path, "middle");
		} else if (IMG_TYPE_LARGE == type) {
			return FileUtil.AppendPath(path, "large");

		} else {
			throw new IllegalArgumentException("what the hell of img type:"
					+ type);
		}

	}

	public static String getImgTargetPath(int type, String name) {
		String os = getOS();

		String path = "";
		if (os.startsWith("win") || os.startsWith("Win")) {
			path = FILE_TRANSMISSION_ROOTPATH_WINDOWS_IMG;
		} else {
			path = FILE_COMMENT_ROOTPATH_LINUX;
		}

		if (IMG_TYPE_SMALL == type) {
			path = FileUtil.AppendPath(path, "s");
		} else if (IMG_TYPE_MIDDLE == type) {
			path = FileUtil.AppendPath(path, "m");
		} else if (IMG_TYPE_LARGE == type) {
			path = FileUtil.AppendPath(path, "l");
		} else {
			throw new IllegalArgumentException("what the hell of img type:"
					+ type);
		}

		return FileUtil.AppendPath(path, name);
	}

	public static String getWaterMarkPath() {
		String os = getOS();

		if (os.startsWith("win") || os.startsWith("Win")) {
			return COMMENTFILE_WATERMARK_WINDOWS;
		} else {
			return COMMENTFILE_WATERMARK_LINUX;
		}
	}

}
