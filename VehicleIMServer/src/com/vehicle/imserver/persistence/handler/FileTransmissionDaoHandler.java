package com.vehicle.imserver.persistence.handler;

import org.hibernate.Session;

import com.vehicle.imserver.persistence.dao.FileTransmission;
import com.vehicle.imserver.utils.HibernateUtil;

public class FileTransmissionDaoHandler {
	
	private FileTransmissionDaoHandler()
	{
		
	}
	
	public static void AddFileTranmission(FileTransmission fileTransmission)
	{
		Session session = HibernateUtil.getInstance().OpenSession();
		
		session.beginTransaction();
		
		session.save(fileTransmission);
		
		session.getTransaction().commit();
		
		session.close();
	}
}
