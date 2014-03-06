package com.vehicle.imserver.dao.interfaces;

import java.util.List;

import com.vehicle.imserver.dao.bean.FollowshipInvitation;

public interface FollowshipInvitationDao {

	public void AddFollowshipInvitation(FollowshipInvitation invitation);

	public void UpdateFollowshipInvitation(FollowshipInvitation invitation);

	public FollowshipInvitation GetFollowshipInvitation(String id);

	public List<FollowshipInvitation> GetNewFollowshipInvitation(String memberId);
	
	public void updateFollowshipInvitation(String id);

	public void updateAllFollowshipInvitation(String memberId);
}
