package com.vehicle.imserver.service.exception;

public class PushMessageFailedException extends Exception{

	public PushMessageFailedException(Throwable t) {
		super(t);
	}
	
	public PushMessageFailedException(String msg, Throwable t)
	{
		super(msg, t);
	}

	public PushMessageFailedException(String errmsg) {
		super(errmsg);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7157010416576804062L;
	
}
