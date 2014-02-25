package com.vehicle.app.db;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.bean.InvitationVerdict;
import com.vehicle.app.msg.bean.InvitationVerdictMessage;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.bean.ImageMessage;
import com.vehicle.app.msg.bean.RecentMessage;
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
	private final static String SQL_TEXTMESSAGE_ALLSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG`, `MSGTYPE` FROM `TEXTMESSAGE` WHERE (`SOURCE` = ? AND `TARGET` = ? AND `FLAG` = ?) OR (`SOURCE` = ? AND `TARGET` = ? AND (`FLAG` = ? OR `FLAG` = ?)) ORDER BY `SENTTIME` ASC;";
	private final static String SQL_TEXTMESSAGE_FLAGSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG`, `MSGTYPE` FROM `TEXTMESSAGE` WHERE `FLAG` = ? AND `SOURCE` = ? AND `TARGET` = ? ORDER BY `SENTTIME` ASC;";
	private final static String SQL_TEXTMESSAGE_FLAGUPDATE = "UPDATE `TEXTMESSAGE` SET `FLAG`= ? WHERE `SOURCE` = ? AND `TARGET` = ? AND `FLAG` = ?;";
	private final static String SQL_TEXTMESSAGE_DELETEALL = "DELETE FROM `TEXTMESSAGE` WHERE (`SOURCE` = ? AND `TARGET` = ? AND `FLAG` = ?) OR (`SOURCE` = ? AND `TARGET` = ? AND (`FLAG` = ? OR `FLAG` = ?));";

	private final static String SQL_FILEMESSAGE_INSERT = "INSERT INTO `FILEMESSAGE` (`TOKEN`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG`) VALUES(?, ?, ?, ?, ?, ?);";
	private final static String SQL_FILEMESSAGE_ALLSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG` FROM `FILEMESSAGE` WHERE (`SOURCE` = ? AND `TARGET` = ? AND `FLAG` = ?) OR (`SOURCE` = ? AND `TARGET` = ? AND (`FLAG` = ? OR `FLAG` = ?)) ORDER BY `SENTTIME` ASC;";
	private final static String SQL_FILEMESSAGE_FLAGSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG` FROM `FILEMESSAGE` WHERE `FLAG` = ? AND `SOURCE` = ? AND `TARGET` = ? ORDER BY `SENTTIME` ASC;";
	private final static String SQL_FILEMESSAGE_FLAGUPDATE = "UPDATE `FILEMESSAGE` SET `FLAG`= ? WHERE `SOURCE` = ? AND `TARGET` = ? AND `FLAG` = ?;";
	private final static String SQL_FILEMESSAGE_DELETEALL = "DELETE FROM `FILEMESSAGE` WHERE (`SOURCE` = ? AND `TARGET` = ? AND `FLAG` = ?) OR (`SOURCE` = ? AND `TARGET` = ? AND (`FLAG` = ? OR `FLAG` = ?));";

	private final static String SQL_INVVERDICTMESSAGE_INSERT = "INSERT INTO `INVITATIONVERDICT`(`INVITATIONID`, `SOURCE`, `TARGET`, `VERDICT`, `FLAG`) VALUES(?, ?, ?, ?, ?);";
	private final static String SQL_INVVERDICTMESSAGE_ALLSELECT = "SELECT `INVITATIONID`, `SOURCE`, `TARGET`, `VERDICT`, `FLAG` WHERE `TARGET` = ? AND (`FLAG` = ? OR `FLAG` = ?);";
	private final static String SQL_INVVERDICTMESSAGE_FLAGSELECT = "SELECT `INVITATIONID`, `SOURCE`, `TARGET`, `VERDICT`, `FLAG` WHERE `TARGET` = ? AND `FLAG` = ?;";
	private final static String SQL_INVVERDICTMESSAGE_FLAGUPDATE = "UPDATE `INVITATIONVERDICT` SET `FLAG`= ? WHERE `INVITATIONID` = ? AND `FLAG` = ?;";

	private final static String SQL_FOLLOWINV_INSERT = "INSERT INTO `FOLLOWSHIPINVITATION`(`ID`, `SOURCE`, `TARGET`, `SENTTIME`, `FLAG`) VALUES(?, ?, ?, ?, ?);";
	private final static String SQL_FOLLOWINV_ALLSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `SENTTIME`, `FLAG` WHERE `TARGET` = ? AND (`FLAG` = ? OR `FLAG` = ?);";
	private final static String SQL_FOLLOWINV_FLAGSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `SENTTIME`, `FLAG` WHERE `TARGET` = ? AND `FLAG` = ?;";
	private final static String SQL_FOLLOWINV_FLAGUPDATE = "UPDATE `FOLLOWSHIPINVITATION` SET `FLAG`= ? WHERE `ID` = ? AND `FLAG` = ?;";

	private final static String SQL_RECENTMSG_INSERT = "INSERT INTO `RECENTMESSAGE`(`SELFID`, `FELLOWID`, `MESSAGEID`, `MESSAGETYPE`, `CONTENT`, `SENTTIME`) VALUES(?, ?, ?, ?, ?, ?);";
	private final static String SQL_RECENTMSG_UPDATE = "UPDATE `RECENTMESSAGE` SET `MESSAGEID` = ?, `MESSAGETYPE` = ?, `CONTENT` = ?, `SENTTIME` = ? WHERE `SELFID` = ? AND `FELLOWID` = ?;";
	private final static String SQL_RECENTMSG_ONEDELETE = "DELETE FROM `RECENTMESSAGE` WHERE `SELFID` = ? AND `FELLOWID` = ?;";
	private final static String SQL_RECENTMSG_ALLDELETE = "DELETE FROM `RECENTMESSAGE` WHERE `SELFID` = ?;";
	private final static String SQL_RECENTMSG_ONESELECT = "SELECT `SELFID`, `FELLOWID`, `MESSAGEID`, `MESSAGETYPE`, `CONTENT`, `SENTTIME` FROM `RECENTMESSAGE` WHERE `SELFID` = ? AND `FELLOWID` = ?;";
	private final static String SQL_RECENTMSG_ALLSELECT = "SELECT `SELFID`, `FELLOWID`, `MESSAGEID`, `MESSAGETYPE`, `CONTENT`, `SENTTIME` FROM `RECENTMESSAGE` WHERE `SELFID` = ? ORDER BY `SENTTIME` DESC;";
	private final static String SQL_RECENTMSG_DELETEALL = "DELETE FROM `RECENTMESSAGE` WHERE `SELFID` = ? AND `FELLOWID` = ?;";

	public DBManager(Context context) {
		mDBHelper = new DBHelper(context);
	}

	public void deleteAllMessages(String selfId, String fellowId) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		try {
			db.beginTransaction();

			SQLiteStatement deleteTextMsgStmt = db.compileStatement(SQL_TEXTMESSAGE_DELETEALL);
			deleteTextMsgStmt.clearBindings();

			deleteTextMsgStmt.bindString(1, selfId);
			deleteTextMsgStmt.bindString(2, fellowId);
			deleteTextMsgStmt.bindString(3, MessageFlag.SELF.toString());
			deleteTextMsgStmt.bindString(4, fellowId);
			deleteTextMsgStmt.bindString(5, selfId);
			deleteTextMsgStmt.bindString(6, MessageFlag.UNREAD.toString());
			deleteTextMsgStmt.bindString(7, MessageFlag.READ.toString());

			deleteTextMsgStmt.executeUpdateDelete();

			SQLiteStatement deleteFileMsgStmt = db.compileStatement(SQL_FILEMESSAGE_DELETEALL);
			deleteFileMsgStmt.clearBindings();

			deleteFileMsgStmt.bindString(1, selfId);
			deleteFileMsgStmt.bindString(2, fellowId);
			deleteFileMsgStmt.bindString(3, MessageFlag.SELF.toString());
			deleteFileMsgStmt.bindString(4, fellowId);
			deleteFileMsgStmt.bindString(5, selfId);
			deleteFileMsgStmt.bindString(6, MessageFlag.UNREAD.toString());
			deleteFileMsgStmt.bindString(7, MessageFlag.READ.toString());

			deleteFileMsgStmt.executeUpdateDelete();

			SQLiteStatement deleteRecentMsgStmt = db.compileStatement(SQL_RECENTMSG_DELETEALL);
			deleteRecentMsgStmt.clearBindings();

			deleteRecentMsgStmt.bindString(1, selfId);
			deleteRecentMsgStmt.bindString(2, fellowId);

			deleteRecentMsgStmt.executeUpdateDelete();

			db.setTransactionSuccessful();
			db.endTransaction();
		} finally {
			if (null != db) {
				db.close();
			}
		}
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
				msg.getFlag(), msg.getMessageType());
	}

	public void updateUnreadTextMessageFlag(String selfId, String fellowId) {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();

		try {
			SQLiteStatement updateStmt = db.compileStatement(SQL_TEXTMESSAGE_FLAGUPDATE);
			updateStmt.clearBindings();
			updateStmt.bindString(1, MessageFlag.READ.toString());
			updateStmt.bindString(2, fellowId);
			updateStmt.bindString(3, selfId);
			updateStmt.bindString(4, MessageFlag.UNREAD.toString());
			updateStmt.executeUpdateDelete();
		} finally {
			db.close();
		}
	}

	public List<TextMessage> queryAllTextMessage(String selfId, String fellowId) {

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<TextMessage> messages = new ArrayList<TextMessage>();

		try {
			Cursor cursor = db.rawQuery(
					SQL_TEXTMESSAGE_ALLSELECT,
					new String[] { selfId, fellowId, MessageFlag.SELF.toString(), fellowId, selfId,
							MessageFlag.READ.toString(), MessageFlag.UNREAD.toString() });

			while (cursor.moveToNext()) {

				TextMessage msg = new TextMessage();

				msg.setId(cursor.getString(cursor.getColumnIndex("ID")));

				msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

				msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

				msg.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));

				msg.setSentTime(cursor.getLong(cursor.getColumnIndex("SENTTIME")));

				msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

				msg.setMessageType(cursor.getInt(cursor.getColumnIndex("MSGTYPE")));

				messages.add(msg);
			}

		} finally {
			if (null != db)
				db.close();
		}

		return messages;
	}

	public List<TextMessage> queryUnreadTextMessage(String selfId, String fellowId) {

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<TextMessage> messages = new ArrayList<TextMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_TEXTMESSAGE_FLAGSELECT, new String[] { MessageFlag.UNREAD.toString(),
					fellowId, selfId });

			while (cursor.moveToNext()) {

				TextMessage msg = new TextMessage();

				msg.setId(cursor.getString(cursor.getColumnIndex("ID")));

				msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

				msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

				msg.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));

				msg.setSentTime(cursor.getLong(cursor.getColumnIndex("SENTTIME")));

				msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

				msg.setMessageType(cursor.getInt(cursor.getColumnIndex("MSGTYPE")));

				messages.add(msg);
			}
		} finally {
			if (null != db)
				db.close();
		}
		return messages;
	}

	public long insertFileMessage(String id, String source, String target, byte[] content, long sentTime,
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
			return insertStmt.executeInsert();

		} finally {
			if (null != db)
				db.close();
		}
	}

	public void insertFileMessage(ImageMessage msg) {
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		msg.getContent().compress(Bitmap.CompressFormat.PNG, 100, boas);
		insertFileMessage(msg.getToken(), msg.getSource(), msg.getTarget(), boas.toByteArray(), msg.getSentTime(),
				msg.getFlag());
	}

	public void updateUnreadFileMessageFlag(String selfId, String fellowId) {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();

		try {
			SQLiteStatement updateStmt = db.compileStatement(SQL_FILEMESSAGE_FLAGUPDATE);
			updateStmt.clearBindings();
			updateStmt.bindString(1, MessageFlag.READ.toString());
			updateStmt.bindString(2, fellowId);
			updateStmt.bindString(3, selfId);
			updateStmt.bindString(4, MessageFlag.UNREAD.toString());
			updateStmt.executeUpdateDelete();
		} finally {
			db.close();
		}
	}

	public List<ImageMessage> queryAllFileMessage(String selfId, String fellowId) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		List<ImageMessage> messages = new ArrayList<ImageMessage>();

		try {
			Cursor cursor = db.rawQuery(
					SQL_FILEMESSAGE_ALLSELECT,
					new String[] { selfId, fellowId, MessageFlag.SELF.toString(), fellowId, selfId,
							MessageFlag.READ.toString(), MessageFlag.UNREAD.toString() });

			while (cursor.moveToNext()) {

				ImageMessage msg = new ImageMessage();

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

	public List<ImageMessage> queryUnreadFileMessage(String selfId, String fellowId) {

		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		List<ImageMessage> messages = new ArrayList<ImageMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_FILEMESSAGE_FLAGSELECT, new String[] { MessageFlag.UNREAD.toString(),
					fellowId, selfId });

			while (cursor.moveToNext()) {

				ImageMessage msg = new ImageMessage();

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

	public void updateUnreadInvitationVerdictMessageFlag(String invitationId) {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();

		try {
			SQLiteStatement updateStmt = db.compileStatement(SQL_INVVERDICTMESSAGE_FLAGUPDATE);
			updateStmt.clearBindings();
			updateStmt.bindString(1, MessageFlag.READ.toString());
			updateStmt.bindString(2, invitationId);
			updateStmt.bindString(3, MessageFlag.UNREAD.toString());
			updateStmt.executeUpdateDelete();
		} finally {
			db.close();
		}
	}

	public List<InvitationVerdictMessage> queryAllInvitationVerdictMessage(String selfId) {

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<InvitationVerdictMessage> messages = new ArrayList<InvitationVerdictMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_INVVERDICTMESSAGE_ALLSELECT,
					new String[] { selfId, MessageFlag.READ.toString(), MessageFlag.UNREAD.toString() });

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

	public List<InvitationVerdictMessage> queryUnreadInvitationVerdictMessage(String selfId) {

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<InvitationVerdictMessage> messages = new ArrayList<InvitationVerdictMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_INVVERDICTMESSAGE_FLAGSELECT,
					new String[] { selfId, MessageFlag.UNREAD.toString() });

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

	public void updateUnreadFollowshipInvMessageFlag(String invitationId) {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();

		try {
			SQLiteStatement updateStmt = db.compileStatement(SQL_FOLLOWINV_FLAGUPDATE);
			updateStmt.clearBindings();
			updateStmt.bindString(1, MessageFlag.READ.toString());
			updateStmt.bindString(2, invitationId);
			updateStmt.bindString(3, MessageFlag.UNREAD.toString());
			updateStmt.executeUpdateDelete();
		} finally {
			db.close();
		}
	}

	public List<FollowshipInvitationMessage> queryAllFollowshipInvMessage(String selfId) {

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<FollowshipInvitationMessage> messages = new ArrayList<FollowshipInvitationMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_FOLLOWINV_ALLSELECT, new String[] { selfId, MessageFlag.READ.toString(),
					MessageFlag.UNREAD.toString() });

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
			Cursor cursor = db.rawQuery(SQL_FOLLOWINV_FLAGSELECT,
					new String[] { target, MessageFlag.UNREAD.toString() });

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

	public void insertOrUpdateRecentMessage(RecentMessage recentMsg) {
		insertOrUpdateRecentMessage(recentMsg.getSelfId(), recentMsg.getFellowId(), recentMsg.getMessageId(),
				recentMsg.getMessageType(), recentMsg.getContent(), recentMsg.getSentTime());
	}

	public void insertOrUpdateRecentMessage(String selfId, String fellowId, String messageId, int messageType,
			String content, long sentTime) {

		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		try {
			db.beginTransaction();

			boolean update = false;
			Cursor cursor = db.rawQuery(SQL_RECENTMSG_ONESELECT, new String[] { selfId, fellowId });
			if (null == cursor || 0 == cursor.getCount()) {

			} else if (1 == cursor.getCount()) {
				cursor.moveToFirst();
				long curTime = cursor.getLong(cursor.getColumnIndex("SENTTIME"));
				if (sentTime > curTime) {
					update = true;
				}
			} else {
				throw new IllegalStateException(String.format("wrong number of recent message for %s and %s", selfId,
						fellowId));
			}

			System.out.println("update:" + update);

			if (update) {
				SQLiteStatement updateStmt = db.compileStatement(SQL_RECENTMSG_UPDATE);
				updateStmt.clearBindings();
				updateStmt.bindString(1, messageId);
				updateStmt.bindLong(2, messageType);
				updateStmt.bindString(3, content);
				updateStmt.bindLong(4, sentTime);
				updateStmt.bindString(5, selfId);
				updateStmt.bindString(6, fellowId);
				updateStmt.executeUpdateDelete();
			} else {
				SQLiteStatement insertStmt = db.compileStatement(SQL_RECENTMSG_INSERT);
				insertStmt.clearBindings();
				insertStmt.bindString(1, selfId);
				insertStmt.bindString(2, fellowId);
				insertStmt.bindString(3, messageId);
				insertStmt.bindLong(4, messageType);
				insertStmt.bindString(5, content);
				insertStmt.bindLong(6, sentTime);
				insertStmt.executeInsert();
			}
			db.setTransactionSuccessful();
		} finally {
			if (null != db)
				db.endTransaction();
			db.close();
		}
	}

	public void deleteOneRecentMessage(String selfId, String fellowId) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		try {
			SQLiteStatement deleteStmt = db.compileStatement(SQL_RECENTMSG_ONEDELETE);
			deleteStmt.clearBindings();
			deleteStmt.bindString(1, selfId);
			deleteStmt.bindString(2, fellowId);
			deleteStmt.executeUpdateDelete();
		} finally {
			if (null != db)
				db.close();
		}
	}

	public void deleteAllRecentMessages(String selfId) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		try {
			SQLiteStatement deleteStmt = db.compileStatement(SQL_RECENTMSG_ALLDELETE);
			deleteStmt.clearBindings();
			deleteStmt.bindString(1, selfId);
			deleteStmt.executeUpdateDelete();
		} finally {
			if (null != db)
				db.close();
		}
	}

	public List<RecentMessage> queryRecentMessage(String selfId) {
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<RecentMessage> recentMsgs = new ArrayList<RecentMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_RECENTMSG_ALLSELECT, new String[] { selfId });

			while (cursor.moveToNext()) {

				RecentMessage msg = new RecentMessage();

				msg.setSelfId(cursor.getString(cursor.getColumnIndex("SELFID")));

				msg.setFellowId(cursor.getString(cursor.getColumnIndex("FELLOWID")));

				msg.setMessageType(cursor.getInt(cursor.getColumnIndex("MESSAGETYPE")));

				msg.setMessageId(cursor.getString(cursor.getColumnIndex("MESSAGEID")));

				msg.setSentTime(cursor.getLong(cursor.getColumnIndex("SENTTIME")));

				msg.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));

				recentMsgs.add(msg);
			}
		} finally {
			if (null != db)
				db.close();
		}

		return recentMsgs;
	}
}
