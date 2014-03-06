package com.vehicle.imserver.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.dao.interfaces.MessageDao;
import com.vehicle.imserver.utils.Contants;

public class MessageDaoImpl extends BaseDaoImpl<Message> implements MessageDao {

	public void InsertMessage(Message msg) {
		this.save(msg);
	}

	@Override
	public List<Message> selectAllNewMessage(String memberId) {
		// TODO Auto-generated method stub
		Session session = this.getSession();

		Query query = session.createQuery(Contants.HQL_SELECT_ALLNEWMESSAGE);
		query.setString("target", memberId);
		query.setInteger("sent", Message.STATUS_SENT);

		List<Message> msgs = query.list();

		return msgs;
	}

	@Override
	public void updateAllNewMessage(String memberId) {
		Session session = this.getSession();

		Query query = session.createQuery(Contants.HQL_UPDATE_ALLNEWMESSAGE);
		query.setString("target", memberId);
		query.setInteger("sent", Message.STATUS_SENT);

		query.executeUpdate();
	}
}
