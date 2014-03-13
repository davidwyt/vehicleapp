package com.vehicle.imserver.service.interfaces;

import com.vehicle.imserver.dao.bean.VersionInfo;

public interface UpgradeService {
	VersionInfo getLatestVersion();
}
