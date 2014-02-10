package com.vehicle.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "VEHICLE.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_TABLE_TEXTMESSAGE = "CREATE TABLE IF NOT EXISTS `TEXTMESSAGE` (`ID` CHAR(40) PRIMARY KEY NOT NULL, `SOURCE` VARCHAR(255) NOT NULL, `TARGET` VARCHAR(255) NOT NULL, `CONTENT` TEXT NOT NULL, `SENTTIME` INTEGER, `FLAG` CHAR(250));";
	private static final String DATABASE_INDEX_TEXTMSGSOURCE = "CREATE INDEX `TEXTMSG_SOURCE_IDX` ON `TEXTMESSAGE`(`SOURCE` ASC);";
	private static final String DATABASE_INDEX_TEXTMSGTARGET = "CREATE INDEX `TEXTMSG_TARGET_IDX` ON `TEXTMESSAGE`(`TARGET` ASC);";
	private static final String DATABASE_INDEX_TEXTMSGFLAG = "CREATE INDEX `TEXTMSG_FLAG_IDX` ON `TEXTMESSAGE`(`FLAG` ASC);";

	private static final String DATABASE_TABLE_FILEMESSAGE = "CREATE TABLE IF NOT EXIST `FILEMESSAGE` (`TOKEN` CHAR(40) PRIMARY KEY NOT NULL, `SOURCE` VARCHAR(255) NOT NULL, `TARGET` VARCHAR(255) NOT NULL, `CONTEXT` BLOB NOT NULL, `SENTTIME` INTEGER, `FLAG` CHAR(250));";
	private static final String DATABASE_INDEX_FILEMSGSRC = "CREATE INDEX `FILEMSG_SOURCE_IDX` ON `FILEMESSAGE` (`SOURCE` ASC);";
	private static final String DATABASE_INDEX_FILEMSGTAR = "CREATE INDEX `FILEMSG_TARGET_IDX` ON `FILEMESSAGE` (`TARGET` ASC);";
	private static final String DATABASE_INDEX_FILEMSGFLAG = "CREATE INDEX `FILEMSG_FLAG_IDX` ON `FILEMESSAGE` (`FLAG` ASC);";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.beginTransaction();

		db.execSQL(DATABASE_TABLE_TEXTMESSAGE);
		db.execSQL(DATABASE_INDEX_TEXTMSGSOURCE);
		db.execSQL(DATABASE_INDEX_TEXTMSGTARGET);
		db.execSQL(DATABASE_INDEX_TEXTMSGFLAG);

		db.execSQL(DATABASE_TABLE_FILEMESSAGE);
		db.execSQL(DATABASE_INDEX_FILEMSGSRC);
		db.execSQL(DATABASE_INDEX_FILEMSGTAR);
		db.execSQL(DATABASE_INDEX_FILEMSGFLAG);

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
