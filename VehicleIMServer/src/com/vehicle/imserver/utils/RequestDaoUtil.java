package com.vehicle.imserver.utils;

import java.util.Date;
import java.util.UUID;

import com.vehicle.imserver.dao.bean.FileTransmission;
import com.vehicle.imserver.dao.bean.FileTransmissionStatus;
import com.vehicle.imserver.dao.bean.Followship;
import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.dao.bean.MessageStatus;
import com.vehicle.imserver.service.bean.FileTransmissionRequest;
import com.vehicle.imserver.service.bean.FollowshipRequest;
import com.vehicle.imserver.service.bean.MessageOne2OneRequest;

public class RequestDaoUtil {
	
	public static FileTransmission toFileTransmission(FileTransmissionRequest request, String path)
	{
		FileTransmission fileTran = new FileTransmission();
		fileTran.setSource(request.getSource());
		fileTran.setTarget(request.getTarget());
		fileTran.setStatus(FileTransmissionStatus.SENT);
		fileTran.setToken(UUID.randomUUID().toString());
		fileTran.setTransmissionTime(new Date());
		fileTran.setPath(path);
		
		return fileTran;
	}
	
	public static Message toRawMessage(MessageOne2OneRequest request) {
		Message msg = new Message();
		msg.setId(GUIDUtil.genNewGuid());
		msg.setSource(request.getSource());
		msg.setTarget(request.getTarget());
		msg.setContent(request.getContent());
		msg.setSentDate(new Date());
		msg.setStatus(MessageStatus.SENT);

		return msg;
	}
	
	public static Followship toRawFollowship(FollowshipRequest request)
	{
		Followship ship = new Followship();
		ship.setFollower(request.getFollower());
		ship.setFollowee(request.getFollowee());
		return ship;
	}
}