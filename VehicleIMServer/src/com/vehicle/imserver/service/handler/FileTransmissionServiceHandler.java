package com.vehicle.imserver.service.handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.vehicle.imserver.persistence.handler.FileTransmissionDaoHandler;
import com.vehicle.imserver.service.bean.FileTransmissionRequest;
import com.vehicle.imserver.utils.FileUtil;

public class FileTransmissionServiceHandler {
	private FileTransmissionServiceHandler() {

	}

	public static void SendFile(FileTransmissionRequest request,
			InputStream input) throws IOException {
		String filePath = FileUtil.GenPathForFileTransmission(Class.class
				.getResource(File.separator).getPath(), request.getFileName());
		
		try {
			FileUtil.SaveFile(filePath, input);
		} catch (IOException e) {
			throw e;
		}

		FileTransmissionDaoHandler.AddFileTranmission(request.toRawDao(filePath));
	}
}
