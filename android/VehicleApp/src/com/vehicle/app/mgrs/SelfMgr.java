package com.vehicle.app.mgrs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import android.content.Context;

import cn.edu.sjtu.vehicleapp.R;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.FavoriteVendor;
import com.vehicle.app.bean.SelfDriver;
import com.vehicle.app.bean.SelfVendor;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.bean.VendorDetail;
import com.vehicle.app.bean.VendorFellow;
import com.vehicle.app.msg.bean.SimpleLocation;
import com.vehicle.app.msg.worker.IMessageCourier;
import com.vehicle.app.msg.worker.WakeupMessageCourier;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.JPushAliasMaker;
import com.vehicle.app.utils.StringUtil;
import com.vehicle.app.web.bean.CarListViewResult;
import com.vehicle.app.web.bean.CommentListViewResult;
import com.vehicle.app.web.bean.DriverListViewResult;
import com.vehicle.app.web.bean.DriverViewResult;
import com.vehicle.app.web.bean.FavVendorListViewResult;
import com.vehicle.app.web.bean.NearbyVendorListViewResult;
import com.vehicle.app.web.bean.VendorFellowListViewResult;
import com.vehicle.app.web.bean.VendorListViewResult;
import com.vehicle.app.web.bean.VendorSpecViewResult;
import com.vehicle.app.web.bean.VendorViewResult;
import com.vehicle.app.web.bean.WebCallBaseResult;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.sdk.client.VehicleWebClient;
import com.vehicle.service.bean.RangeInfo;
import com.vehicle.service.bean.RangeResponse;

public class SelfMgr {

	public static final int ROLENUM_DRIVER = 1;
	public static final int ROLENUM_VENDOR = 2;

	private SelfMgr() {

		mFavVendorSimpleVector = new Vector<FavoriteVendor>();
		mVendorFellowSimpleVector = new Vector<VendorFellow>();

		mNearbyVendorMap = new Hashtable<String, Vendor>();
		mNearbyDriverMap = new Hashtable<String, Driver>();

		mVendorFellowMap = new Hashtable<String, Driver>();
		mFavVendorMap = new Hashtable<String, Vendor>();

		mNearbyVendorDetailMap = new Hashtable<String, VendorDetail>();
		mFavVendorDetailMap = new Hashtable<String, VendorDetail>();

		mUnknowVendorDetailMap = new Hashtable<String, VendorDetail>();

		unknownDriversMap = new Hashtable<String, Driver>();
		unknowsVendorsMap = new Hashtable<String, Vendor>();
	}

	private static class InstanceHolder {
		private static SelfMgr instance = new SelfMgr();
	}

	public static SelfMgr getInstance() {
		return InstanceHolder.instance;
	}

	private boolean mIsDriver;

	public boolean isDriver() {
		return this.mIsDriver;
	}

	public void setIsDriver(boolean isDriver) {
		this.mIsDriver = isDriver;
	}

	private SelfDriver mSelfDriver = null;
	private SelfVendor mSelfVendor = null;
	private VendorDetail selfVendorDetail = null;

	private Vector<FavoriteVendor> mFavVendorSimpleVector;
	private Vector<VendorFellow> mVendorFellowSimpleVector;

	private Map<String, Vendor> mNearbyVendorMap;
	private Map<String, Driver> mNearbyDriverMap;

	private Map<String, Driver> mVendorFellowMap;
	private Map<String, Vendor> mFavVendorMap;

	private Map<String, VendorDetail> mNearbyVendorDetailMap;
	private Map<String, VendorDetail> mFavVendorDetailMap;
	private Map<String, VendorDetail> mUnknowVendorDetailMap;

	private Map<String, Driver> unknownDriversMap;
	private Map<String, Vendor> unknowsVendorsMap;

	public Map<String, VendorDetail> getNearbyVendorDetailMap() {
		return this.mNearbyVendorDetailMap;
	}

	public boolean isNearbyVendorDetailExist(String id) {
		return this.mNearbyVendorDetailMap.containsKey(id);
	}

	public void updateNearbyVendorDetail(VendorDetail detail) {
		if (null != detail)
			this.mNearbyVendorDetailMap.put(detail.getVendor().getId(), detail);
	}

