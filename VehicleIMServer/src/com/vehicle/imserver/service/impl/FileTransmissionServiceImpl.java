package com.vehicle.imserver.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.vehicle.imserver.dao.bean.FileTransmission;
import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.dao.bean.MessageType;
import com.vehicle.imserver.dao.bean.OfflineMessage;
import com.vehicle.imserver.dao.interfaces.FileTransmissionDao;
import com.vehicle.imserver.dao.interfaces.MessageDao;
import com.vehicle.imserver.dao.interfaces.OfflineMessageDao;
import com.vehicle.imserver.service.exception.FileTransmissionNotFoundException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.exception.PushNotificationFailedException;
import com.vehicle.imserver.service.interfaces.FileTransmissionService;
import com.vehicle.imserver.utils.Contants;
import com.vehicle.imserver.utils.FileUtil;
import com.vehicle.imserver.utils.GUIDUtil;
import com.vehicle.imserver.utils.ImageUtil;
import com.vehicle.imserver.utils.JPushUtil;
import com.vehicle.imserver.utils.JsonUtil;
import com.vehicle.imserver.utils.RequestDaoUtil;
import com.vehicle.service.bean.FileFetchRequest;
import com.vehicle.service.bean.FileMultiTransmissionRequest;
import com.vehicle.service.bean.FileTransmissionRequest;
import com.vehicle.service.bean.INotification;
import com.vehicle.service.bean.NewFileNotification;

public class FileTransmissionServiceImpl implements FileTransmissionService {

	FileTransmissionDao fileTransmissionDao;
	private MessageDao messageDao;
	private OfflineMessageDao offlineMessageDao;

	public FileTransmissionDao getFileTransmissionDao() {
		return this.fileTransmissionDao;
	}

	public void setFileTransmissionDao(FileTransmissionDao fileTransmissionDao) {
		this.fileTransmissionDao = fileTransmissionDao;
	}

	public FileTransmission SendFile(FileTransmissionRequest request,
			InputStream input) throws IOException,
			PushNotificationFailedException, PersistenceException {
		String token = UUID.randomUUID().toString();
		String filePath = FileUtil.GenPathForFileTransmission("",
				request.getFileName(), token);

		System.out.println("upload file:" + filePath);

		try {
			FileUtil.SaveFile(filePath, input);
		} catch (IOException e) {
			throw e;
		}

		FileTransmission fileTran = RequestDaoUtil.toFileTransmission(request,
				filePath, token);

		try {
			fileTransmissionDao.AddFileTranmission(fileTran);
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}

		INotification notification = new NewFileNotification(
				fileTran.getSource(), fileTran.getTarget(),
				fileTran.getToken(), request.getFileName(),
				fileTran.getTransmissionTime());
		Message msg = new Message();
		msg.setContent(token);
		msg.setId(GUIDUtil.genNewGuid());
		msg.setMessageType(MessageType.IMAGE.ordinal());
		msg.setSentTime(System.currentTimeMillis());
		msg.setTarget(notification.getTarget());
		msg.setSource(notification.getSource());

		try {
			JPushUtil.getInstance().SendNotification(fileTran.getTarget(),
					notification.getTitle(),
					JsonUtil.toJsonString(notification));
			messageDao.save(msg);
		} catch (PushNotificationFailedException e) {
			offlineMessageDao.save(new OfflineMessage(msg));
			// throw e;
		}

		return fileTran;
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

		fileTran.setStatus(FileTransmission.STATUS_RECEIVED);
		try {
			fileTransmissionDao.UpdateFileTranmission(fileTran);
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}

		return path;
	}

	public MessageDao getMessageDao() {
		return messageDao;
	}

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public OfflineMessageDao getOfflineMessageDao() {
		return offlineMessageDao;
	}

	public void setOfflineMessageDao(OfflineMessageDao offlineMessageDao) {
		this.offlineMessageDao = offlineMessageDao;
	}

