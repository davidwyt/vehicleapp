package com.vehicle.imserver.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.vehicle.imserver.dao.bean.FileTransmission;
import com.vehicle.imserver.dao.bean.FollowshipInvitation;
import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.dao.interfaces.FileTransmissionDao;
import com.vehicle.imserver.dao.interfaces.FollowshipInvitationDao;
import com.vehicle.imserver.dao.interfaces.MessageDao;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.interfaces.LoginService;
import com.vehicle.service.bean.NewFileNotification;
import com.vehicle.service.bean.WakeupRequest;

public class LoginServiceImpl implements LoginService {

	private FollowshipInvitationDao followshipInvDao;
	private FileTransmissionDao fileTransmissionDao;
	private MessageDao messageDao;

	public MessageDao getMessageDao() {
		return this.messageDao;
	}

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public FileTransmissionDao getFileTransmissionDao() {
		return this.fileTransmissionDao;
	}

	public void setFileTransmissionDao(FileTransmissionDao fileTransmissionDao) {
		this.fileTransmissionDao = fileTransmissionDao;
	}

	public FollowshipInvitationDao getFollowshipInvDao() {
		return this.followshipInvDao;
	}

	public void setFollowshipInvDao(FollowshipInvitationDao followshipInvDao) {
		this.followshipInvDao = followshipInvDao;
	}

	@Override
	public List<FollowshipInvitation> GetNewInvitations(WakeupRequest request)
			throws PersistenceException {
		// TODO Auto-generated method stub

		String id = request.getId();

		List<FollowshipInvitation> invitations = null;
		try {
			invitations = followshipInvDao.GetNewFollowshipInvitation(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return invitations;
	}

	@Override
	public List<Message> GetNewMessages(WakeupRequest request) {
		// TODO Auto-generated method stub
		try {
			return this.messageDao.selectAllNewMessage(request.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<NewFileNotification> GetNewFiles(WakeupRequest request) {
		// TODO Auto-generated method stub
		try {
			List<FileTransmission> files = this.fileTransmissionDao
					.selectAllNewFiles(request.getId());

			List<NewFileNotification> notifications = new ArrayList<NewFileNotification>();
			if (null != files) {
				for (FileTransmission file : files) {
					File f = new File(file.getPath());

					NewFileNotification notification = new NewFileNotification(
							file.getSource(), file.getTarget(),
							file.getToken(), f.getName(),
							file.getTransmissionTime(), file.getMsgType());

					notifications.add(notification);
				}
			}

			return notifications;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void updateAllNewTextAndFileMessage(String memberId) {
		// TODO Auto-generated method stub
		try {
			this.messageDao.updateAllNewMessage(memberId);
			this.followshipInvDao.updateAllFollowshipInvitation(memberId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}