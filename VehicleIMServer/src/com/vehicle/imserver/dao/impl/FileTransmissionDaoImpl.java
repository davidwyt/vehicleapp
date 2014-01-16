package com.vehicle.imserver.dao.impl;

import org.hibernate.Session;

import com.vehicle.imserver.dao.bean.FileTransmission;
import com.vehicle.imserver.dao.interfaces.FileTransmissionDao;
import com.vehicle.imserver.utils.HibernateUtil;

public class FileTransmissionDaoImpl extends BaseDaoImpl<FileTransmission> implements FileTransmissionDao {
	
	public void AddFileTranmission(FileTransmission fileTransmission)
	{
		Session session = HibernateUtil.getInstance().OpenSession();
		
		session.beginTransaction();
		
		session.save(fileTransmission);
		
		session.getTransaction().commit();
		
		session.close();
	}
	
	public FileTransmission GetFileTranmission(String token)
	{
		Session session = HibernateUtil.getInstance().OpenSession();
		session.beginTransaction();
		
		FileTransmission fileTran = (FileTransmission)session.get(FileTransmission.class, token);
		
		session.getTransaction().commit();
		session.close();
		
		return fileTran;
	}
	
	public void UpdateFileTranmission(FileTransmission fileTransmission)
	{
		Session session = HibernateUtil.getInstance().OpenSession();
		
		session.beginTransaction();
		
		session.update(fileTransmission);
		
		session.getTransaction().commit();
		
		session.close();
	}
}
