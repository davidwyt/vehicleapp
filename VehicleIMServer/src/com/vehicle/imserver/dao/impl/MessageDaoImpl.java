package com.vehicle.imserver.persistence.handler;

import org.hibernate.Session;

import com.vehicle.imserver.persistence.dao.Message;
import com.vehicle.imserver.persistence.dao.MessageStatus;
import com.vehicle.imserver.service.exception.MessageNotFoundException;
import com.vehicle.imserver.utils.HibernateUtil;

public class MessageDaoHandler {
	
	private MessageDaoHandler()
	{
	}
	
	public static void InsertMessage(Message msg)
	{
		Session session = HibernateUtil.getInstance().OpenSession();
		
		session.beginTransaction();
		session.save(msg);
		
		session.getTransaction().commit();
		session.close();
	}
	
	public static void UpdateMessageStatus(String msgId, MessageStatus status) throws MessageNotFoundException
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
