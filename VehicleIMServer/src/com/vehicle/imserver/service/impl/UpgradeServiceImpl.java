package com.vehicle.imserver.service.impl;

import com.vehicle.imserver.dao.bean.VersionInfo;
import com.vehicle.imserver.dao.interfaces.VersionDao;
import com.vehicle.imserver.service.interfaces.UpgradeService;

public class UpgradeServiceImpl implements UpgradeService {

	private VersionDao versionDao;

	public void setVersionDao(VersionDao version) {
		this.versionDao = version;
	}

	public VersionDao getVersionDao() {
		return this.versionDao;
	}

	@Override
	public VersionInfo getLatestVersion() {
		// TODO Auto-generated method stub
		VersionInfo version = null;
		try {
			version = this.versionDao.getLatestVersion();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return version;
	}

}
