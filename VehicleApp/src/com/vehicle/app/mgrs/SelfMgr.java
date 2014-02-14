package com.vehicle.app.mgrs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.FavoriteVendor;
import com.vehicle.app.bean.SelfDriver;
import com.vehicle.app.bean.SelfVendor;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.bean.VendorFellow;
import com.vehicle.sdk.client.VehicleWebClient;

public class SelfMgr {

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
	private List<FavoriteVendor> mFavVendorList;
	private List<VendorFellow> mVendorFellowList;

	private List<Vendor> mNearbyVendorList;
	private List<Driver> mNearbyDriverList;

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

	public List<FavoriteVendor> getFavVendorList() {
		return this.mFavVendorList;
	}

	public synchronized void setFavVendorList(List<FavoriteVendor> favList) {
		this.mFavVendorList = favList;
	}

	public synchronized boolean isFavoriteVendor(String id) {
		if (null == mFavVendorList) {
			return false;
		}

		for (FavoriteVendor vendor : mFavVendorList) {
			if (id.equals(vendor.getId())) {
				return true;
			}
		}

		return false;
	}

	public void addFavoriteVendor(FavoriteVendor favVendor) {
		if (isFavoriteVendor(favVendor.getId())) {
			throw new IllegalArgumentException(String.format("vendor %s has benn in the favorite list",
					favVendor.getName()));
		}

		this.mFavVendorList.add(favVendor);
	}

	public void setNearbyVendorList(List<Vendor> vendorList) {
		this.mNearbyVendorList = vendorList;
	}

	public List<Vendor> getNearbyVendorList() {
		return this.mNearbyVendorList;
	}

	public void setNearbyDriverList(List<Driver> driverList) {
		this.mNearbyDriverList = driverList;
	}

	public List<Driver> getNearbyDriverList() {
		return this.mNearbyDriverList;
	}

	public List<VendorFellow> getVendorFellowList() {
		return this.mVendorFellowList;
	}

	public void setVendorFellowList(List<VendorFellow> vendorFellowList) {
		this.mVendorFellowList = vendorFellowList;
	}

	public Map<String, Driver> getVendorFellowDetailMap() {
		return this.mVendorFellowDetailMap;
	}

	public void setVendorFellowDetailList(Map<String, Driver> drivers) {
		this.mVendorFellowDetailMap = drivers;
	}

	public Map<String, Vendor> getFavVendorDetailMap() {
		return this.mFavVendorDetailMap;
	}

	public void setFavVendorDetailMap(Map<String, Vendor> vendors) {
		this.mFavVendorDetailMap = vendors;
	}

	public Vendor getNearbyVendor(String id) {
		if (null == this.mNearbyVendorList)
			return null;

		for (Vendor vendor : this.mNearbyVendorList) {
			if (id.equals(vendor.getId())) {
				return vendor;
			}
		}

		return null;
	}

	public Driver getNearbyDriver(String id) {
		if (null == this.mNearbyDriverList) {
			return null;
		}

		for (Driver driver : this.mNearbyDriverList) {
			if (id.equals(driver.getId())) {
				return driver;
			}
		}

		return null;
	}

	public void refreshFellows() {
		VehicleWebClient webClient = new VehicleWebClient();

		if (mIsDriver) {
			this.mFavVendorList = webClient.FavVendorListView(getId()).getInfoBean();

			List<String> vendorIds = new ArrayList<String>();
			for (FavoriteVendor vendor : this.mFavVendorList) {
				vendorIds.add(vendor.getId());
			}

			this.mFavVendorDetailMap = webClient.VendorListView(vendorIds).getInfoBean();

		} else {
			this.mVendorFellowList = webClient.VendorFellowListView(getId()).getInfoBean();

			List<String> driverIds = new ArrayList<String>();
			for (VendorFellow fellow : this.mVendorFellowList) {
				driverIds.add(fellow.getId());
			}

			this.mVendorFellowDetailMap = webClient.DriverListView(driverIds).getInfoBean();
		}
	}
}
