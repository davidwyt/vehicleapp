package com.vehicle.imserver.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vehicle.imserver.dao.bean.FollowshipInvitation;
import com.vehicle.imserver.dao.interfaces.FollowshipInvitationDao;
import com.vehicle.imserver.utils.Contants;

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

	@SuppressWarnings("unchecked")
	@Override
	public List<FollowshipInvitation> GetNewFollowshipInvitation(String memberId) {
		// TODO Auto-generated method stub

		Session session = this.getSession();

		Query query = session.createQuery(Contants.HQL_SELECT_NEWFOLLOWINV);
		query.setString("target", memberId);
		query.setInteger("requested", FollowshipInvitation.STATUS_REQUESTED);
		query.setString("source", memberId);
		query.setInteger("accepted", FollowshipInvitation.STATUS_ACCEPTED);
		query.setInteger("rejected", FollowshipInvitation.STATUS_REJECTED);

		List<FollowshipInvitation> invitations = query.list();

		return invitations;
	}

	@Override
	public void UpdateNewFollowshipInvitation(String memberId) {
		Session session = this.getSession();

		Query newUpdate = session
				.createQuery(Contants.HQL_UPDATE_NEWREQFOLLOWINV);
		newUpdate.setString("target", memberId);
		newUpdate
				.setInteger("requested", FollowshipInvitation.STATUS_REQUESTED);
		newUpdate.setInteger("received", FollowshipInvitation.STATUS_RECEIVED);

		newUpdate.executeUpdate();

		Query doneUpdate = session
				.createQuery(Contants.HQL_UPDATE_NEWDONFOLLOWINV);
		doneUpdate.setString("source", memberId);
		doneUpdate.setInteger("accepted", FollowshipInvitation.STATUS_ACCEPTED);
		doneUpdate.setInteger("rejected", FollowshipInvitation.STATUS_REJECTED);
		doneUpdate.setInteger("done", FollowshipInvitation.STATUS_DONE);

		doneUpdate.executeUpdate();
		session.flush();
	}
}
