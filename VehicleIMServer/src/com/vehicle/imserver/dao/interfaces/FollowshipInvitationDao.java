package com.vehicle.imserver.dao.interfaces;

import com.vehicle.imserver.dao.bean.FollowshipInvitation;

public interface FollowshipInvitationDao {

	public void AddFollowshipInvitation(FollowshipInvitation invitation);

	public void UpdateFollowshipInvitation(FollowshipInvitation invitation);

	public FollowshipInvitation GetFollowshipInvitation(String id);
}
