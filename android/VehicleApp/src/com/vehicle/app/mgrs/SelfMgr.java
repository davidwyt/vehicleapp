package com.vehicle.app.mgrs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.location.Location;

import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.FavoriteVendor;
import com.vehicle.app.bean.SelfDriver;
import com.vehicle.app.bean.SelfVendor;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.bean.VendorDetail;
import com.vehicle.app.bean.VendorFellow;
import com.vehicle.app.utils.LocationUtil;
import com.vehicle.app.web.bean.CarListViewResult;
import com.vehicle.app.web.bean.CommentListViewResult;
import com.vehicle.app.web.bean.DriverListViewResult;
import com.vehicle.app.web.bean.FavVendorListViewResult;
import com.vehicle.app.web.bean.NearbyDriverListViewResult;
import com.vehicle.app.web.bean.NearbyVendorListViewResult;
import com.vehicle.app.web.bean.VendorFellowListViewResult;
import com.vehicle.app.web.bean.VendorListViewResult;
import com.vehicle.app.web.bean.VendorSpecViewResult;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.sdk.client.VehicleWebClient;
import com.vehicle.service.bean.RangeInfo;
import com.vehicle.service.bean.RangeResponse;

public class SelfMgr {

	private SelfMgr() {

		mFavVendorSimpleVector = new Vector<FavoriteVendor>();
		mVendorFellowSimpleVector = new Vector<VendorFellow>();

		mNearbyVendorMap = new Hashtable<String, Vendor>();
		mNearbyDriverMap = new Hashtable<String, Driver>();

		mVendorFellowMap = new Hashtable<String, Driver>();
		mFavVendorMap = new Hashtable<String, Vendor>();

		mNearbyVendorDetailMap = new Hashtable<String, VendorDetail>();
		mFavVendorDetailMap = new Hashtable<String, VendorDetail>();
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

	private SelfDriver mSelfDriver;
	private SelfVendor mSelfVendor;

	private Vector<FavoriteVendor> mFavVendorSimpleVector;
	private Vector<VendorFellow> mVendorFellowSimpleVector;

	private Map<String, Vendor> mNearbyVendorMap;
	private Map<String, Driver> mNearbyDriverMap;

	private Map<String, Driver> mVendorFellowMap;
	private Map<String, Vendor> mFavVendorMap;

	private Map<String, VendorDetail> mNearbyVendorDetailMap;
	private Map<String, VendorDetail> mFavVendorDetailMap;

	public Map<String, VendorDetail> getNearbyVendorDetailMap() {
		return this.mNearbyVendorDetailMap;
	}

	public boolean isNearbyVendorDetailExist(String id) {
		return this.mNearbyVendorDetailMap.containsKey(id);
	}

	public void updateNearbyVendorDetail(VendorDetail detail) {
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

	public String getId() {
		return mIsDriver ? mSelfDriver.getId() : mSelfVendor.getId();
	}

	public boolean IsSelf(String id) {
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
		this.mNearbyVendorMap.put(vendor.getId(), vendor);
	}

	public void addNearbyDriver(Driver driver) {
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

	public synchronized void refreshNearby(double longitude, double latitude) {
		this.clearNearby();
		try {
			if (mIsDriver) {
				VehicleWebClient client = new VehicleWebClient();

				NearbyVendorListViewResult result = client.NearbyVendorListView(1, -1, -1, 1, longitude, latitude, 6,
						1, -1, -1);

				if (null != result) {
					List<Vendor> vendors = result.getInfoBean();
					if (null != vendors) {
						for (Vendor vendor : vendors) {
							this.mNearbyVendorMap.put(vendor.getId(), vendor);
						}
					}
				}
			} else {
				/**
				 * VehicleWebClient client = new VehicleWebClient();
				 * NearbyDriverListViewResult result =
				 * client.NearbyDriverListView(1, longitude, latitude);
				 * this.mNearbyDriverMap.putAll(result.getResult());
				 */
				Map<String, Driver> result = this.searchNearbyDrivers(longitude, latitude, 30);
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
			RangeResponse resp = client.NearbyDrivers(121.56, 31.24, 30);
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
				if (null == result || null == result.getInfoBean())
					return null;

				Map<String, Driver> drivers = result.getInfoBean();
				for (RangeInfo info : infos) {
					Driver driver = drivers.get(info.getOwnerId());
					driver.setDistance(info.getDistance());
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

				FavVendorListViewResult result = webClient.FavVendorListView(getId());
				if (null == result)
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
				if (null == vendorResult)
					return;

				Map<String, Vendor> vendorMap = vendorResult.getInfoBean();
				if (null != vendorMap)
					this.mFavVendorMap.putAll(vendorMap);

				for (String id : vendorIds) {
					VendorSpecViewResult vendorSpecResult = webClient.VendorSpecView(id);
					if (null != vendorSpecResult && null != vendorSpecResult.getInfoBean())
						this.mFavVendorDetailMap.put(id, vendorSpecResult.getInfoBean());
				}

			} else {
				this.mVendorFellowSimpleVector.clear();
				this.mVendorFellowMap.clear();
				VendorFellowListViewResult result = webClient.VendorFellowListView(getId());

				if (null == result || null == result.getInfoBean())
					return;

				this.mVendorFellowSimpleVector.addAll(result.getInfoBean());

				List<String> driverIds = new ArrayList<String>();
				for (VendorFellow fellow : this.mVendorFellowSimpleVector) {
					driverIds.add(fellow.getId());
				}

				DriverListViewResult driverResult = webClient.DriverListView(driverIds);
				if (null != driverResult && null != driverResult.getInfoBean()) {
					Map<String, Driver> drivers = driverResult.getInfoBean();
					for (Driver driver : drivers.values()) {
						CarListViewResult carResult = webClient.CarListView(driver.getId());
						if (null != carResult && null != carResult.getInfoBean()) {
							driver.setCars(carResult.getInfoBean());
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
				if (null != result && null != result.getInfoBean())
					this.mSelfDriver.setComments(result.getInfoBean());
			} else {
				VendorSpecViewResult result = webClient.VendorSpecView(this.mSelfVendor.getId());
				if (null != result && null != result.getResult())
					this.mSelfVendor.setComments(result.getResult().getReviews());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearFellows() {
		this.mFavVendorMap.clear();
		this.mFavVendorSimpleVector.clear();
		this.mFavVendorDetailMap.clear();

		this.mVendorFellowMap.clear();
		this.mVendorFellowSimpleVector.clear();
	}

	public void clearNearby() {
		this.mNearbyDriverMap.clear();
		this.mNearbyVendorMap.clear();

		this.mNearbyVendorDetailMap.clear();
	}
}
