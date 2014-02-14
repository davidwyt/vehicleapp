package com.vehicle.imserver.utils;

public class ErrorCodes {
	
	public static final int UNKNOWN_ERROR_ERRCODE                  = 0x00000001;
	public static final int MESSAGE_NOT_FOUND_ERRCODE              = 0x00000002;
	public static final int MESSAGEID_NULL_ERRCODE                 = 0x00000003;
	public static final int MESSAGE_INVALID_ERRCODE                = 0x00000004;
	public static final int MESSAGE_PERSISTENCE_ERRCODE            = 0x00000005;
	public static final int MESSAGE_JPUSH_ERRCODE                  = 0x00000006;
	public static final int FOLLOWSHIP_ALREXIST_ERRCODE            = 0x00000007;
	public static final int FOLLOWSHIP_NOTEXIST_ERRCODE            = 0x00000008;
	public static final int FOLLOWSHIP_NULL_ERRCODE                = 0x00000009;
	public static final int FOLLOWSHIP_FOLLOWERNULL_ERRCODE        = 0x0000000A;
	public static final int FOLLOWSHIP_FOLLOWEENULL_ERRCODE        = 0x0000000B;
	public static final int FILETRAN_INVALID_ERRCODE               = 0x0000000C;
	public static final int FILETRAN_FILESAVE_ERRCODE              = 0x0000000D;
	public static final int NOTIFICATION_PUSHFAILED_ERRCODE        = 0x0000000E;
	public static final int FILEFETCH_TOKENNULL_ERRCODE            = 0x0000000F;
	public static final int FILEFETCH_TOKENINVALID_ERRCODE         = 0x00000010;
	public static final int FILEFETCH_NOTFOUND_ERRCODE             = 0x00000011;
	public static final int FOLLOWSHIPADDED_INVALID_ERRCODE        = 0x00000012;
	public static final int FOLLOWSHIPDROPPED_INVALIED_ERRCODE     = 0x00000013;
	public static final int FOLLOWINVITATION_INVALID_ERRCODE       = 0x00000014;
	public static final int FOLLOWINVRESULT_INVALID_ERRCODE        = 0x00000015;
	public static final int FOLLOWINVITATION_NOTEXIST_ERRCODE      = 0x00000016;
	public static final int FOLLOWINVITATION_PROCESSED_ERRCODE     = 0x00000017;
	public static final int LOGIN_INVALID_ERRORCODE                = 0x00000018;
	
	public static final String UNKNOWN_ERROR_ERRMSG                = "unknown error in the server: %s";
	public static final String MESSAGE_NOT_FOUND_ERRMSG            = "message %s not found in server";
	public static final String MESSAGEID_NULL_ERRMSG               = "the id of message is null or empty";
	public static final String MESSAGE_INVALID_ERRMSG              = "invalid message, please check if the source/target/content is null";
	public static final String MESSAGE_PERSISTENCE_ERRMSG          = "error(%s) when persistenting the message: %s";
	public static final String MESSAGE_JPUSH_ERRMSG                = "error(%s) when push the message: %s";
	public static final String FOLLOWSHIP_ALREXIST_ERRMSG          = "the followship %s => %s already exists";
	public static final String FOLLOWSHIP_NOTEXIST_ERRMSG          = "the followship %s => %s not exists";
	public static final String FOLLOWSHIP_NULL_ERRMSG              = "invalid followship request, please check if the follower/followee is null";
	public static final String FOLLOWSHIP_FOLLOWERNULL_ERRMSG      = "invalid followees request, please check if the follower is null";
	public static final String FOLLOWSHIP_FOLLOWEENULL_ERRMSG      = "invalid followers request, please check if the followee is null";
	public static final String FILETRAN_INVALID_ERRMSG             = "invalid file transmission request, please check if source/target/fileName is null";
	public static final String FILETRAN_FILESAVE_ERRMSG            = "error when save file: %s";
	public static final String NOTIFICATION_PUSHFAILED_ERRMSG      = "push %s notification failed with message: %s";
	public static final String FILEFETCH_TOKENNULL_ERRMSG          = "invalid file fetch request, please check if the token is null";
	public static final String FILEFETCH_TOKENINVALID_ERRMSG       = "file token : %s not found";
	public static final String FILEFETCH_NOTFOUND_ERRMSG           = "your request file: %s not found";
	public static final String FOLLOWSHIPADDED_INVALID_ERRMSG      = "invalid request, the memberid/shopid should not be null";
	public static final String FOLLOWSHIPDROPPED_INVALID_ERRMSG    = "invalid request, the memberid/shopid should not be null";
	public static final String FOLLOWINVITATION_INVALID_ERRMSG     = "invalid followship invitation, the memberId/shopId should not be null";
	public static final String FOLLOWINVRESULT_INVALID_ERRMSG      = "invalid request of invitation result, the invitationid should not be null";
	public static final String FOLLOWINVITATION_NOTEXIST_ERRMSG    = "the followship invitation:%s not exist";
	public static final String FOLLOWINVITATION_PROCESSED_ERRMSG   = "the followship invitation%s processed already";
	public static final String LOGIN_INVALID_ERRMSG                = "the id is null or empty when login";
}
