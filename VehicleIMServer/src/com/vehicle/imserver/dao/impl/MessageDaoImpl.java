package com.vehicle.imserver.dao.impl;

import org.hibernate.Session;

import com.vehicle.imserver.dao.bean.FileTransmission;
import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.dao.bean.MessageStatus;
import com.vehicle.imserver.dao.interfaces.MessageDao;
import com.vehicle.imserver.service.exception.MessageNotFoundException;
import com.vehicle.imserver.utils.HibernateUtil;

public class MessageDaoImpl extends BaseDaoImpl<Message> implements MessageDao{
	
	public void InsertMessage(Message msg)
	{
		Session session = HibernateUtil.getInstance().OpenSession();
		
		session.beginTransaction();
		session.save(msg);
		
		session.getTransaction().commit();
		session.close();
	}
	
	public void UpdateMessageStatus(String msgId, MessageStatus status) throws MessageNotFoundException
	{
		Session session = HibernateUtil.getInstance().OpenSession();
		
		session.beginTransaction();
		Message message = (Message)session.get(Message.class, msgId);
		
		if(null == message)
		{
			session.getTransaction().commit();
			session.close();
			throw new MessageNotFoundException(String.format("message %s not found", msgId));
		}
		
		message.setStatus(status);
		session.update(message);
		session.getTransaction().commit();
		session.close();
	}
	
}
