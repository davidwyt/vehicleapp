package com.vehicle.imserver.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vehicle.imserver.dao.bean.Followship;
import com.vehicle.imserver.dao.interfaces.FollowshipDao;
import com.vehicle.imserver.service.exception.FollowshipAlreadyExistException;
import com.vehicle.imserver.service.exception.FollowshipNotExistException;
import com.vehicle.imserver.utils.Contants;

public class FollowshipDaoImpl extends BaseDaoImpl<Followship> implements
		FollowshipDao {

	public void AddFollowship(Followship followship)
			throws FollowshipAlreadyExistException {

		Followship oldship = this.get(followship);

		if (null != oldship) {
			throw new FollowshipAlreadyExistException();
		} else {
			this.save(followship);
		}
	}

	public void DropFollowship(Followship followship)
			throws FollowshipNotExistException {
		Followship oldship = this.get(followship);

		if (null == oldship) {
			throw new FollowshipNotExistException();
		} else {
			this.delete(followship);
		}
	}

	public List<String> GetFollowees(String follower) {
		Session session = this.getSession();

		Query query = session.createQuery(Contants.HQL_SELECT_FOLLOWEES);
		query.setString("follower", follower);

		List<String> followees = query.list();
		return followees;
	}

	public List<String> GetFollowers(String followee) {
		Session session = this.getSession();

		Query query = session.createQuery(Contants.HQL_SELECT_FOLLOWERS);
		query.setString("followee", followee);

		List<String> followers = query.list();

		return followers;
	}
}
