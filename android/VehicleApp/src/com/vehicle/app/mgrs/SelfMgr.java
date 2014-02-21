package com.vehicle.app.mgrs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.FavoriteVendor;
import com.vehicle.app.bean.SelfDriver;
import com.vehicle.app.bean.SelfVendor;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.bean.VendorFellow;
import com.vehicle.app.web.bean.DriverListViewResult;
import com.vehicle.app.web.bean.NearbyVendorListViewResult;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.sdk.client.VehicleWebClient;
import com.vehicle.service.bean.RangeInfo;
import com.vehicle.service.bean.RangeResponse;

public class SelfMgr {

	private SelfMgr() {

		mFavVendorVector = new Vector<FavoriteVendor>();
		mVendorFellowVector = new Vector<VendorFellow>();

		mNearbyVendorMap = new Hashtable<String, Vendor>();
		mNearbyDriverMap = new Hashtable<String, Driver>();

		mVendorFellowDetailMap = new Hashtable<String, Driver>();
		mFavVendorDetailMap = new Hashtable<String, Vendor>();
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

	private Vector<FavoriteVendor> mFavVendorVector;
	private Vector<VendorFellow> mVendorFellowVector;

	private Map<String, Vendor> mNearbyVendorMap;
	private Map<String, Driver> mNearbyDriverMap;

	private Map<String, Driver> mVendorFellowDetailMap;
	private Map<String, Vendor> mFavVendorDetailMap;

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

	public Vector<FavoriteVendor> getFavVendorVector() {
		return this.mFavVendorVector;
	}

	public boolean isFavoriteVendor(String id) {
		if (null == this.mFavVendorDetailMap) {
			return false;
		}

		return this.mFavVendorDetailMap.containsKey(id);
	}

	public boolean isVendorFellow(String driverId) {
		if (null == this.mVendorFellowDetailMap) {
			return false;
		}

		return this.mVendorFellowDetailMap.containsKey(driverId);
	}

	public FavoriteVendor getFavVendor(String id) {

		if (null == this.mFavVendorVector) {
			return null;
		}

		synchronized (this.mFavVendorVector) {

			for (FavoriteVendor vendor : this.mFavVendorVector) {
				if (id.equals(vendor.getId()))
					return vendor;
			}

			return null;
		}
	}

	public VendorFellow getVendorFellow(String id) {
		if (null == this.mVendorFellowVector) {
			return null;
		}
		synchronized (this.mVendorFellowVector) {
			for (VendorFellow driver : this.mVendorFellowVector) {
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

	public void updateNearbyDrivers(Collection<Driver> drivers) {
		synchronized (this.mNearbyDriverMap) {
			this.mNearbyDriverMap.clear();
			this.addNearbyDrivers(drivers);
		}
	}

	public Vector<VendorFellow> getVendorFellowList() {
		return this.mVendorFellowVector;
	}

	public Map<String, Driver> getVendorFellowDetailMap() {
		return this.mVendorFellowDetailMap;
	}

	public Map<String, Vendor> getFavVendorDetailMap() {
		return this.mFavVendorDetailMap;
	}

	public Driver getVendorFellowDetail(String id) {
		if (null == this.mVendorFellowDetailMap) {
			return null;
		}

		return this.mVendorFellowDetailMap.get(id);
	}

	public Vendor getFavVendorDetail(String id) {
		if (null == this.mFavVendorDetailMap) {
			return null;
		}

		return this.mFavVendorDetailMap.get(id);
	}

	public synchronized void refreshNearby() {
		this.clearNearby();

		if (mIsDriver) {
			VehicleWebClient client = new VehicleWebClient();
			NearbyVendorListViewResult result = client.NearbyVendorListView(1, -1, -1, 1, 121.56, 31.24, 4, 1, -1, -1);

			List<Vendor> vendors = result.getInfoBean();
			for (Vendor vendor : vendors) {
				this.mNearbyVendorMap.put(vendor.getId(), vendor);
			}
		} else {
			this.mNearbyDriverMap.putAll(this.searchNearbyDrivers(121.56, 31.24, 30));
		}
	}

	public Map<String, Driver> searchNearbyDrivers(double centerX, double centerY, int range) {
		try {
			VehicleClient client = new VehicleClient(this.getId());
			RangeResponse resp = client.NearbyDrivers(121.56, 31.24, 30);
			List<RangeInfo> infos = resp.getList();

			List<String> driverIds = new ArrayList<String>();
			for (RangeInfo info : infos) {
				driverIds.add(info.getOwnerId());
			}

			if (null != infos && infos.size() > 0) {
				VehicleWebClient webClient = new VehicleWebClient();

				DriverListViewResult result = webClient.DriverListView(driverIds);
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
		VehicleWebClient webClient = new VehicleWebClient();

		if (mIsDriver) {
			this.mFavVendorVector.clear();
			this.mFavVendorVector.addAll(webClient.FavVendorListView(getId()).getInfoBean());

			List<String> vendorIds = new ArrayList<String>();
			for (FavoriteVendor vendor : this.mFavVendorVector) {
				vendorIds.add(vendor.getId());
			}

			this.mFavVendorDetailMap.clear();
			this.mFavVendorDetailMap.putAll(webClient.VendorListView(vendorIds).getInfoBean());

		} else {
			this.mVendorFellowVector.clear();
			this.mVendorFellowVector.addAll(webClient.VendorFellowListView(getId()).getInfoBean());

			List<String> driverIds = new ArrayList<String>();
			for (VendorFellow fellow : this.mVendorFellowVector) {
				driverIds.add(fellow.getId());
			}

			this.mVendorFellowDetailMap.clear();
			this.mVendorFellowDetailMap.putAll(webClient.DriverListView(driverIds).getInfoBean());
		}
	}

	public void clearFellows() {
		this.mFavVendorDetailMap.clear();
		this.mFavVendorVector.clear();

		this.mVendorFellowDetailMap.clear();
		this.mVendorFellowVector.clear();
	}

	public void clearNearby() {
		this.mNearbyDriverMap.clear();
		this.mNearbyVendorMap.clear();
	}
}