	public VendorDetail getNearbyVendorDetail(String id) {
		return this.mNearbyVendorDetailMap.get(id);
	}

	public Map<String, VendorDetail> getFavVendorDetailMap() {
		return this.mFavVendorDetailMap;
	}

	public boolean isFavVendorDetailExist(String id) {
		return this.mFavVendorDetailMap.containsKey(id);
	}

	public void updateFavVendorDetail(VendorDetail detail) {
		if (null != detail)
			this.mFavVendorDetailMap.put(detail.getVendor().getId(), detail);
	}

	public VendorDetail getFavVendorDetail(String id) {
		return this.mFavVendorDetailMap.get(id);
	}

	public SelfDriver getSelfDriver() {
		return this.mSelfDriver;
	}

	public void setSelfDriver(SelfDriver self) {
		this.mSelfDriver = self;
	}

	public SelfVendor getSelfVendor() {
		return this.mSelfVendor;
	}

	public void setSelfVendor(SelfVendor self) {
		this.mSelfVendor = self;
	}

	public void setSelfVendorDetail(VendorDetail detail) {
		this.selfVendorDetail = detail;
	}

	public VendorDetail getSelfVendorDetail() {
		return this.selfVendorDetail;
	}

	public String getId() {
		return mIsDriver ? mSelfDriver.getId() : mSelfVendor.getId();
	}

	public boolean IsSelf(String id) {
		if (!this.isLogin())
			return false;

		return this.getId().equals(id);
	}

	public Vector<FavoriteVendor> getFavVendorSimpleVector() {
		return this.mFavVendorSimpleVector;
	}

	public boolean isFavoriteVendor(String id) {
		if (null == this.mFavVendorMap) {
			return false;
		}

		return this.mFavVendorMap.containsKey(id);
	}

	public boolean isVendorFellow(String driverId) {
		if (null == this.mVendorFellowMap) {
			return false;
		}

		return this.mVendorFellowMap.containsKey(driverId);
	}

	public FavoriteVendor getSimpleFavVendor(String id) {

		if (null == this.mFavVendorSimpleVector) {
			return null;
		}

		synchronized (this.mFavVendorSimpleVector) {

			for (FavoriteVendor vendor : this.mFavVendorSimpleVector) {
				if (id.equals(vendor.getId()))
					return vendor;
			}

			return null;
		}
	}

	public VendorFellow getSimpleVendorFellow(String id) {
		if (null == this.mVendorFellowSimpleVector) {
			return null;
		}
		synchronized (this.mVendorFellowSimpleVector) {
			for (VendorFellow driver : this.mVendorFellowSimpleVector) {
				if (id.equals(driver.getId())) {
					return driver;
				}
			}

			return null;
		}
	}

	public Vendor getNearbyVendor(String id) {
		return this.mNearbyVendorMap.get(id);
	}

	public Driver getNearbyDriver(String id) {
		return this.mNearbyDriverMap.get(id);
	}

	public Collection<Vendor> getNearbyVendors() {
		return this.mNearbyVendorMap.values();
	}

	public Collection<Driver> getNearbyDrivers() {
		return this.mNearbyDriverMap.values();
	}

	public boolean isNearbyVendor(String id) {
		return this.mNearbyVendorMap.containsKey(id);
	}

	public boolean isNearbyDriver(String id) {
		return this.mNearbyDriverMap.containsKey(id);
	}

	public void addNearbyVendor(Vendor vendor) {
		if (null != vendor)
			this.mNearbyVendorMap.put(vendor.getId(), vendor);
	}

	public void addNearbyDriver(Driver driver) {
		if (null != driver)
			this.mNearbyDriverMap.put(driver.getId(), driver);
	}

	public void addNearbyVendors(Collection<Vendor> vendors) {
		for (Vendor vendor : vendors) {
			this.addNearbyVendor(vendor);
		}
	}

	public void addNearbyDrivers(Collection<Driver> drivers) {
		for (Driver driver : drivers) {
			this.addNearbyDriver(driver);
		}
	}

	public void clearNearbyVendors() {
		this.mNearbyVendorMap.clear();
		this.mNearbyVendorDetailMap.clear();
	}

	public void clearNearbyDrivers() {
		this.mNearbyDriverMap.clear();
	}

