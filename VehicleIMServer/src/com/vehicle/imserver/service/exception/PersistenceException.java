package com.vehicle.imserver.service.exception;

public class PersistenceException extends Exception{

	public PersistenceException(Throwable e) {
		// TODO Auto-generated constructor stub
		super(e);
	}
	
	public PersistenceException(String msg, Throwable t)
	{
		super(msg, t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7602177532126232143L;

}
