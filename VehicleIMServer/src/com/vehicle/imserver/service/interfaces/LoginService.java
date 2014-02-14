package com.vehicle.imserver.service.interfaces;

import java.util.List;

import com.vehicle.imserver.dao.bean.FollowshipInvitation;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.service.bean.LoginRequest;

public interface LoginService {

	List<FollowshipInvitation> getNewInvitations(LoginRequest request) throws PersistenceException;
}
