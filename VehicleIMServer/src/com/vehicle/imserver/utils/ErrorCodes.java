package com.vehicle.imserver.utils;

public class ErrorCodes {
	
	public static final int UNKNOWN_ERROR_ERRCODE = 0x00000000;
	public static final int MESSAGE_NOT_FOUND_ERRCODE = 0x00000001;
	public static final int MESSAGEID_NULL_ERRCODE = 0x000000002;
	public static final int MESSAGE_INVALID_ERRCODE = 0x00000003;
	public static final int MESSAGE_PERSISTENCE_ERRCODE = 0x00000004;
	public static final int MESSAGE_JPUSH_ERRCODE = 0x00000005;
	public static final int FOLLOWSHIP_ALREXIST_ERRCODE = 0x00000006;
	public static final int FOLLOWSHIP_NOTEXIST_ERRCODE = 0x00000007;
	public static final int FOLLOWSHIP_NULL_ERRCODE = 0x00000008;
	
	public static final String UNKNOWN_ERROR_ERRMSG = "unknown error in the server: %s";
	public static final String MESSAGE_NOT_FOUND_ERRMSG = "message %s not found in server";
	public static final String MESSAGEID_NULL_ERRMSG = "the id of message is null or empty";
	public static final String MESSAGE_INVALID_ERRMSG = "invalid message, please check if the source/target/content is null";
	public static final String MESSAGE_PERSISTENCE_ERRMSG = "error(%s) when persistenting the message: %s";
	public static final String MESSAGE_JPUSH_ERRMSG = "error(%s) when push the message: %s";
	public static final String FOLLOWSHIP_ALREXIST_ERRMSG = "the followship %s => %s already exists";
	public static final String FOLLOWSHIP_NOTEXIST_ERRMSG = "the followship %s => %s not exists";
	public static final String FOLLOWSHIP_NULL_ERRMSG = "invalid followship request, please check if the follower/followee is null";
}
