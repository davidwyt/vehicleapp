package com.vehicle.imserver.service.exception;

public class JPushException extends Exception {

	public JPushException(Throwable e) {
		// TODO Auto-generated constructor stub
		super(e);
	}
	
	public JPushException(String msg, Throwable t)
	{
		super(msg, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7368533167995807638L;
}
