package com.vehicle.imserver.persistence.handler;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.vehicle.imserver.persistence.dao.Message;
import com.vehicle.imserver.persistence.dao.MessageStatus;
import com.vehicle.imserver.service.exception.MessageNotFoundException;

public class MessageDaoHandler {
	private static Configuration configuration = null;
	private static SessionFactory sessionFactory = null;
	private static ServiceRegistry serviceRegistry = null;
	
	private static MessageDaoHandler instance = new MessageDaoHandler();
	
	private MessageDaoHandler()
	{
		configuration = new Configuration().configure();
		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}
	
	public static MessageDaoHandler getInstance()
	{
		return instance;
	}
	
	public void InsertMessage(Message msg)
	{
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		session.save(msg);
		
		session.getTransaction().commit();
		session.close();
	}
	
	public void UpdateMessageStatus(String msgId, MessageStatus status) throws MessageNotFoundException
	{
		Session session = sessionFactory.openSession();
		
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
