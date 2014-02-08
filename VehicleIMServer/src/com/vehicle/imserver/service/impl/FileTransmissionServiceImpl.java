package com.vehicle.imserver.service.impl;

import java.io.IOException;
import java.io.InputStream;

import com.vehicle.imserver.dao.bean.FileTransmission;
import com.vehicle.imserver.dao.bean.FileTransmissionStatus;
import com.vehicle.imserver.dao.interfaces.FileTransmissionDao;
import com.vehicle.imserver.service.exception.FileTransmissionNotFoundException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.exception.PushNotificationFailedException;
import com.vehicle.imserver.service.interfaces.FileTransmissionService;
import com.vehicle.imserver.utils.FileUtil;
import com.vehicle.imserver.utils.JPushUtil;
import com.vehicle.imserver.utils.JsonUtil;
import com.vehicle.imserver.utils.RequestDaoUtil;
import com.vehicle.service.bean.FileFetchRequest;
import com.vehicle.service.bean.FileTransmissionRequest;
import com.vehicle.service.bean.INotification;
import com.vehicle.service.bean.NewFileNotification;

public class FileTransmissionServiceImpl implements FileTransmissionService {

	FileTransmissionDao fileTransmissionDao;

	public FileTransmissionDao getFileTransmissionDao() {
		return this.fileTransmissionDao;
	}

	public void setFileTransmissionDao(FileTransmissionDao fileTransmissionDao) {
		this.fileTransmissionDao = fileTransmissionDao;
	}

	public void SendFile(FileTransmissionRequest request, InputStream input)
			throws IOException, PushNotificationFailedException,
			PersistenceException {
		String filePath = FileUtil.GenPathForFileTransmission("",
				request.getFileName());

		System.out.println("upload file:" + filePath);

		try {
			FileUtil.SaveFile(filePath, input);
		} catch (IOException e) {
			throw e;
		}

		FileTransmission fileTran = RequestDaoUtil.toFileTransmission(request,
				filePath);
		try {
			fileTransmissionDao.AddFileTranmission(fileTran);
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}

		INotification notification = new NewFileNotification(
				fileTran.getSource(), fileTran.getTarget(), fileTran.getToken());

		JPushUtil.getInstance().SendNotification(fileTran.getTarget(),
				notification.getTitle(), JsonUtil.toJsonString(notification));
	}

	public String FetchFile(FileFetchRequest request)
			throws FileTransmissionNotFoundException, PersistenceException {
		String token = request.getToken();
		FileTransmission fileTran = fileTransmissionDao
				.GetFileTranmission(token);

		if (null == fileTran) {
			throw new FileTransmissionNotFoundException(String.format(
					"file transmission with token %s not found", token));
		}

		String path = fileTran.getPath();

		fileTran.setStatus(FileTransmissionStatus.RECEIVED);
		try {
			fileTransmissionDao.UpdateFileTranmission(fileTran);
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}

		return path;
	}
}
