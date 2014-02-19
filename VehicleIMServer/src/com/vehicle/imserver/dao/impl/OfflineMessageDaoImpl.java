package com.vehicle.imserver.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vehicle.imserver.dao.bean.OfflineMessage;
import com.vehicle.imserver.dao.interfaces.OfflineMessageDao;
import com.vehicle.imserver.utils.Contants;

public class OfflineMessageDaoImpl extends BaseDaoImpl<OfflineMessage> implements
		OfflineMessageDao {

	@Override
	public List<OfflineMessage> getOffline(String target, Date time) {
		// TODO Auto-generated method stub
		Session session = this.getSession();
		
		Query query = session.createQuery(Contants.HQL_SELECT_OFFLINE);
		query.setString("target", target);
		query.setDate("senttime", time);
		List<OfflineMessage> msgs = query.list();
		
		return msgs;
	}

	@Override
	public void deleteOffline(String target) {
		// TODO Auto-generated method stub
		Session session = this.getSession();
		Query query = session.createQuery(Contants.HQL_DEL_OFFLINE);
		query.setString("target", target);
		query.executeUpdate();
	}

}
