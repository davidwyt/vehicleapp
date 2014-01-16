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
	
	public static FileTransmission GetFileTranmission(String token)
	{
		Session session = HibernateUtil.getInstance().OpenSession();
		session.beginTransaction();
		
		FileTransmission fileTran = (FileTransmission)session.get(FileTransmission.class, token);
		
		session.getTransaction().commit();
		session.close();
		
		return fileTran;
	}
	
	public static void UpdateFileTranmission(FileTransmission fileTransmission)
	{
		Session session = HibernateUtil.getInstance().OpenSession();
		
		session.beginTransaction();
		
		session.update(fileTransmission);
		
		session.getTransaction().commit();
		
		session.close();
	}
}
