package com.vehicle.imserver.service.interfaces;

import java.io.IOException;
import java.io.InputStream;

import com.vehicle.imserver.dao.bean.FileTransmission;
import com.vehicle.imserver.service.exception.FileTransmissionNotFoundException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.exception.PushNotificationFailedException;
import com.vehicle.service.bean.FileFetchRequest;
import com.vehicle.service.bean.FileTransmissionRequest;

public interface FileTransmissionService {
	
	public FileTransmission SendFile(FileTransmissionRequest request,
			InputStream input) throws IOException, PushNotificationFailedException, PersistenceException;
	
	public String FetchFile(FileFetchRequest request) throws FileTransmissionNotFoundException, PersistenceException;

}
