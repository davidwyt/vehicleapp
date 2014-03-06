package com.vehicle.imserver.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vehicle.imserver.dao.bean.FileTransmission;
import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.dao.interfaces.FileTransmissionDao;
import com.vehicle.imserver.utils.Contants;

public class FileTransmissionDaoImpl extends BaseDaoImpl<FileTransmission>
		implements FileTransmissionDao {

	public void AddFileTranmission(FileTransmission fileTransmission) {
		this.save(fileTransmission);
	}

	public FileTransmission GetFileTranmission(String token) {
		return this.get(token);
	}

	public void UpdateFileTranmission(FileTransmission fileTransmission) {
		this.update(fileTransmission);
	}

	@Override
	public List<FileTransmission> selectAllNewFiles(String memberId) {
		// TODO Auto-generated method stub
		Session session = this.getSession();

		Query query = session.createQuery(Contants.HQL_SELECT_ALLNEWFILE);
		query.setString("target", memberId);
		query.setInteger("sent", FileTransmission.STATUS_SENT);

		List<FileTransmission> files = query.list();

		return files;
	}
}
