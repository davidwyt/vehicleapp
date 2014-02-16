package com.vehicle.app.db;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.bean.InvitationVerdict;
import com.vehicle.app.msg.bean.InvitationVerdictMessage;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.bean.PictureMessage;
import com.vehicle.app.msg.bean.TextMessage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DBManager {

	private DBHelper mDBHelper;

	private final static String SQL_TEXTMESSAGE_INSERT = "INSERT INTO `TEXTMESSAGE`(`ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG`, `MSGTYPE`) VALUES(?, ?, ?, ?, ?, ?, ?);";
	private final static String SQL_TEXTMESSAGE_ALLSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG`, `MSGTYPE` FROM `TEXTMESSAGE` WHERE (`SOURCE` = ? AND `TARGET` = ?) OR (`SOURCE` = ? AND `TARGET` = ?) ORDER BY `SENTTIME` ASC;";
	private final static String SQL_TEXTMESSAGE_FLAGSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG`, `MSGTYPE` FROM `TEXTMESSAGE` WHERE `FLAG` = ? AND ((`SOURCE` = ? AND `TARGET` = ?) OR (`SOURCE` = ? AND `TARGET` = ?)) ORDER BY `SENTTIME` ASC;";
	private final static String SQL_TEXTMESSAGE_FLAGUPDATE = "UPDATE `TEXTMESSAGE` SET `FLAG`= ? WHERE `ID` = ?;";

	private final static String SQL_FILEMESSAGE_INSERT = "INSERT INTO `FILEMESSAGE` (`TOKEN`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG`) VALUES(?, ?, ?, ?, ?, ?);";
	private final static String SQL_FILEMESSAGE_ALLSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG` FROM `FILEMESSAGE` WHERE (`SOURCE` = ? AND `TARGET` = ?) OR (`SOURCE` = ? AND `TARGET` = ?) ORDER BY `SENTTIME` ASC;";
	private final static String SQL_FILEMESSAGE_FLAGSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG` FROM `FILEMESSAGE` WHERE `FLAG` = ? AND ((`SOURCE` = ? AND `TARGET` = ?) OR (`SOURCE` = ? AND `TARGET` = ?)) ORDER BY `SENTTIME` ASC;";
	private final static String SQL_FILEMESSAGE_FLAGUPDATE = "UPDATE `FILEMESSAGE` SET `FLAG`= ? WHERE `TOKEN` = ?;";

	private final static String SQL_INVVERDICTMESSAGE_INSERT = "INSERT INTO `INVITATIONVERDICT`(`INVITATIONID`, `SOURCE`, `TARGET`, `VERDICT`, `FLAG`) VALUES(?, ?, ?, ?, ?);";
	private final static String SQL_INVVERDICTMESSAGE_ALLSELECT = "SELECT `INVITATIONID`, `SOURCE`, `TARGET`, `VERDICT`, `FLAG` WHERE `TARGET` = ?;";
	private final static String SQL_INVVERDICTMESSAGE_FLAGSELECT = "SELECT `INVITATIONID`, `SOURCE`, `TARGET`, `VERDICT`, `FLAG` WHERE `TARGET` = ? AND `FLAG` = ?;";
	private final static String SQL_INVVERDICTMESSAGE_FLAGUPDATE = "UPDATE `INVITATIONVERDICT` SET `FLAG`= ? WHERE `INVITATIONID` = ?;";

	private final static String SQL_FOLLOWINV_INSERT = "INSERT INTO `FOLLOWSHIPINVITATION`(`ID`, `SOURCE`, `TARGET`, `SENTTIME`, `FLAG`) VALUES(?, ?, ?, ?, ?);";
	private final static String SQL_FOLLOWINV_ALLSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `SENTTIME`, `FLAG` WHERE `TARGET` = ?;";
	private final static String SQL_FOLLOWINV_FLAGSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `SENTTIME`, `FLAG` WHERE `TARGET` = ? AND `FLAG` = ?;";
	private final static String SQL_FOLLOWINV_FLAGUPDATE = "UPDATE `FOLLOWSHIPINVITATION` SET `FLAG`= ? WHERE `ID` = ?;";

	public DBManager(Context context) {
		mDBHelper = new DBHelper(context);
	}

	public void insertTextMessage(String id, String source, String target, String content, long sentTime,
			MessageFlag flag, int msgType) {

		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		try {
			SQLiteStatement insertStmt = db.compileStatement(SQL_TEXTMESSAGE_INSERT);
			insertStmt.clearBindings();
			insertStmt.bindString(1, id);
			insertStmt.bindString(2, source);
			insertStmt.bindString(3, target);
			insertStmt.bindString(4, content);
			insertStmt.bindLong(5, sentTime);
			insertStmt.bindString(6, flag.toString());
			insertStmt.bindLong(7, msgType);
			insertStmt.executeInsert();
		} finally {
			db.close();
		}
	}

	public void insertTextMessage(TextMessage msg) {
		insertTextMessage(msg.getId(), msg.getSource(), msg.getTarget(), msg.getContent(), msg.getSentTime(),
				msg.getFlag(), msg.getMsgType());
	}

	public void updateTextMessageFlag(String id, MessageFlag flag) {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();

		try {
			SQLiteStatement updateStmt = db.compileStatement(SQL_TEXTMESSAGE_FLAGUPDATE);
			updateStmt.clearBindings();
			updateStmt.bindString(1, flag.toString());
			updateStmt.bindString(2, id);
			updateStmt.executeUpdateDelete();
		} finally {
			db.close();
		}
	}

	public List<TextMessage> queryAllTextMessage(String source, String target) {

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<TextMessage> messages = new ArrayList<TextMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_TEXTMESSAGE_ALLSELECT, new String[] { source, target, target, source });

			while (cursor.moveToNext()) {

				TextMessage msg = new TextMessage();

				msg.setId(cursor.getString(cursor.getColumnIndex("ID")));

				msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

				msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

				msg.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));

				msg.setSentTime(cursor.getLong(cursor.getColumnIndex("SENTTIME")));

				msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

				msg.setMsgType(cursor.getInt(cursor.getColumnIndex("MSGTYPE")));

				messages.add(msg);
			}

		} finally {
			if (null != db)
				db.close();
		}

		return messages;
	}

	public List<TextMessage> queryUnreadTextMessage(String source, String target) {

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<TextMessage> messages = new ArrayList<TextMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_TEXTMESSAGE_FLAGSELECT, new String[] { MessageFlag.UNREAD.toString(),
					source, target, target, source });

			while (cursor.moveToNext()) {

				TextMessage msg = new TextMessage();

				msg.setId(cursor.getString(cursor.getColumnIndex("ID")));

				msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

				msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

				msg.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));

				msg.setSentTime(cursor.getLong(cursor.getColumnIndex("SENTTIME")));

				msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

				msg.setMsgType(cursor.getInt(cursor.getColumnIndex("MSGTYPE")));

				messages.add(msg);
			}
		} finally {
			if (null != db)
				db.close();
		}
		return messages;
	}

	public void insertFileMessage(String id, String source, String target, byte[] content, long sentTime,
			MessageFlag flag) {

		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		try {
			SQLiteStatement insertStmt = db.compileStatement(SQL_FILEMESSAGE_INSERT);
			insertStmt.clearBindings();
			insertStmt.bindString(1, id);
			insertStmt.bindString(2, source);
			insertStmt.bindString(3, target);

			insertStmt.bindBlob(4, content);
			insertStmt.bindLong(5, sentTime);
			insertStmt.bindString(6, flag.toString());
			insertStmt.executeInsert();
		} finally {
			if (null != db)
				db.close();
		}
	}

	public void insertFileMessage(PictureMessage msg) {
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		msg.getContent().compress(Bitmap.CompressFormat.PNG, 100, boas);
		insertFileMessage(msg.getToken(), msg.getSource(), msg.getTarget(), boas.toByteArray(), msg.getSentTime(),
				msg.getFlag());
	}

	public void updateFileMessageFlag(String token, MessageFlag flag) {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();

		try {
			SQLiteStatement updateStmt = db.compileStatement(SQL_FILEMESSAGE_FLAGUPDATE);
			updateStmt.clearBindings();
			updateStmt.bindString(1, flag.toString());
			updateStmt.bindString(2, token);
			updateStmt.executeUpdateDelete();
		} finally {
			db.close();
		}
	}

	public List<PictureMessage> queryAllFileMessage(String source, String target) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		List<PictureMessage> messages = new ArrayList<PictureMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_FILEMESSAGE_ALLSELECT, new String[] { source, target, target, source });

			while (cursor.moveToNext()) {

				PictureMessage msg = new PictureMessage();

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
		} finally {
			if (null != db)
				db.close();
		}
		return messages;
	}

	public List<PictureMessage> queryUnreadFileMessage(String source, String target) {

		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		List<PictureMessage> messages = new ArrayList<PictureMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_FILEMESSAGE_FLAGSELECT, new String[] { MessageFlag.UNREAD.toString(),
					source, target, target, source });

			while (cursor.moveToNext()) {

				PictureMessage msg = new PictureMessage();

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
		} finally {
			if (null != db)
				db.close();
		}
		return messages;
	}

	public void insertInvitationVerdictMessage(String invitationId, String source, String target,
			InvitationVerdict verdict, MessageFlag flag) {

		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		try {
			SQLiteStatement insertStmt = db.compileStatement(SQL_INVVERDICTMESSAGE_INSERT);
			insertStmt.clearBindings();
			insertStmt.bindString(1, invitationId);
			insertStmt.bindString(2, source);
			insertStmt.bindString(3, target);
			insertStmt.bindString(4, verdict.toString());
			insertStmt.bindString(5, flag.toString());
			insertStmt.executeInsert();
		} finally {
			db.close();
		}
	}

	public void insertInvitationVerdictMessage(InvitationVerdictMessage msg) {
		insertInvitationVerdictMessage(msg.getInvitationId(), msg.getSource(), msg.getTarget(), msg.getVerdict(),
				msg.getFlag());
	}

	public void updateInvitationVerdictMessageFlag(String invitationId, MessageFlag flag) {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();

		try {
			SQLiteStatement updateStmt = db.compileStatement(SQL_INVVERDICTMESSAGE_FLAGUPDATE);
			updateStmt.clearBindings();
			updateStmt.bindString(1, flag.toString());
			updateStmt.bindString(2, invitationId);
			updateStmt.executeUpdateDelete();
		} finally {
			db.close();
		}
	}

	public List<InvitationVerdictMessage> queryAllInvitationVerdictMessage(String target) {

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<InvitationVerdictMessage> messages = new ArrayList<InvitationVerdictMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_INVVERDICTMESSAGE_ALLSELECT, new String[] { target });

			while (cursor.moveToNext()) {

				InvitationVerdictMessage msg = new InvitationVerdictMessage();

				msg.setInvitationId(cursor.getString(cursor.getColumnIndex("INVITATIONID")));

				msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

				msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

				msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

				msg.setVerdict(InvitationVerdict.valueOf(cursor.getString(cursor.getColumnIndex("VERDICT"))));

				messages.add(msg);
			}
		} finally {
			if (null != db)
				db.close();
		}
		return messages;
	}

	public List<InvitationVerdictMessage> queryUnreadInvitationVerdictMessage(String target) {

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<InvitationVerdictMessage> messages = new ArrayList<InvitationVerdictMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_INVVERDICTMESSAGE_FLAGSELECT,
					new String[] { target, MessageFlag.UNREAD.toString() });

			while (cursor.moveToNext()) {

				InvitationVerdictMessage msg = new InvitationVerdictMessage();

				msg.setInvitationId(cursor.getString(cursor.getColumnIndex("INVITATIONID")));

				msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

				msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

				msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

				msg.setVerdict(InvitationVerdict.valueOf(cursor.getString(cursor.getColumnIndex("VERDICT"))));

				messages.add(msg);
			}
		} finally {
			if (null != db)
				db.close();
		}
		return messages;
	}

	public void insertFollowshipInvMessage(String invitationId, String source, String target, long sentTime,
			MessageFlag flag) {

		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		try {
			SQLiteStatement insertStmt = db.compileStatement(SQL_FOLLOWINV_INSERT);
			insertStmt.clearBindings();
			insertStmt.bindString(1, invitationId);
			insertStmt.bindString(2, source);
			insertStmt.bindString(3, target);
			insertStmt.bindLong(4, sentTime);
			insertStmt.bindString(5, flag.toString());
			insertStmt.executeInsert();
		} finally {
			db.close();
		}
	}

	public void insertFollowshipInvMessage(FollowshipInvitationMessage msg) {
		insertFollowshipInvMessage(msg.getId(), msg.getSource(), msg.getTarget(), msg.getSentTime(), msg.getFlag());
	}

	public void updateFollowshipInvMessageFlag(String invitationId, MessageFlag flag) {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();

		try {
			SQLiteStatement updateStmt = db.compileStatement(SQL_FOLLOWINV_FLAGUPDATE);
			updateStmt.clearBindings();
			updateStmt.bindString(1, flag.toString());
			updateStmt.bindString(2, invitationId);
			updateStmt.executeUpdateDelete();
		} finally {
			db.close();
		}
	}

	public List<FollowshipInvitationMessage> queryAllFollowshipInvMessage(String target) {

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<FollowshipInvitationMessage> messages = new ArrayList<FollowshipInvitationMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_FOLLOWINV_ALLSELECT, new String[] { target });

			while (cursor.moveToNext()) {

				FollowshipInvitationMessage msg = new FollowshipInvitationMessage();

				msg.setId(cursor.getString(cursor.getColumnIndex("ID")));

				msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

				msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

				msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

				msg.setSentTime(cursor.getLong(cursor.getColumnIndex("SENTTIME")));

				messages.add(msg);
			}
		} finally {
			if (null != db)
				db.close();
		}
		return messages;
	}

	public List<FollowshipInvitationMessage> queryUnreadFollowshipInvMessage(String target) {

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<FollowshipInvitationMessage> messages = new ArrayList<FollowshipInvitationMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_FOLLOWINV_FLAGSELECT, new String[] { target });

			while (cursor.moveToNext()) {

				FollowshipInvitationMessage msg = new FollowshipInvitationMessage();

				msg.setId(cursor.getString(cursor.getColumnIndex("ID")));

				msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

				msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

				msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

				msg.setSentTime(cursor.getLong(cursor.getColumnIndex("SENTTIME")));

				messages.add(msg);
			}
		} finally {
			if (null != db)
				db.close();
		}
		return messages;
	}
}
