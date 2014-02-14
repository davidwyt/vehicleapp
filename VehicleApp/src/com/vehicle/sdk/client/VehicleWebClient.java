package com.vehicle.sdk.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vehicle.app.bean.Driver;
import com.vehicle.app.utils.HttpUtil;
import com.vehicle.app.utils.StringUtil;
import com.vehicle.app.utils.URLUtil;
import com.vehicle.app.web.bean.AddCommentResult;
import com.vehicle.app.web.bean.AddFavVendorResult;
import com.vehicle.app.web.bean.CarListViewResult;
import com.vehicle.app.web.bean.CommentListViewResult;
import com.vehicle.app.web.bean.DriverListViewResult;
import com.vehicle.app.web.bean.DriverViewResult;
import com.vehicle.app.web.bean.DriverLoginResult;
import com.vehicle.app.web.bean.DriverRegisterResult;
import com.vehicle.app.web.bean.FavVendorListViewResult;
import com.vehicle.app.web.bean.NearbyDriverListViewResult;
import com.vehicle.app.web.bean.NearbyVendorListViewResult;
import com.vehicle.app.web.bean.RemoveFavVendorResult;
import com.vehicle.app.web.bean.VendorFellowListViewResult;
import com.vehicle.app.web.bean.VendorListViewResult;
import com.vehicle.app.web.bean.VendorLoginResult;
import com.vehicle.app.web.bean.VendorRegisterResult;
import com.vehicle.app.web.bean.VendorViewResult;

public class VehicleWebClient {

	private final static String URL_DEFAULTROOT = "http://mobiles.ac0086.com";

	private final static String URL_DRIVER_LOGIN = "index/login?userName=%s&pass=%s";
	private final static String URL_DRIVER_REGISTER = "personal/personalCreate?userName=%s&pass=%s&email=%s";
	private final static String URL_DRIVERI_VIEW = "personal/personalView?memberId=%s";
	private final static String URL_DRIVERLIST_VIEW = "personal/personalListView?memberId=%s";

	private final static String URL_VENDOR_LOGIN = "business/businessLogin?userName=%s&pass=%s";
	private final static String URL_VENDOR_REGISTER = "business/businessCreate?userName=%s&pass=%s&email=%s";
	private final static String URL_VENDOR_VIEW = "business/businessView?memberId=%s";
	private final static String URL_VENDORLIST_VIEW = "business/businessListView?memberId=%s";
	private final static String URL_VENDORFELLOWS_VIEW = "business/businessFavoriteList?shopId=%s";

	private final static String URL_CARS_VIEW = "car/carList?memberId=%s";

	private final static String URL_FAVSHOPS_VIEW = "favorite/favoriteList?memberId=%s";
	private final static String URL_FAVSHOPS_ADD = "favorite/favoriteCreate?memberId=%s&shopId=%s";
	private final static String URL_FAVSHOPS_REMOVE = "favorite/favoriteDel?memberId=%s&shopId=%s";

	private final static String URL_COMMENTS_VIEW = "personal/personalReviewList?memberId=%s";
	private final static String URL_COMMENTS_ADD = "review/reviewCreate?memberId=%s&shopId=%s&reviews=%s&price=%s&technology=%s&efficiency=%s&receive=%s&environment=%s&mainProjectId=%s";

	private final static String URL_NEARBYVENDORLIST_VIEW = "member/memberBusinessProject2List?cityId=%s&areaId=%s&streetId=%s&pageId=%s&centerx=%s&centery=%s&range=%s&projectType=%s&projectId=%s&sort=%s";

	public VehicleWebClient() {

	}

