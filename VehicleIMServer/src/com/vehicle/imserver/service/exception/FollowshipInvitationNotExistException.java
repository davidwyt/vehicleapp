package com.vehicle.imserver.service.exception;

public class FollowshipInvitationNotExistException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4887184848613781066L;
	
	public FollowshipInvitationNotExistException(String msg, Throwable t)
	{
		super(msg, t);
	}
	
	public FollowshipInvitationNotExistException(Throwable t)
	{
		super(t);
	}

	public FollowshipInvitationNotExistException(String msg) {
		super(msg);
	}
}
