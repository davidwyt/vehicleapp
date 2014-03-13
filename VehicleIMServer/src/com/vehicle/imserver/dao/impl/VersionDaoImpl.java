package com.vehicle.imserver.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vehicle.imserver.dao.bean.VersionInfo;
import com.vehicle.imserver.dao.interfaces.VersionDao;
import com.vehicle.imserver.utils.Contants;

public class VersionDaoImpl extends BaseDaoImpl<VersionInfo> implements
		VersionDao {

	@SuppressWarnings("unchecked")
	@Override
	public VersionInfo getLatestVersion() {
		// TODO Auto-generated method stub
		Session session = this.getSession();

		Query query = session.createQuery(Contants.HQL_SELECT_LATESTVERSION);
		query.setMaxResults(1);
		List<VersionInfo> versions = query.list();

		return null != versions && versions.size() > 0 ? versions.get(0) : null;
	}
}
