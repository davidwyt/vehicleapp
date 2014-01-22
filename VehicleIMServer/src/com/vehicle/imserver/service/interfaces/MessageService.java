package com.vehicle.imserver.service.interfaces;

import java.util.List;

import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.exception.PushMessageFailedException;
import com.vehicle.service.bean.MessageOne2FolloweesRequest;
import com.vehicle.service.bean.MessageOne2FollowersRequest;
import com.vehicle.service.bean.MessageOne2OneRequest;
import com.vehicle.service.bean.OfflineMessageRequest;
import com.vehicle.service.bean.RespMessage;

public interface MessageService {

	public String SendMessage(MessageOne2OneRequest msgReq)
			throws PersistenceException, PushMessageFailedException;

	public List<RespMessage> sendOfflineMsgs(OfflineMessageRequest omReq);
	public void SendMessage2Followees(MessageOne2FolloweesRequest msgRequest)
			throws PersistenceException, PushMessageFailedException;

	public void SendMessage2Followers(MessageOne2FollowersRequest msgRequest)
			throws PersistenceException, PushMessageFailedException;
}
