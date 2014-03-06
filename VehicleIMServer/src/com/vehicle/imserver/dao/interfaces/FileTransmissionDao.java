package com.vehicle.imserver.dao.interfaces;

import java.util.List;

import com.vehicle.imserver.dao.bean.FileTransmission;

public interface FileTransmissionDao extends BaseDao<FileTransmission>{
	
	public void AddFileTranmission(FileTransmission fileTransmission);
	public FileTransmission GetFileTranmission(String token);
	public void UpdateFileTranmission(FileTransmission fileTransmission);
	
	public List<FileTransmission> selectAllNewFiles(String memberid);
}
