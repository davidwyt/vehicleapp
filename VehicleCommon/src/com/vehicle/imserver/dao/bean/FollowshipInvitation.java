package com.vehicle.imserver.dao.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "FOLLOWSHIPINVITATION", indexes = {
		@Index(columnList = "SOURCE"), @Index(columnList = "TARGET"),
		@Index(columnList = "STATUS") })
public class FollowshipInvitation implements Serializable {

	public static final int STATUS_REQUESTED = 0;
	public static final int STATUS_RECEIVED = 1;
	public static final int STATUS_ACCEPTED = 2;
	public static final int STATUS_REJECTED = 3;
	public static final int STATUS_DONE = 4;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4342291399359308498L;

	private String ID;
	private String source;
	private String target;
	private int status;
	private long reqTime;

	@Id
	@Column(name = "ID")
	public String getID() {
		return this.ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	@Column(name = "SOURCE")
	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "TARGET")
	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Column(name = "STATUS")
	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name = "REQTIME")
	public long getReqTime() {
		return this.reqTime;
	}

	public void setReqTime(long reqTime) {
		this.reqTime = reqTime;
	}
}
