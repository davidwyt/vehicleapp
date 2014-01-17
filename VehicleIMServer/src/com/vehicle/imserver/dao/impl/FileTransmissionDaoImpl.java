package com.vehicle.imserver.dao.impl;

import com.vehicle.imserver.dao.bean.FileTransmission;
import com.vehicle.imserver.dao.interfaces.FileTransmissionDao;

public class FileTransmissionDaoImpl extends BaseDaoImpl<FileTransmission> implements FileTransmissionDao {
	
	public void AddFileTranmission(FileTransmission fileTransmission)
	{
		this.save(fileTransmission);
	}
	
	public FileTransmission GetFileTranmission(String token)
	{
		return this.get(token);
	}
	
	public void UpdateFileTranmission(FileTransmission fileTransmission)
	{
		this.update(fileTransmission);
	}
}
