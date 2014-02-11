package com.vehicle.sdk.client;

import com.vehicle.app.web.bean.DriverInfoResult;
import com.vehicle.app.web.bean.DriverLoginResult;
import com.vehicle.app.web.bean.RegisterResult;
import com.vehicle.sdk.utils.HttpUtil;
import com.vehicle.sdk.utils.URLUtil;

public class VehicleWebClient {

	private final static String URL_DEFAULTROOT = "http://mobiles.ac0086.com";

	private final static String URL_DRIVER_LOGIN = "index/login?&userName=%s&pass=%s";
	private final static String URL_DRIVER_REGISTER = "personal/personalCreate?&userName=%s&pass=%s&email=%s";

	private final static String URL_SHOP_LOGIN = "business/businessLogin?&userName=%s&pass=%s";
	private final static String URL_SHOP_REGISTER = "business/businessCreate?&userName=%s&pass=%s&email=%s";
	private final static String URL_SHOPFELLOWS_RETRIEVE = "business/businessFavoriteList?shopId=%s";

	private final static String URL_DRIVERINFO_RETRIEVE = "personal/personalView?memberId=%s";
	private final static String URL_SHOPINFO_RETRIEVE = "business/businessView?memberId=%s";

	private final static String URL_CARS_RETRIEVE = "car/carList?memberId=%s";

	private final static String URL_FAVSHOPS_RETRIEVE = "favorite/favoriteList?memberId=%s";
	private final static String URL_FAVSHOPS_ADD = "favorite/favoriteCreate?memberId=%s&shopId=%s";
	private final static String URL_FAVSHOPS_REMOVE = "favorite/favoriteDel?memberId=%s&shopId=%s";

	private final static String URL_COMMENTS_RETRIEVE = "personal/personalReviewList?memberId=%s";
	private final static String URL_COMMENTS_ADD = "review/reviewCreate";

	public VehicleWebClient() {

	}

	public DriverLoginResult DriverLogin(String userName, String password) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_DRIVER_LOGIN);
		url = String.format(url, userName, password);

		return HttpUtil.PostJson(url, null, DriverLoginResult.class);
	}

	public RegisterResult Register(String email, String userName, String password) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_DRIVER_REGISTER);
		url = String.format(url, userName, password, email);

		return HttpUtil.PostJson(url, null, RegisterResult.class);
	}

	public DriverInfoResult GetDriverInfo(String memberId) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_DRIVERINFO_RETRIEVE);
		url = String.format(url, memberId);

		return HttpUtil.GetJson(url, DriverInfoResult.class);
	}
}
