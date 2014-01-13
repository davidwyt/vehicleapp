package com.vehicle.imserver.persistence.handler;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.vehicle.imserver.persistence.dao.Message;

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
	
	public void UpdateMessage(Message msg)
	{
		
	}
	
}
