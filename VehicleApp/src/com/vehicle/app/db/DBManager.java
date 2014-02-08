package com.vehicle.app.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vehicle.app.bean.Message;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

	private DBHelper mDBHelper;
	private SQLiteDatabase mDB;

	private final static String SQL_MESSAGE_INSERT = "INSERT INTO `MESSAGE`(`ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`) VALUES(?, ?, ?, ?, ?)";
	private final static String SQL_MESSAGE_SELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME` FROM `MESSAGE` WHERE (`SOURCE` = ? AND `TARGET` = ?) OR (`SOURCE` = ? AND `TARGET` = ?) ORDER BY `SENTTIME` ASC";

	public DBManager(Context context) {
		mDBHelper = new DBHelper(context);
		mDB = mDBHelper.getWritableDatabase();
	}

	public void addMessage(String id, String source, String target, String content, Date sentTime) {
		mDB.execSQL(SQL_MESSAGE_INSERT, new Object[] { id, source, target, content, sentTime });
	}

	public void addMessage(Message msg) {
		mDB.execSQL(SQL_MESSAGE_INSERT, new Object[] { msg.getId(), msg.getSource(), msg.getTarget(), msg.getContent(),
				msg.getSentDate() });
	}

	public List<Message> queryMessage(String source, String target) {
		Cursor cursor = mDB.rawQuery(SQL_MESSAGE_SELECT, new String[] { source, target, target, source });

		List<Message> messages = new ArrayList<Message>();
		
		while (cursor.moveToNext()) {
			
			Message msg = new Message();

			msg.setId(cursor.getString(cursor.getColumnIndex("ID")));

			msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

			msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

			msg.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));

			msg.setSentDate(cursor.getLong(cursor.getColumnIndex("SENTTIME")));

			messages.add(msg);
		}

		return messages;
	}

	public void close() {
		mDB.close();
	}
}
