package com.vehicle.imserver.service.interfaces;

import com.vehicle.imserver.service.bean.MessageACKRequest;
import com.vehicle.imserver.service.bean.One2FolloweesMessageRequest;
import com.vehicle.imserver.service.bean.One2FollowersMessageRequest;
import com.vehicle.imserver.service.bean.One2OneMessageRequest;
import com.vehicle.imserver.service.exception.JPushException;
import com.vehicle.imserver.service.exception.MessageNotFoundException;
import com.vehicle.imserver.service.exception.PersistenceException;

public interface MessageService {

	public String SendMessage(
			One2OneMessageRequest msgReq) throws JPushException,
			PersistenceException;
	public void MessageReceived(MessageACKRequest msgACKReq)
			throws MessageNotFoundException, PersistenceException;
	public void SendMessage2Followees(One2FolloweesMessageRequest msgRequest) throws PersistenceException, JPushException;
	public void SendMessage2Followers(One2FollowersMessageRequest msgRequest) throws PersistenceException, JPushException;
	
}
