package com.vehicle.imserver.service.exception;

public class FollowshipInvitationProcessedAlreadyException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6241860716380703703L;
	
	public FollowshipInvitationProcessedAlreadyException(String msg, Throwable t)
	{
		super(msg, t);
	}
	
	public FollowshipInvitationProcessedAlreadyException(Throwable t)
	{
		super(t);
	}

	public FollowshipInvitationProcessedAlreadyException(String msg) {
		super(msg);
	}
}
