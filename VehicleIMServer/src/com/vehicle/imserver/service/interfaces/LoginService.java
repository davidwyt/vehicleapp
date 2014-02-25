package com.vehicle.imserver.service.interfaces;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.vehicle.imserver.dao.bean.FollowshipInvitation;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.service.bean.WakeupRequest;

public interface LoginService {

	@Transactional(readOnly = false)
	List<FollowshipInvitation> ShakeNewInvitations(WakeupRequest request)
			throws PersistenceException;
}
