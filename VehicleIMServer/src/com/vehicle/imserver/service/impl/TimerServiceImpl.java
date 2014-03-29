package com.vehicle.imserver.service.impl;

import org.springframework.scheduling.annotation.Scheduled;

import com.vehicle.imserver.dao.interfaces.FileTransmissionDao;
import com.vehicle.imserver.dao.interfaces.MessageDao;
import com.vehicle.imserver.service.interfaces.TimerService;

public class TimerServiceImpl implements TimerService {
	
	private MessageDao messageDao;
	private FileTransmissionDao fileTransmissionDao;

	@Override
	@Scheduled(cron="0 0 0 * * ?") 
	public void clean() {
		// TODO Auto-generated method stub
		Long during=1000*60*60*24*7L;
		messageDao.removeOldMessage(during);
		fileTransmissionDao.removeOldFiles(during);
	}

	public MessageDao getMessageDao() {
		return messageDao;
	}

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public FileTransmissionDao getFileTransmissionDao() {
		return fileTransmissionDao;
	}

	public void setFileTransmissionDao(FileTransmissionDao fileTransmissionDao) {
		this.fileTransmissionDao = fileTransmissionDao;
	}

}
