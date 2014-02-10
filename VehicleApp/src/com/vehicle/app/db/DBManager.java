package com.vehicle.app.db;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.vehicle.app.bean.MessageFlag;
import com.vehicle.app.bean.PictureMessageItem;
import com.vehicle.app.bean.TextMessageItem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DBManager {

	private DBHelper mDBHelper;

	private final static String SQL_TEXTMESSAGE_INSERT = "INSERT INTO `TEXTMESSAGE`(`ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG`) VALUES(?, ?, ?, ?, ?, ?);";
	private final static String SQL_TEXTMESSAGE_ALLSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME` `FLAG` FROM `TEXTMESSAGE` WHERE (`SOURCE` = ? AND `TARGET` = ?) OR (`SOURCE` = ? AND `TARGET` = ?) ORDER BY `SENTTIME` ASC;";
	private final static String SQL_TEXTMESSAGE_FLAGSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME` `FLAG` FROM `TEXTMESSAGE` WHERE `FLAG` = ? AND ((`SOURCE` = ? AND `TARGET` = ?) OR (`SOURCE` = ? AND `TARGET` = ?)) ORDER BY `SENTTIME` ASC;";

	private final static String SQL_FILEMESSAGE_INSERT = "INSERT INTO `FILEMESSAGE` (`TOKEN`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG`) VALUES(?, ?, ?, ?, ?, ?);";
	private final static String SQL_FILEMESSAGE_ALLSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG` FROM `FILEMESSAGE` WHERE (`SOURCE` = ? AND `TARGET` = ?) OR (`SOURCE` = ? AND `TARGET` = ?) ORDER BY `SENTTIME` ASC;";
	private final static String SQL_FILEMESSAGE_FLAGSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME` `FLAG` FROM `FILEMESSAGE` WHERE `FLAG` = ? AND ((`SOURCE` = ? AND `TARGET` = ?) OR (`SOURCE` = ? AND `TARGET` = ?)) ORDER BY `SENTTIME` ASC;";

	public DBManager(Context context) {
		mDBHelper = new DBHelper(context);
	}

	public void insertTextMessage(String id, String source, String target, String content, long sentTime,
			MessageFlag flag) {

		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		SQLiteStatement insertStmt = db.compileStatement(SQL_TEXTMESSAGE_INSERT);
		insertStmt.clearBindings();
		insertStmt.bindString(1, id);
		insertStmt.bindString(2, source);
		insertStmt.bindString(3, target);
		insertStmt.bindString(4, content);
		insertStmt.bindLong(5, sentTime);
		insertStmt.bindString(6, flag.toString());
		insertStmt.executeInsert();
		db.close();
	}

	public void insertTextMessage(TextMessageItem msg) {
		insertTextMessage(msg.getId(), msg.getSource(), msg.getTarget(), msg.getContent(), msg.getSentTime(),
				msg.getFlag());
	}

	public List<TextMessageItem> queryAllTextMessage(String source, String target) {

		SQLiteDatabase db = mDBHelper.getReadableDatabase();

		Cursor cursor = db.rawQuery(SQL_TEXTMESSAGE_ALLSELECT, new String[] { source, target, target, source });

		List<TextMessageItem> messages = new ArrayList<TextMessageItem>();

		while (cursor.moveToNext()) {

			TextMessageItem msg = new TextMessageItem();

			msg.setId(cursor.getString(cursor.getColumnIndex("ID")));

			msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

			msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

			msg.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));

			msg.setSentTime(cursor.getLong(cursor.getColumnIndex("SENTTIME")));

			msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

			messages.add(msg);
		}

		db.close();
		return messages;
	}

	public List<TextMessageItem> queryUnreadTextMessage(String source, String target) {

		SQLiteDatabase db = mDBHelper.getReadableDatabase();

		Cursor cursor = db.rawQuery(SQL_TEXTMESSAGE_FLAGSELECT, new String[] { MessageFlag.UNREAD.toString(), source,
				target, target, source });

		List<TextMessageItem> messages = new ArrayList<TextMessageItem>();

		while (cursor.moveToNext()) {

			TextMessageItem msg = new TextMessageItem();

			msg.setId(cursor.getString(cursor.getColumnIndex("ID")));

			msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

			msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

			msg.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));

			msg.setSentTime(cursor.getLong(cursor.getColumnIndex("SENTTIME")));

			msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

			messages.add(msg);
		}

		db.close();

		return messages;
	}

	public void insertFileMessage(String id, String source, String target, byte[] content, long sentTime,
			MessageFlag flag) {

		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		SQLiteStatement insertStmt = db.compileStatement(SQL_FILEMESSAGE_INSERT);
		insertStmt.clearBindings();
		insertStmt.bindString(1, id);
		insertStmt.bindString(2, source);
		insertStmt.bindString(3, target);

		insertStmt.bindBlob(4, content);
		insertStmt.bindLong(5, sentTime);
		insertStmt.bindString(6, flag.toString());
		insertStmt.executeInsert();

		db.close();
	}

	public void insertFileMessage(PictureMessageItem msg) {
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		msg.getContent().compress(Bitmap.CompressFormat.PNG, 100, boas);
		insertFileMessage(msg.getToken(), msg.getSource(), msg.getTarget(), boas.toByteArray(), msg.getSentTime(),
				msg.getFlag());
	}

	public List<PictureMessageItem> queryAllFileMessage(String source, String target) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(SQL_FILEMESSAGE_ALLSELECT, new String[] { source, target, target, source });

		List<PictureMessageItem> messages = new ArrayList<PictureMessageItem>();

		while (cursor.moveToNext()) {

			PictureMessageItem msg = new PictureMessageItem();

			msg.setToken(cursor.getString(cursor.getColumnIndex("TOKEN")));

			msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

			msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

			byte[] byteArray = cursor.getBlob(cursor.getColumnIndex("CONTENT"));

			Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

			msg.setContent(bitmap);

			msg.setSentTime(cursor.getLong(cursor.getColumnIndex("SENTTIME")));

			msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

			messages.add(msg);
		}

		db.close();

		return messages;
	}

	public List<PictureMessageItem> queryUnreadFileMessage(String source, String target) {

		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(SQL_FILEMESSAGE_FLAGSELECT, new String[] { MessageFlag.UNREAD.toString(), source,
				target, target, source });

		List<PictureMessageItem> messages = new ArrayList<PictureMessageItem>();

		while (cursor.moveToNext()) {

			PictureMessageItem msg = new PictureMessageItem();

			msg.setToken(cursor.getString(cursor.getColumnIndex("TOKEN")));

			msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

			msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

			byte[] byteArray = cursor.getBlob(cursor.getColumnIndex("CONTENT"));

			Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

			msg.setContent(bitmap);

			msg.setSentTime(cursor.getLong(cursor.getColumnIndex("SENTTIME")));

			msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

			messages.add(msg);
		}

		db.close();

		return messages;
	}
}
