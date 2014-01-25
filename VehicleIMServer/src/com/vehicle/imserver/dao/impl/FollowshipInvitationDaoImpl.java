package com.vehicle.imserver.dao.impl;

import com.vehicle.imserver.dao.bean.FollowshipInvitation;
import com.vehicle.imserver.dao.interfaces.FollowshipInvitationDao;

public class FollowshipInvitationDaoImpl extends
		BaseDaoImpl<FollowshipInvitation> implements FollowshipInvitationDao {

	@Override
	public void AddFollowshipInvitation(FollowshipInvitation invitation) {
		this.save(invitation);

	}

	@Override
	public void UpdateFollowshipInvitation(FollowshipInvitation invitation) {
		this.update(invitation);
	}

	@Override
	public FollowshipInvitation GetFollowshipInvitation(String id) {
		return this.get(id);
	}

}