	public DriverLoginResult DriverLogin(String userName, String password) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_DRIVER_LOGIN);
		url = String.format(url, userName, password);

		return HttpUtil.PostJson(url, null, DriverLoginResult.class);
	}

	public DriverRegisterResult DriverRegister(String email, String userName, String password) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_DRIVER_REGISTER);
		url = String.format(url, userName, password, email);

		return HttpUtil.PostJson(url, null, DriverRegisterResult.class);
	}

	public DriverViewResult DriverView(String memberId) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_DRIVERI_VIEW);
		url = String.format(url, memberId);

		return HttpUtil.GetJson(url, DriverViewResult.class);
	}

	public DriverListViewResult DriverListView(List<String> drivers) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_DRIVERLIST_VIEW);
		String params = StringUtil.JointString(drivers, ",");
		url = String.format(url, params);

		return HttpUtil.GetJson(url, DriverListViewResult.class);
	}

	public VendorLoginResult VendorLogin(String userName, String password) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_VENDOR_LOGIN);
		url = String.format(url, userName, password);
		return HttpUtil.PostJson(url, null, VendorLoginResult.class);
	}

	public VendorRegisterResult VendorRegister(String email, String userName, String password) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_VENDOR_REGISTER);
		url = String.format(url, userName, password, email);

		return HttpUtil.PostJson(url, null, VendorRegisterResult.class);
	}

	public VendorViewResult VendorView(String vendorId) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_VENDOR_VIEW);
		url = String.format(url, vendorId);

		return HttpUtil.GetJson(url, VendorViewResult.class);
	}

	public VendorListViewResult VendorListView(List<String> vendors) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_VENDORLIST_VIEW);
		String params = StringUtil.JointString(vendors, ",");
		url = String.format(url, params);

		return HttpUtil.GetJson(url, VendorListViewResult.class);
	}

	public CarListViewResult CarListView(String driverId) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_CARS_VIEW);
		url = String.format(url, driverId);

		return HttpUtil.GetJson(url, CarListViewResult.class);
	}

	public FavVendorListViewResult FavVendorListView(String driverId) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_FAVSHOPS_VIEW);
		url = String.format(url, driverId);

		return HttpUtil.GetJson(url, FavVendorListViewResult.class);
	}

	public AddFavVendorResult AddFavVendor(String driverId, String vendorId) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_FAVSHOPS_ADD);
		url = String.format(url, driverId, vendorId);

		return HttpUtil.PostJson(url, null, AddFavVendorResult.class);
	}

	public RemoveFavVendorResult RemoveFavVendor(String driverId, String vendorId) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_FAVSHOPS_REMOVE);
		url = String.format(url, driverId, vendorId);

		return HttpUtil.PostJson(url, null, RemoveFavVendorResult.class);
	}

	public VendorFellowListViewResult VendorFellowListView(String vendorId) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_VENDORFELLOWS_VIEW);
		url = String.format(url, vendorId);

		return HttpUtil.GetJson(url, VendorFellowListViewResult.class);
	}

	public CommentListViewResult CommenListView(String driverId) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_COMMENTS_VIEW);
		url = String.format(url, driverId);

		return HttpUtil.GetJson(url, CommentListViewResult.class);
	}

	public AddCommentResult AddComment(String driverId, String vendorId, String reviews, float priceScore,
			float technologyScore, float efficiencyScore, float receptionScore, int mainProjectId) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_COMMENTS_ADD);
		url = String.format(url, driverId, vendorId, reviews, priceScore, technologyScore, efficiencyScore,
				receptionScore, mainProjectId);

		return HttpUtil.PostJson(url, null, AddCommentResult.class);
	}

	public NearbyVendorListViewResult NearbyVendorListView(int cityId, int areaId, int streetId, int pageId,
			double centerX, double centerY, int range, int projectType, int projectId, int sort) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_NEARBYVENDORLIST_VIEW);
		url = String.format(url, cityId, areaId, streetId, pageId, centerX, centerY, range, projectType, projectId,
				sort);

		return HttpUtil.GetJson(url, NearbyVendorListViewResult.class);
	}

	public NearbyDriverListViewResult NearbyDriverListView(int cityId, double centerX, double centerY) {
		List<String> driverIds = new ArrayList<String>();
		driverIds.add("18755");
		driverIds.add("18726");
		driverIds.add("18727");

		DriverListViewResult driverListResult = DriverListView(driverIds);
		List<Driver> driverList = new ArrayList<Driver>();
		driverList.addAll(driverListResult.getInfoBean().values());

		Map<String, List<Driver>> result = new HashMap<String, List<Driver>>();
		result.put("NearbyDriver.list", driverList);

		NearbyDriverListViewResult nearbyDriverListResult = new NearbyDriverListViewResult();
		nearbyDriverListResult.setResult(result);

		return nearbyDriverListResult;
	}
}
