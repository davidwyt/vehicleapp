package com.vehicle.imserver.service.handler;

import java.io.IOException;
import java.io.InputStream;

import cn.jpush.api.ErrorCodeEnum;
import cn.jpush.api.MessageResult;

import com.vehicle.imserver.persistence.dao.FileTransmission;
import com.vehicle.imserver.persistence.dao.FileTransmissionStatus;
import com.vehicle.imserver.persistence.handler.FileTransmissionDaoHandler;
import com.vehicle.imserver.service.bean.FileFetchRequest;
import com.vehicle.imserver.service.bean.FileTransmissionRequest;
import com.vehicle.imserver.service.bean.INotification;
import com.vehicle.imserver.service.bean.NewFileNotification;
import com.vehicle.imserver.service.exception.FileTransmissionNotFoundException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.exception.PushNotificationFailedException;
import com.vehicle.imserver.utils.FileUtil;
import com.vehicle.imserver.utils.JPushUtil;

public class FileTransmissionServiceHandler {
	private FileTransmissionServiceHandler() {

	}

	public static void SendFile(FileTransmissionRequest request,
			InputStream input) throws IOException,
			PushNotificationFailedException, PersistenceException {
		String filePath = FileUtil.GenPathForFileTransmission("", request.getFileName());

		try {
			FileUtil.SaveFile(filePath, input);
		} catch (IOException e) {
			throw e;
		}

		FileTransmission fileTran = request.toRawDao(filePath);
		try{
		FileTransmissionDaoHandler.AddFileTranmission(fileTran);
		}catch(Exception e)
		{
			throw new PersistenceException(e.getMessage(), e);
		}
		
		INotification notification = new NewFileNotification(
				fileTran.getSource(), fileTran.getTarget(), fileTran.getToken());

		MessageResult msgResult = null;
		try {
			msgResult = JPushUtil.getInstance().SendNotification(notification);
		} catch (Exception e) {
			throw new PushNotificationFailedException(e);
		}

		if (null != msgResult) {
			if (ErrorCodeEnum.NOERROR.value() == msgResult.getErrcode()) {

			} else {
				throw new PushNotificationFailedException(msgResult.getErrmsg());
			}
		} else {
			throw new PushNotificationFailedException("no push result");
		}
	}
	
	public static String FetchFile(FileFetchRequest request) throws FileTransmissionNotFoundException, PersistenceException
	{
		String token = request.getToken();
		FileTransmission fileTran = FileTransmissionDaoHandler.GetFileTranmission(token);
		
		if(null == fileTran)
		{
			throw new FileTransmissionNotFoundException(String.format("file transmission with token %s not found", token));
		}
		
		String path = fileTran.getPath();
		
		fileTran.setStatus(FileTransmissionStatus.RECEIVED);
		try{
		FileTransmissionDaoHandler.UpdateFileTranmission(fileTran);
		}catch(Exception e)
		{
			throw new PersistenceException(e.getMessage(), e);
		}
		
		return path;
	}
}
