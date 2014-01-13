package com.vehicle.imserver.service.exception;

public class FollowshipAlreadyExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8339736662085968973L;

	public FollowshipAlreadyExistException(Throwable e)
	{
		super(e);
	}
	
	public FollowshipAlreadyExistException(String msg, Throwable e)
	{
		super(msg, e);
	}

	public FollowshipAlreadyExistException() {
		// TODO Auto-generated constructor stub
		super();
	}
}