	public void updateNearbyVendors(Collection<Vendor> vendors) {
		synchronized (this.mNearbyVendorMap) {
			this.mNearbyVendorMap.clear();
			this.addNearbyVendors(vendors);
		}
	}

	public void updateNearbyVendor(Vendor vendor) {
		this.mNearbyVendorMap.put(vendor.getId(), vendor);
	}

	public void updateNearbyDrivers(Collection<Driver> drivers) {
		synchronized (this.mNearbyDriverMap) {
			this.mNearbyDriverMap.clear();
			this.addNearbyDrivers(drivers);
		}
	}

	public Vector<VendorFellow> getVendorFellowList() {
		return this.mVendorFellowSimpleVector;
	}

	public Map<String, Driver> getVendorFellowMap() {
		return this.mVendorFellowMap;
	}

	public Map<String, Vendor> getFavVendorMap() {
		return this.mFavVendorMap;
	}

	public Driver getVendorFellow(String id) {
		if (null == this.mVendorFellowMap) {
			return null;
		}

		return this.mVendorFellowMap.get(id);
	}

	public Vendor getFavVendor(String id) {
		if (null == this.mFavVendorMap) {
			return null;
		}

		return this.mFavVendorMap.get(id);
	}

	public synchronized void refreshNearby(double longitude, double latitude, int pageNum) {
		this.clearNearby();
		try {
			if (mIsDriver) {
				VehicleWebClient client = new VehicleWebClient();

				NearbyVendorListViewResult result = client.NearbyVendorListView(1, pageNum, longitude, latitude, 6);

				if (null != result && result.isSuccess()) {
					List<Vendor> vendors = result.getInfoBean();
					if (null != vendors) {
						for (Vendor vendor : vendors) {
							this.mNearbyVendorMap.put(vendor.getId(), vendor);
						}
					}
				}
			} else {
				Map<String, Driver> result = this.searchNearbyDrivers(longitude, latitude,
						Constants.LOCATION_DEFAULT_NEARBYDRIVERDISTANCE);
				if (null != result) {
					this.mNearbyDriverMap.putAll(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, Driver> searchNearbyDrivers(double centerX, double centerY, int range) {
		try {
			VehicleClient client = new VehicleClient(this.getId());
			RangeResponse resp = client.NearbyDrivers(centerX, centerY, range);
			if (null == resp)
				return null;

			List<RangeInfo> infos = resp.getList();
			if (null == infos)
				return null;

			List<String> driverIds = new ArrayList<String>();
			for (RangeInfo info : infos) {
				driverIds.add(info.getOwnerId());
			}

			if (null != infos && infos.size() > 0) {
				VehicleWebClient webClient = new VehicleWebClient();

				DriverListViewResult result = webClient.DriverListView(driverIds);
				if (null == result || !result.isSuccess() || null == result.getInfoBean())
					return null;

				Map<String, Driver> drivers = result.getInfoBean();
				for (RangeInfo info : infos) {
					Driver driver = drivers.get(info.getOwnerId());
					if (null != driver) {
						driver.setDistance(info.getDistance());
						driver.setDuration(info.getDuring());
					}
				}

				return drivers;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public synchronized void refreshFellows() {
		try {
			VehicleWebClient webClient = new VehicleWebClient();

			if (mIsDriver) {
				this.mFavVendorSimpleVector.clear();
				this.mFavVendorMap.clear();
				this.mFavVendorDetailMap.clear();

				System.out.println("get iddddddddd:" + getId());

				FavVendorListViewResult result = webClient.FavVendorListView(getId());
				if (null == result || !result.isSuccess())
					return;

				List<FavoriteVendor> favVendors = result.getInfoBean();
				if (null == favVendors)
					return;

				this.mFavVendorSimpleVector.addAll(favVendors);

				List<String> vendorIds = new ArrayList<String>();
				for (FavoriteVendor vendor : this.mFavVendorSimpleVector) {
					vendorIds.add(vendor.getId());
				}

				VendorListViewResult vendorResult = webClient.VendorListView(vendorIds);
				if (null == vendorResult || !vendorResult.isSuccess())
					return;

				Map<String, Vendor> vendorMap = vendorResult.getInfoBean();
				if (null != vendorMap)
					this.mFavVendorMap.putAll(vendorMap);

			} else {
				this.mVendorFellowSimpleVector.clear();
				this.mVendorFellowMap.clear();
				VendorFellowListViewResult result = webClient.VendorFellowListView(getId());

				if (null == result || !result.isSuccess() || null == result.getInfoBean())
					return;

				this.mVendorFellowSimpleVector.addAll(result.getInfoBean());

				List<String> driverIds = new ArrayList<String>();
				for (VendorFellow fellow : this.mVendorFellowSimpleVector) {
					driverIds.add(fellow.getId());
				}

				DriverListViewResult driverResult = webClient.DriverListView(driverIds);
				if (null != driverResult && driverResult.isSuccess() && null != driverResult.getInfoBean()) {
					Map<String, Driver> drivers = driverResult.getInfoBean();
					for (Driver driver : drivers.values()) {
						try {
							if (null != driver) {
								CarListViewResult carResult = webClient.CarListView(driver.getId());
								if (null != carResult && null != carResult.getInfoBean()) {
									driver.setCars(carResult.getInfoBean());
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					this.mVendorFellowMap.putAll(driverResult.getInfoBean());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void refreshComments() {
		try {
			VehicleWebClient webClient = new VehicleWebClient();

			if (mIsDriver) {
				CommentListViewResult result = webClient.CommenListView(this.mSelfDriver.getId());
				if (null != result && result.isSuccess() && null != result.getInfoBean())
					this.mSelfDriver.setComments(result.getInfoBean());
			} else {
				VendorSpecViewResult result = webClient.VendorSpecView(this.mSelfVendor.getId());
				if (null != result && result.isSuccess() && null != result.getResult())
					this.selfVendorDetail.setReviews(result.getResult().getReviews());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void clearSelf() {
		this.selfVendorDetail = null;
		this.mSelfDriver = null;
		this.mSelfVendor = null;
	}

	public synchronized void clearFellows() {
		this.mFavVendorMap.clear();
		this.mFavVendorSimpleVector.clear();
		this.mFavVendorDetailMap.clear();

		this.mVendorFellowMap.clear();
		this.mVendorFellowSimpleVector.clear();
	}

	public synchronized void clearNearby() {
		this.mNearbyDriverMap.clear();
		this.mNearbyVendorMap.clear();

		this.mNearbyVendorDetailMap.clear();
	}

	public synchronized void clearUnknown() {
		this.mUnknowVendorDetailMap.clear();
		this.unknownDriversMap.clear();
		this.unknowsVendorsMap.clear();
	}

	public Driver getDriverInfo(String id) {
		if (this.mVendorFellowMap.containsKey(id)) {
			return this.mVendorFellowMap.get(id);
		}

		if (this.mNearbyDriverMap.containsKey(id)) {
			return this.mNearbyDriverMap.get(id);
		}

		if (this.unknownDriversMap.containsKey(id)) {
			return this.unknownDriversMap.get(id);
		}

		return null;
	}

	public void addUnknownDrivers(Map<String, Driver> map) {
		if (null != map) {
			Set<String> ids = map.keySet();
			if (null != ids) {
				for (String id : ids) {
					try {
						if (!StringUtil.IsNullOrEmpty(id)) {
							Driver driver = map.get(id);
							if (null != driver)
								this.unknownDriversMap.put(id, driver);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void addUnknownVendors(Map<String, Vendor> map) {
		if (null != map) {
			Set<String> ids = map.keySet();
			if (null != ids) {
				for (String id : ids) {
					try {
						if (!StringUtil.IsNullOrEmpty(id)) {
							Vendor vendor = map.get(id);
							if (null != vendor) {
								this.unknowsVendorsMap.put(id, vendor);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void putUnknownVendorDetail(VendorDetail detail) {
		if (null != detail)
			this.mUnknowVendorDetailMap.put(detail.getVendor().getId(), detail);
	}

	public VendorDetail getUnknownVendorDetail(String id) {
		return this.mUnknowVendorDetailMap.get(id);
	}

	public Vendor getVendorInfo(String id) {
		if (this.mFavVendorMap.containsKey(id)) {
			return this.mFavVendorMap.get(id);
		}

		if (this.mNearbyVendorMap.containsKey(id)) {
			return this.mNearbyVendorMap.get(id);
		}

		if (this.unknowsVendorsMap.containsKey(id)) {
			return this.unknowsVendorsMap.get(id);
		}

		return null;
	}

	public void retrieveInfo(String id) {
		if (this.isDriver()) {
			if (null == this.getVendorInfo(id)) {
				try {
					VehicleWebClient webClient = new VehicleWebClient();
					VendorViewResult result = webClient.VendorView(id);
					if (null != result && result.isSuccess()) {
						Vendor vendor = result.getInfoBean();
						if (null != vendor)
							this.unknowsVendorsMap.put(vendor.getId(), vendor);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			if (null == this.getDriverInfo(id)) {
				try {
					VehicleWebClient webClient = new VehicleWebClient();
					DriverViewResult result = webClient.DriverView(id);
					if (null != result && result.isSuccess()) {
						Driver driver = result.getInfoBean();
						if (null != driver)
							this.unknownDriversMap.put(driver.getId(), driver);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getFellowName(Context context, String id) {
		if (SelfMgr.getInstance().isDriver()) {
			Vendor vendor = SelfMgr.getInstance().getVendorInfo(id);
			if (null != vendor) {
				return vendor.getName();
			}
		} else {
			Driver driver = SelfMgr.getInstance().getDriverInfo(id);
			if (null != driver) {
				return driver.getAlias();
			}
		}

		return context.getString(R.string.zh_unknown);
	}

	public synchronized boolean isLogin() {
		return (this.mIsDriver && null != this.mSelfDriver) || (!this.mIsDriver && null != this.mSelfVendor);
	}

	public synchronized void clearAll() {
		this.clearFellows();
		this.clearNearby();
		this.clearUnknown();
		this.clearSelf();
	}

	public void doLogout(Context context) {
		try {
			this.clearAll();
			JPushInterface.setAlias(context, "a", new TagAliasCallback() {

				@Override
				public void gotResult(int arg0, String arg1, Set<String> arg2) {
					// TODO Auto-generated method stub

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public WebCallBaseResult doLogin(String userName, String password, final Context context) {
		WebCallBaseResult loginResult = null;

		if (this.mIsDriver) {

			try {
				VehicleWebClient webClient = new VehicleWebClient();
				loginResult = webClient.DriverLogin(userName, password);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (null == loginResult || !loginResult.isSuccess()) {
				return loginResult;
			}

			SelfDriver self = (SelfDriver) loginResult.getInfoBean();
			SelfMgr.getInstance().setSelfDriver(self);

			try {
				VehicleWebClient webClient = new VehicleWebClient();
				CarListViewResult carResult = webClient.CarListView(this.getId());
				if (null != carResult && carResult.isSuccess())
					this.mSelfDriver.setCars((carResult).getInfoBean());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				VehicleWebClient webClient = new VehicleWebClient();
				loginResult = webClient.VendorLogin(userName, password);
			} catch (Exception e) {

			}
			if (null == loginResult || !loginResult.isSuccess()) {
				return loginResult;
			}

			SelfVendor self = (SelfVendor) loginResult.getInfoBean();
			SelfMgr.getInstance().setSelfVendor(self);

			try {
				VehicleWebClient webClient = new VehicleWebClient();
				VendorSpecViewResult detailResult = webClient.ViewVendorAllDetail(this.getId());

				if (null != detailResult && detailResult.isSuccess() && null != detailResult.getInfoBean()) {
					SelfMgr.getInstance().setSelfVendorDetail(detailResult.getInfoBean());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		TopMsgerMgr.getInstance().init(context, getId());

		JPushAliasMaker aliasMaker = new JPushAliasMaker(context.getApplicationContext(), getId());
		aliasMaker.makeAlias();

		IMessageCourier courier = new WakeupMessageCourier(context);
		courier.dispatch(null);

		this.refreshFellows();

		return loginResult;
	}

	private SimpleLocation myLocation = null;

	public synchronized void updateLocation(double lat, double lnt) {
		if (null == myLocation) {
			myLocation = new SimpleLocation();
		}
		myLocation.setLatitude(lat);
		myLocation.setLongitude(lnt);
	}

	public synchronized SimpleLocation getLocation() {
		return this.myLocation;
	}
}
