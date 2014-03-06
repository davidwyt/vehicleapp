package com.vehicle.imserver.service.interfaces;

import java.util.List;

import com.vehicle.imserver.dao.bean.FollowshipInvitation;
import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.service.bean.NewFileNotification;
import com.vehicle.service.bean.WakeupRequest;

public interface LoginService {

	List<FollowshipInvitation> GetNewInvitations(WakeupRequest request)
			throws PersistenceException;

	List<Message> GetNewMessages(WakeupRequest request);

	List<NewFileNotification> GetNewFiles(WakeupRequest request);

	void updateAllNewTextAndFileMessage(String memberId);
}
