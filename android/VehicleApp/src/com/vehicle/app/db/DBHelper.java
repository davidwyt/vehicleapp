package com.vehicle.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "VEHICLE.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_TABLE_TEXTMESSAGE = "CREATE TABLE IF NOT EXISTS `TEXTMESSAGE` (`ID` CHAR(40) NOT NULL, `SOURCE` VARCHAR(255) NOT NULL, `TARGET` VARCHAR(255) NOT NULL, `CONTENT` TEXT NOT NULL, `SENTTIME` INTEGER, `FLAG` CHAR(250), `MSGTYPE` INTEGER);";
	private static final String DATABASE_INDEX_TEXTMSGSOURCE = "CREATE INDEX `TEXTMSG_SOURCE_IDX` ON `TEXTMESSAGE`(`SOURCE` ASC);";
	private static final String DATABASE_INDEX_TEXTMSGTARGET = "CREATE INDEX `TEXTMSG_TARGET_IDX` ON `TEXTMESSAGE`(`TARGET` ASC);";
	private static final String DATABASE_INDEX_TEXTMSGFLAG = "CREATE INDEX `TEXTMSG_FLAG_IDX` ON `TEXTMESSAGE`(`FLAG` ASC);";

	private static final String DATABASE_TABLE_FILEMESSAGE = "CREATE TABLE IF NOT EXISTS `FILEMESSAGE` (`TOKEN` CHAR(40) NOT NULL, `SOURCE` VARCHAR(255) NOT NULL, `TARGET` VARCHAR(255) NOT NULL, `PATH` TEXT NOT NULL, `SENTTIME` INTEGER, `FLAG` CHAR(250), `MSGTYPE` INTEGER);";
	private static final String DATABASE_INDEX_FILEMSGSRC = "CREATE INDEX `FILEMSG_SOURCE_IDX` ON `FILEMESSAGE` (`SOURCE` ASC);";
	private static final String DATABASE_INDEX_FILEMSGTAR = "CREATE INDEX `FILEMSG_TARGET_IDX` ON `FILEMESSAGE` (`TARGET` ASC);";
	private static final String DATABASE_INDEX_FILEMSGFLAG = "CREATE INDEX `FILEMSG_FLAG_IDX` ON `FILEMESSAGE` (`FLAG` ASC);";

	private static final String DATABASE_TABLE_INVITATIONVERDICT = "CREATE TABLE IF NOT EXISTS `INVITATIONVERDICT` (`INVITATIONID` CHAR(40) NOT NULL, `SOURCE` VARCHAR(255) NOT NULL, `TARGET` VARCHAR(255) NOT NULL, `VERDICT` CHAR(250) NOT NULL, `FLAG` CHAR(250));";
	private static final String DATABASE_INDEX_INVVERDICTSRC = "CREATE INDEX `INVVERDICT_SOURCE_IDX` ON `INVITATIONVERDICT` (`SOURCE` ASC);";
	private static final String DATABASE_INDEX_INVVERDICTTAR = "CREATE INDEX `INVVERDICT_TARGET_IDX` ON `INVITATIONVERDICT` (`TARGET` ASC);";
	private static final String DATABASE_INDEX_INVVERDICTFLAG = "CREATE INDEX `INVVERDICT_FLAG_IDX` ON `INVITATIONVERDICT` (`FLAG` ASC);";

	private static final String DATABASE_TABLE_FOLLOWSHIPINVITATION = "CREATE TABLE IF NOT EXISTS `FOLLOWSHIPINVITATION` (`ID` CHAR(40) NOT NULL, `SOURCE` VARCHAR(255) NOT NULL, `TARGET` VARCHAR(255) NOT NULL, `SENTTIME` INTEGER, `FLAG` CHAR(250));";
	private static final String DATABASE_INDEX_FOLLOWINVSRC = "CREATE INDEX `FOLLOWINV_SOURCE_IDX` ON `FOLLOWSHIPINVITATION` (`SOURCE` ASC);";
	private static final String DATABASE_INDEX_FOLLOWINVTAR = "CREATE INDEX `FOLLOWINV_TARGET_IDX` ON `FOLLOWSHIPINVITATION` (`TARGET` ASC);";
	private static final String DATABASE_INDEX_FOLLOWINVFLAG = "CREATE INDEX `FOLLOWINV_FLAG_IDX` ON `FOLLOWSHIPINVITATION` (`FLAG` ASC);";

	private static final String DATABASE_TABLE_RECENTMESSAGE = "CREATE TABLE IF NOT EXISTS `RECENTMESSAGE` (`SELFID` VARCHAR(255) NOT NULL, `FELLOWID` VARCHAR(255) NOT NULL, `MESSAGETYPE` INTEGER NOT NULL, `MESSAGEID` CHAR(40) NOT NULL, `CONTENT` TEXT NULL, `SENTTIME` INTEGER NOT NULL, PRIMARY KEY(`SELFID`, `FELLOWID`));";
	private static final String DATABASE_INDEX_RECENTMSGTYPE = "CREATE INDEX `RECENTMSG_TYPE_IDX` ON `RECENTMESSAGE` (`MESSAGETYPE` ASC);";
	private static final String DATABASE_INDEX_RECENTMSGTIME = "CREATE INDEX `RECENTMSG_TIME_IDX` ON `RECENTMESSAGE` (`SENTTIME` ASC);";
	private static final String DATABASE_INDEX_RECENTMSGSELF = "CREATE INDEX `RECENTMSG_SELF_IDX` ON `RECENTMESSAGE` (`SELFID` ASC)";

	private static final String DATABASE_TABLE_TOPMESSAGE = "CREATE TABLE IF NOT EXISTS `TOPMESSAGE` (`HOST` CHAR(240) NOT NULL, `TOPMSG` TEXT, PRIMARY KEY (`HOST`))";
	
	private static final String DATABASE_TABLE_LASTLOGUSER = "CREATE TABLE IF NOT EXISTS `LASTONBOARDROLE` (`USERNAME` CHAR(240) NOT NULL, `PASSWORD` CHAR(240) NOT NULL, `ROLE` INTEGER NOT NULL DEFAULT 1, `AUTOLOG` INTEGER NOT NULL DEFAULT 1, PRIMARY KEY (`USERNAME`, `ROLE`))";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			db.beginTransaction();

			db.execSQL(DATABASE_TABLE_TEXTMESSAGE);
			db.execSQL(DATABASE_INDEX_TEXTMSGSOURCE);
			db.execSQL(DATABASE_INDEX_TEXTMSGTARGET);
			db.execSQL(DATABASE_INDEX_TEXTMSGFLAG);

			db.execSQL(DATABASE_TABLE_FILEMESSAGE);
			db.execSQL(DATABASE_INDEX_FILEMSGSRC);
			db.execSQL(DATABASE_INDEX_FILEMSGTAR);
			db.execSQL(DATABASE_INDEX_FILEMSGFLAG);

			db.execSQL(DATABASE_TABLE_INVITATIONVERDICT);
			db.execSQL(DATABASE_INDEX_INVVERDICTSRC);
			db.execSQL(DATABASE_INDEX_INVVERDICTTAR);
			db.execSQL(DATABASE_INDEX_INVVERDICTFLAG);

			db.execSQL(DATABASE_TABLE_FOLLOWSHIPINVITATION);
			db.execSQL(DATABASE_INDEX_FOLLOWINVSRC);
			db.execSQL(DATABASE_INDEX_FOLLOWINVTAR);
			db.execSQL(DATABASE_INDEX_FOLLOWINVFLAG);

			db.execSQL(DATABASE_TABLE_RECENTMESSAGE);
			db.execSQL(DATABASE_INDEX_RECENTMSGTYPE);
			db.execSQL(DATABASE_INDEX_RECENTMSGTIME);
			db.execSQL(DATABASE_INDEX_RECENTMSGSELF);

			db.execSQL(DATABASE_TABLE_TOPMESSAGE);
			
			db.execSQL(DATABASE_TABLE_LASTLOGUSER);
			
			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != db) {
				db.endTransaction();
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
