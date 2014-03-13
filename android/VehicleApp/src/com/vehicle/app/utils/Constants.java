package com.vehicle.app.utils;

import com.amap.api.maps.model.LatLng;

public class Constants {

	public static final String ACTION_TEXTMESSAGE_RECEIVED = "com.vehicle.app.action.textmsgreceived";
	public static final String ACTION_TEXTMESSAGE_SENTOK = "com.vehicle.app.action.textmsgack";
	public static final String ACTION_FILEMSG_RECEIVED = "com.vehicle.app.action.filereceived";
	public static final String ACTION_FILEMSG_SENTOK = "com.vehicle.app.action.filesentok";
	public static final String ACTION_TEXTMESSAGE_SENTFAILED = "com.vehicle.app.action.textmsg.failed";
	public static final String ACTION_IMGMESSAGE_SENTFAILED = "com.vehicle.app.action.imgmsg.failed";
	public static final String ACTION_AUDIOMESSAGE_SENTFAILED = "com.vehicle.app.action.audiomsg.failed";
	public static final String ACTION_LOCMESSAGE_SENTFAILED = "com.vehicle.app.action.locmsg.failed";
	public static final String ACTION_FOLLOWSHIP_SUCCESS = "com.vehicle.app.action.followship.success";
	public static final String ACTION_FOLLOWSHIP_FAILED = "com.vehicle.app.action.followship.failed";
	public static final String ACTION_INVITATION_SUCCESS = "com.vehicle.app.action.invitation.success";
	public static final String ACTION_INVITATION_FAILED = "com.vehicle.app.action.invitation.failed";
	public static final String ACTION_INVVERDICT_ACCEPTSUCCESS = "com.vehicle.app.action.invitationverdict.acceptsuccess";
	public static final String ACTION_INVVERDICT_ACCEPTFAILED = "com.vehicle.app.action.invitationverdict.acceptfailed";
	public static final String ACTION_INVVERDICT_REJECTSUCCESS = "com.vehicle.app.action.invitationverdict.rejectsuccess";
	public static final String ACTION_INVVERDICT_REJECTFAILED = "com.vehicle.app.action.invitationverdict.rejectfailed";
	public static final String ACTION_VENDORCOMMENT_SUCCESS = "com.vehicle.app.action.vendorcomment.success";
	public static final String ACTION_VENDORCOMMENT_FAILED = "com.vehicle.app.action.vendorcomment.failed";
	public static final String ACTION_RECENTMSG_UPDATE = "com.vehicle.app.action.recentmsgupdate";

	public static String SERVERURL = "http://10.0.2.2:8080/VehicleIMServer/rest";

	public static String URL_DEFAULTICON = "http://www.ac0086.com/uploads/face1.png";

	public static final LatLng BEIJING = new LatLng(39.90403, 116.407525);// 北京市经纬度
	public static final LatLng ZHONGGUANCUN = new LatLng(39.983456, 116.3154950);// 北京市中关村经纬度
	public static final LatLng SHANGHAI = new LatLng(31.238068, 121.501654);// 上海市经纬度
	public static final LatLng FANGHENG = new LatLng(39.989614, 116.481763);// 方恒国际中心经纬度
	public static final LatLng CHENGDU = new LatLng(30.679879, 104.064855);// 成都市经纬度
	public static final LatLng XIAN = new LatLng(34.341568, 108.940174);// 西安市经纬度
	public static final LatLng ZHENGZHOU = new LatLng(34.7466, 113.625367);// 郑州市经纬度

	public static final String COMMA = ",";

	public static final String IMGNAME_DIVIDER = "|";

	public static final String MIDDLEIMG_PATH = "http://www.ac0086.com/uploads/pics/m/";

	public static final double LOCATION_DEFAULT_LATITUDE = 31.025885;
	public static final double LOCATION_DEFAULT_LONGTITUDE = 121.428689;

	public static final int LOCATION_DEFAULT_NEARBYDRIVERDISTANCE = 10;
	public static final int LOCATION_DEFAULT_NEARBYVENDORDISTANCE = 30;

	public static String getMiddleVendorImg(String name) {
		return URLUtil.UrlAppend(MIDDLEIMG_PATH, name);
	}

	public static final String POSTFIX_DEFAULT_IMAGE = "jpg";
	public static final String POSTFIX_DEFAULT_AUDIO = "amr";
}
