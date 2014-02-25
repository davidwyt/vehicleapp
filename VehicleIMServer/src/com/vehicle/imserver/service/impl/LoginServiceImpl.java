package com.vehicle.imserver.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.vehicle.imserver.dao.bean.FollowshipInvitation;
import com.vehicle.imserver.dao.interfaces.FollowshipInvitationDao;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.interfaces.LoginService;
import com.vehicle.service.bean.WakeupRequest;

public class LoginServiceImpl implements LoginService {

	private FollowshipInvitationDao followshipInvDao;

	public FollowshipInvitationDao getFollowshipInvDao() {
		return this.followshipInvDao;
	}

	public void setFollowshipInvDao(FollowshipInvitationDao followshipInvDao) {
		this.followshipInvDao = followshipInvDao;
	}

	@Transactional(readOnly = false)
	@Override
	public List<FollowshipInvitation> ShakeNewInvitations(
			WakeupRequest request) throws PersistenceException {
		// TODO Auto-generated method stub

		String id = request.getId();

		List<FollowshipInvitation> invitations = null;
		try {
			invitations = followshipInvDao.GetNewFollowshipInvitation(id);
			followshipInvDao.UpdateNewFollowshipInvitation(id);
			System.out.println("new invitations:" + invitations.size());
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage(), e);
		}

		return invitations;
	}
}