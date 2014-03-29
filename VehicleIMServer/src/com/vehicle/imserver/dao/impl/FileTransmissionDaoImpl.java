package com.vehicle.imserver.dao.impl;

import java.io.File;
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

	@Override
	public void removeOldFiles(Long during) {
		// TODO Auto-generated method stub
		Long currentTime=System.currentTimeMillis();
		Session session=this.getSession();
		Query query=session.createQuery(Contants.HQL_SELECT_OLDFILES);
		query.setLong("transtime", currentTime-during);
		List<FileTransmission> files=query.list();
		
		Query del=session.createQuery(Contants.HQL_DEL_OLDFILES);
		del.setLong("transtime", currentTime-during);
		del.executeUpdate();
		for(int i=0;i<files.size();i++){
			FileTransmission f=files.get(i);
			String path=f.getPath();
			File file=new File(path);
			if(file.exists()){
				file.delete();
			}
		}
	}
}
