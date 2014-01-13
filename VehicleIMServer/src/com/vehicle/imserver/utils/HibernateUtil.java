package com.vehicle.imserver.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
	
	private static SessionFactory sessionFactory = null;
	
	private HibernateUtil()
	{
		Configuration configuration = new Configuration().configure();
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}
	
	private static HibernateUtil instance = new HibernateUtil();
	
	public static HibernateUtil getInstance()
	{
		return instance;
	}
	
	public Session OpenSession()
	{
		return sessionFactory.openSession();
	}

}