	@Override
	public FileTransmission SendFile2Multi(
			FileMultiTransmissionRequest request, InputStream input)
			throws IOException, PushNotificationFailedException,
			PersistenceException {
		String fileName = UUID.randomUUID().toString();
		String filePath = FileUtil.GenPathForFileTransmission("",
				request.getFileName(), fileName);

		System.out.println("upload file:" + filePath);

		try {
			FileUtil.SaveFile(filePath, input);
		} catch (IOException e) {
			throw e;
		}

		String[] targets = request.getTargets().split(",");
		FileTransmission fileTran = null;
		for (int i = 0; i < targets.length; i++) {
			FileTransmissionRequest fReq = new FileTransmissionRequest();
			fReq.setFileName(request.getFileName());
			fReq.setSource(request.getSource());
			fReq.setTarget(targets[i]);
			String token = UUID.randomUUID().toString();
			fileTran = RequestDaoUtil.toFileTransmission(fReq, filePath, token);
			try {
				fileTransmissionDao.AddFileTranmission(fileTran);
			} catch (Exception e) {
				throw new PersistenceException(e.getMessage(), e);
			}

			INotification notification = new NewFileNotification(
					fileTran.getSource(), fileTran.getTarget(),
					fileTran.getToken(), request.getFileName(),
					fileTran.getTransmissionTime());
			Message msg = new Message();
			msg.setContent(token);
			msg.setId(GUIDUtil.genNewGuid());
			msg.setMessageType(MessageType.IMAGE.ordinal());
			msg.setSentTime(System.currentTimeMillis());
			msg.setTarget(notification.getTarget());
			msg.setSource(notification.getSource());

			try {
				JPushUtil.getInstance().SendNotification(fileTran.getTarget(),
						notification.getTitle(),
						JsonUtil.toJsonString(notification));
				messageDao.save(msg);
			} catch (PushNotificationFailedException e) {
				offlineMessageDao.save(new OfflineMessage(msg));
				throw e;
			}
		}
		return fileTran;
	}

	@Override
	public String SendCommentFile(InputStream input, String fileName)
			throws IOException {

		String token = UUID.randomUUID().toString();
		String filePath = FileUtil.GenPathForFileTransmission("", fileName,
				token);

		System.out.println("upload file:" + filePath);

		try {
			FileUtil.SaveFile(filePath, input);
		} catch (IOException e) {
			throw e;
		}

		File inputFile = new File(filePath);

		String retName = inputFile.getName();
		BufferedImage sourceImg = ImageIO.read(inputFile);
		int width = sourceImg.getWidth();

		processImgFile(filePath, width, Contants.IMG_TYPE_SMALL);
		processImgFile(filePath, width, Contants.IMG_TYPE_MIDDLE);
		processImgFile(filePath, width, Contants.IMG_TYPE_LARGE);

		return retName;
	}

	private void processImgFile(String filePath, int width, int type)
			throws IOException {

		File srcFile = new File(filePath);
		String fileName = srcFile.getName();
		String tarPath = Contants.getImgTargetPath(type, fileName);

		File tarFile = new File(tarPath);
		File parent = tarFile.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}

		InputStream input = new FileInputStream(srcFile);

		if (Contants.IMG_TYPE_SMALL == type) {
			if (width <= Contants.SMALLIMG_WIDTH) {
				FileUtil.SaveFile(tarPath, input);
			} else {
				ImageUtil.scale(srcFile, tarFile, Contants.SMALLIMG_WIDTH,
						Contants.SMALLIMG_HEIGHT, "PNG");
			}
		} else if (Contants.IMG_TYPE_MIDDLE == type) {
			if (width <= Contants.MIDDLEIMG_WIDTH) {
				FileUtil.SaveFile(tarPath, input);
			} else {
				ImageUtil.scale(srcFile, tarFile, Contants.MIDDLEIMG_WIDTH,
						Contants.MIDDLEIMG_HEIGHT, "PNG");
			}
		} else if (Contants.IMG_TYPE_LARGE == type) {
			if (width <= Contants.LARGEIMG_WIDTH) {
				FileUtil.SaveFile(tarPath, input);
			} else {
				File waterFile = new File(Contants.getWaterMarkPath());
				ImageUtil.scaleAndWaterPrint(srcFile, tarFile, waterFile,
						Contants.LARGEIMG_WIDTH, Contants.LARGEIMG_HEIGHT,
						"PNG");
			}
		}

	}
}
