package com.vehicle.imserver.service.interfaces;

import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.exception.PushMessageFailedException;
import com.vehicle.service.bean.MessageOne2FolloweesRequest;
import com.vehicle.service.bean.MessageOne2FollowersRequest;
import com.vehicle.service.bean.MessageOne2OneRequest;
import com.vehicle.service.bean.OfflineMessageRequest;

public interface MessageService {

	public String SendMessage(MessageOne2OneRequest msgReq)
			throws PersistenceException, PushMessageFailedException;

	public void sendOfflineMsgs(OfflineMessageRequest omReq) throws PushMessageFailedException, PersistenceException;
	public void SendMessage2Followees(MessageOne2FolloweesRequest msgRequest)
			throws PersistenceException, PushMessageFailedException;

	public void SendMessage2Followers(MessageOne2FollowersRequest msgRequest)
			throws PersistenceException, PushMessageFailedException;
}
