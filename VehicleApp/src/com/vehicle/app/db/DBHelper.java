package com.vehicle.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "VEHICLE.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS `MESSAGE` (`ID` CHAR(40) PRIMARY KEY NOT NULL, `SOURCE` VARCHAR(255) NOT NULL, `TARGET` VARCHAR(255) NOT NULL, `CONTENT` TEXT NOT NULL, `SENTTIME` INTEGER, `STATUS` CHAR(40));";
	private static final String DATABASE_INDEX_MSGSOURCE = "CREATE INDEX `MSG_SOURCE_IDX` ON `MESSAGE`(`SOURCE` ASC);";
	private static final String DATABASE_INDEX_MSGTARGET = "CREATE INDEX `MSG_TARGET_IDX` ON `MESSAGE`(`TARGET` ASC);";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.beginTransaction();
		db.execSQL(DATABASE_TABLE_MESSAGE);
		db.execSQL(DATABASE_INDEX_MSGSOURCE);
		db.execSQL(DATABASE_INDEX_MSGTARGET);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
