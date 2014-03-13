package com.vehicle.imserver.dao.interfaces;

import com.vehicle.imserver.dao.bean.VersionInfo;

public interface VersionDao extends BaseDao<VersionInfo> {

	static final String HQL_QUERYLATESTVERSION = "";
	
	VersionInfo getLatestVersion();
}
