package com.vehicle.imserver.service.interfaces;

import com.vehicle.imserver.service.bean.MessageACKRequest;
import com.vehicle.imserver.service.bean.MessageOne2FolloweesRequest;
import com.vehicle.imserver.service.bean.MessageOne2FollowersRequest;
import com.vehicle.imserver.service.bean.MessageOne2OneRequest;
import com.vehicle.imserver.service.exception.JPushException;
import com.vehicle.imserver.service.exception.MessageNotFoundException;
import com.vehicle.imserver.service.exception.PersistenceException;

public interface MessageService {

	public String SendMessage(
			MessageOne2OneRequest msgReq) throws JPushException,
			PersistenceException;
	public void MessageReceived(MessageACKRequest msgACKReq)
			throws MessageNotFoundException, PersistenceException;
	public void SendMessage2Followees(MessageOne2FolloweesRequest msgRequest) throws PersistenceException, JPushException;
	public void SendMessage2Followers(MessageOne2FollowersRequest msgRequest) throws PersistenceException, JPushException;
	
}
