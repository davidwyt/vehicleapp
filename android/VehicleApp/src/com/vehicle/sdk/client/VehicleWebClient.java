package com.vehicle.sdk.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.HttpUtil;
import com.vehicle.app.utils.StringUtil;
import com.vehicle.app.utils.URLUtil;
import com.vehicle.app.web.bean.AddCommentResult;
import com.vehicle.app.web.bean.AddFavVendorResult;
import com.vehicle.app.web.bean.AdviceResult;
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
import com.vehicle.app.web.bean.VendorImgViewResult;
import com.vehicle.app.web.bean.VendorListViewResult;
import com.vehicle.app.web.bean.VendorLoginResult;
import com.vehicle.app.web.bean.VendorRegisterResult;
import com.vehicle.app.web.bean.VendorSpecViewResult;
import com.vehicle.app.web.bean.VendorViewResult;
import com.vehicle.app.web.bean.WebCallBaseResult;

public class VehicleWebClient {

	private final static String URL_DEFAULTROOT = "http://mobiles.ac0086.com";

	private final static String URL_DRIVER_LOGIN = "index/login?userName=%s&pass=%s";
	private final static String URL_DRIVER_REGISTER = "personal/personalCreate?userName=%s&pass=%s&email=%s";
	private final static String URL_DRIVERI_VIEW = "personal/personalView?memberId=%s";
	private final static String URL_DRIVERLIST_VIEW = "personal/personalListView?memberId=%s";

	private final static String URL_VENDOR_LOGIN = "business/businessLogin?userName=%s&pass=%s";
	private final static String URL_VENDOR_REGISTER = "business/businessCreate?userName=%s&pass=%s&email=%s";
	private final static String URL_VENDOR_VIEW = "business/businessView?memberId=%s";
	private final static String URL_VENDOR_SPECVIEW = "business/businessMulList?memberId=%s";
	private final static String URL_VENDOR_IMGVIEW = "business/businessImagesList?memberId=%s";
	private final static String URL_VENDORLIST_VIEW = "business/businessListView?memberId=%s";
	private final static String URL_VENDORFELLOWS_VIEW = "business/businessFavoriteList?shopId=%s";

	private final static String URL_CARS_VIEW = "car/carList?memberId=%s";

	private final static String URL_FAVSHOPS_VIEW = "favorite/favoriteList?memberId=%s";
	private final static String URL_FAVSHOPS_ADD = "favorite/favoriteCreate?memberId=%s&shopId=%s";
	private final static String URL_FAVSHOPS_REMOVE = "favorite/favoriteDel?memberId=%s&shopId=%s";

	private final static String URL_COMMENTS_VIEW = "personal/personalReviewList?memberId=%s";
	private final static String URL_COMMENTS_ADD = "review/reviewCreate2?memberId=%s&shopId=%s&reviews=%s&price=%s&technology=%s&efficiency=%s&receive=%s&environment=%s&mainProjectId=%s&imgNames=%s";

	private final static String URL_NEARBYVENDORLIST_VIEW = "member/memberBusinessProject2List?cityId=%s&areaId=%s&streetId=%s&pageId=%s&centerx=%s&centery=%s&range=%s&projectType=%s&projectId=%s&sort=%s";

	private final static String URL_ADVICE_CREATE = "suggest/suggestCreate?memberId=%s&tel=%s&email=%s&content=%s";
	private final static String URL_ECC_CREATE = "correction/correctionCreate?&memberId=%s&shopId=%s&email=%s&content=%s";

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
		String params = StringUtil.JointString(drivers, Constants.COMMA);
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

	public VendorSpecViewResult VendorSpecView(String vendorId) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_VENDOR_SPECVIEW);
		url = String.format(url, vendorId);

		return HttpUtil.GetJson(url, VendorSpecViewResult.class);
	}

	public VendorImgViewResult VendorImgView(String vendorId) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_VENDOR_IMGVIEW);
		url = String.format(url, vendorId);

		return HttpUtil.GetJson(url, VendorImgViewResult.class);
	}

	public VendorListViewResult VendorListView(List<String> vendors) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_VENDORLIST_VIEW);
		String params = StringUtil.JointString(vendors, Constants.COMMA);
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

	public AddCommentResult AddComment(String driverId, String vendorId, String reviews, double priceScore,
			double technologyScore, double efficiencyScore, double receptionScore, double envScore, int mainProjectId,
			String imgNames) throws UnsupportedEncodingException {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_COMMENTS_ADD);

		if (null == imgNames || imgNames.length() == 0) {
			imgNames = Constants.IMGNAME_DIVIDER;
		}

		url = String.format(url, driverId, vendorId, URLEncoder.encode(reviews, "UTF-8"), priceScore, technologyScore,
				efficiencyScore, receptionScore, envScore, mainProjectId, URLEncoder.encode(imgNames, "UTF-8"));

		// url = url.replace(" ", "%20");

		System.out.println("url:" + url);

		return HttpUtil.PostJson(url, null, AddCommentResult.class);
	}

	public NearbyVendorListViewResult NearbyVendorListView(int cityId, int areaId, int streetId, int pageId,
			double centerX, double centerY, int range, int projectType, int projectId, int sort) {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_NEARBYVENDORLIST_VIEW);
		url = String.format(url, cityId, areaId, streetId, pageId, centerX, centerY, 6, projectType, projectId,
				sort);

		return HttpUtil.GetJson(url, NearbyVendorListViewResult.class);
	}

	public NearbyDriverListViewResult NearbyDriverListView(int cityId, double centerX, double centerY) {
		List<String> driverIds = new ArrayList<String>();
		driverIds.add("18755");
		driverIds.add("18726");
		driverIds.add("18727");

		DriverListViewResult driverListResult = DriverListView(driverIds);

		NearbyDriverListViewResult nearbyDriverListResult = new NearbyDriverListViewResult();
		nearbyDriverListResult.setCode(WebCallBaseResult.CODE_SUCCESS);
		nearbyDriverListResult.setResult(driverListResult.getInfoBean());

		return nearbyDriverListResult;
	}

	public AdviceResult CreateAdvice(String memberId, String email, String phone, String content)
			throws UnsupportedEncodingException {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_ADVICE_CREATE);

		url = String.format(url, memberId, phone, URLEncoder.encode(email, "UTF-8"),
				URLEncoder.encode(content, "UTF-8"));

		return HttpUtil.PostJson(url, null, AdviceResult.class);
	}

	public AdviceResult CreateECC(String memberId, String shopId, String email, String content)
			throws UnsupportedEncodingException {
		String url = URLUtil.UrlAppend(URL_DEFAULTROOT, URL_ECC_CREATE);

		url = String.format(url, memberId, shopId, URLEncoder.encode(email, "UTF-8"),
				URLEncoder.encode(content, "UTF-8"));
		return HttpUtil.PostJson(url, null, AdviceResult.class);
	}
}