package com.vehicle.imserver.service.interfaces;

import java.io.IOException;
import java.io.InputStream;

import com.vehicle.imserver.service.bean.FileFetchRequest;
import com.vehicle.imserver.service.bean.FileTransmissionRequest;
import com.vehicle.imserver.service.exception.FileTransmissionNotFoundException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.exception.PushNotificationFailedException;

public interface FileTransmissionService {
	
	public void SendFile(FileTransmissionRequest request,
			InputStream input) throws IOException, PushNotificationFailedException, PersistenceException;
	
	public String FetchFile(FileFetchRequest request) throws FileTransmissionNotFoundException, PersistenceException;

}
