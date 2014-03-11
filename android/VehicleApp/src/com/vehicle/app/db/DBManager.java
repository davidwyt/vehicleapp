package com.vehicle.app.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.vehicle.app.bean.RoleInfo;
import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.bean.InvitationVerdict;
import com.vehicle.app.msg.bean.InvitationVerdictMessage;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.bean.FileMessage;
import com.vehicle.app.msg.bean.RecentMessage;
import com.vehicle.app.msg.bean.TextMessage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class DBManager {

	private DBHelper mDBHelper;

	private static Lock lock = new ReentrantLock();

	private final static String SQL_TEXTMESSAGE_INSERT = "INSERT INTO `TEXTMESSAGE`(`ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG`, `MSGTYPE`) VALUES(?, ?, ?, ?, ?, ?, ?);";
	private final static String SQL_TEXTMESSAGE_ALLSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG`, `MSGTYPE` FROM `TEXTMESSAGE` WHERE (`SOURCE` = ? AND `TARGET` = ? AND `FLAG` = ?) OR (`SOURCE` = ? AND `TARGET` = ? AND (`FLAG` = ? OR `FLAG` = ?)) ORDER BY `SENTTIME` ASC;";
	private final static String SQL_TEXTMESSAGE_FLAGSELECT = "SELECT `ID`, `SOURCE`, `TARGET`, `CONTENT`, `SENTTIME`, `FLAG`, `MSGTYPE` FROM `TEXTMESSAGE` WHERE `FLAG` = ? AND `SOURCE` = ? AND `TARGET` = ? ORDER BY `SENTTIME` ASC;";
	private final static String SQL_TEXTMESSAGE_FLAGUPDATE = "UPDATE `TEXTMESSAGE` SET `FLAG`= ? WHERE `SOURCE` = ? AND `TARGET` = ? AND `FLAG` = ?;";
	private final static String SQL_TEXTMESSAGE_DELETEALL = "DELETE FROM `TEXTMESSAGE` WHERE (`SOURCE` = ? AND `TARGET` = ? AND `FLAG` = ?) OR (`SOURCE` = ? AND `TARGET` = ? AND (`FLAG` = ? OR `FLAG` = ?));";

	private final static String SQL_FILEMESSAGE_INSERT = "INSERT INTO `FILEMESSAGE` (`TOKEN`, `SOURCE`, `TARGET`, `PATH`, `SENTTIME`, `FLAG`, `MSGTYPE`) VALUES(?, ?, ?, ?, ?, ?, ?);";
	private final static String SQL_FILEMESSAGE_ALLSELECT = "SELECT `TOKEN`, `SOURCE`, `TARGET`, `PATH`, `SENTTIME`, `FLAG`, `MSGTYPE` FROM `FILEMESSAGE` WHERE (`SOURCE` = ? AND `TARGET` = ? AND `FLAG` = ?) OR (`SOURCE` = ? AND `TARGET` = ? AND (`FLAG` = ? OR `FLAG` = ?)) ORDER BY `SENTTIME` ASC;";
	private final static String SQL_FILEMESSAGE_FLAGSELECT = "SELECT `TOKEN`, `SOURCE`, `TARGET`, `PATH`, `SENTTIME`, `FLAG`, `MSGTYPE` FROM `FILEMESSAGE` WHERE `FLAG` = ? AND `SOURCE` = ? AND `TARGET` = ? ORDER BY `SENTTIME` ASC;";
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

	private final static String SQL_TOPMSG_INSERT = "INSERT INTO `TOPMESSAGE`(`HOST`, `TOPMSG`) VALUES(?, ?);";
	private final static String SQL_TOPMSG_UPDATE = "UPDATE `TOPMESSAGE` SET `TOPMSG` = ? WHERE `HOST` = ?;";
	private final static String SQL_TOPMSG_SELECT = "SELECT `TOPMSG` FROM `TOPMESSAGE` WHERE `HOST` = ?;";

	private final static String SQL_LASTONBOARD_SELECT = "SELECT `USERNAME`, `PASSWORD`, `ROLE`, `AUTOLOG` FROM `LASTONBOARDROLE` WHERE `ROLE` = ?;";
	private final static String SQL_LASTONBOARD_INSERT = "INSERT INTO `LASTONBOARDROLE` (`USERNAME`, `PASSWORD`, `ROLE`, `AUTOLOG`) VALUES(?, ?, ?, ?);";
	private final static String SQL_LASTONBOARD_DELETEALL = "DELETE FROM `LASTONBOARDROLE` WHERE `ROLE` = ?;";

	public DBManager(Context context) {
		mDBHelper = new DBHelper(context);
	}

	public RoleInfo selectLastOnBoard(int roleType) {
		lock.lock();
		SQLiteDatabase db = mDBHelper.getReadableDatabase();

		try {
			Cursor cursor = db.rawQuery(SQL_LASTONBOARD_SELECT, new String[] { Integer.toString(roleType) });

			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				String name = cursor.getString(cursor.getColumnIndex("USERNAME"));
				String pwd = cursor.getString(cursor.getColumnIndex("PASSWORD"));
				int role = cursor.getInt(cursor.getColumnIndex("ROLE"));
				int auto = cursor.getInt(cursor.getColumnIndex("AUTOLOG"));

				RoleInfo roleInfo = new RoleInfo();
				roleInfo.setUserName(name);
				roleInfo.setPassword(pwd);
				roleInfo.setRoleType(role);
				roleInfo.setIsAutoLog(auto != 0);

				cursor.close();
				return roleInfo;
			} else {
				return null;
			}
		} finally {
			if (null != db) {
				try {
					db.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			lock.unlock();
		}
	}

	public void updateLastOnboard(String userName, String password, int role, boolean isAuto) {
		lock.lock();
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		try {
			db.beginTransaction();

			SQLiteStatement delStmt = db.compileStatement(SQL_LASTONBOARD_DELETEALL);
			delStmt.bindLong(1, role);
			delStmt.executeUpdateDelete();

			SQLiteStatement insertStmt = db.compileStatement(SQL_LASTONBOARD_INSERT);
			insertStmt.clearBindings();
			insertStmt.bindString(1, userName);
			insertStmt.bindString(2, password);
			insertStmt.bindLong(3, role);
			insertStmt.bindLong(4, isAuto ? 1 : 0);
			insertStmt.executeInsert();

			db.setTransactionSuccessful();
		} finally {
			if (null != db) {
				try {
					db.endTransaction();
					db.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				lock.unlock();
			}
		}
	}

	public void insertOrUpdateTopMsg(String host, String tops) {

		lock.lock();

		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		try {
			db.beginTransaction();

			Cursor cursor = db.rawQuery(SQL_TOPMSG_SELECT, new String[] { host });

			if (0 >= cursor.getCount()) {
				SQLiteStatement insertStmt = db.compileStatement(SQL_TOPMSG_INSERT);
				insertStmt.clearBindings();
				insertStmt.bindString(1, host);
				insertStmt.bindString(2, tops);
				insertStmt.executeInsert();
			} else {
				SQLiteStatement updateStmt = db.compileStatement(SQL_TOPMSG_UPDATE);
				updateStmt.clearBindings();
				updateStmt.bindString(1, tops);
				updateStmt.bindString(2, host);
				updateStmt.executeUpdateDelete();
			}

			cursor.close();
			db.setTransactionSuccessful();

		} finally {
			try {

				if (null != db) {
					db.endTransaction();
					db.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
	}

	public String selectMsgTops(String host) {

		lock.lock();

		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		try {
			Cursor cursor = db.rawQuery(SQL_TOPMSG_SELECT, new String[] { host });

			if (null == cursor || 0 >= cursor.getCount()) {
				cursor.close();
				return "";
			}

			cursor.moveToFirst();
			String ret = cursor.getString(cursor.getColumnIndex("TOPMSG"));
			cursor.close();
			return ret;
		} finally {
			try {
				if (null != db) {
					db.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
	}

	public void deleteAllMessages(String selfId, String fellowId) {
		lock.lock();

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

		} finally {
			try {
				if (null != db) {
					db.endTransaction();
					db.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
	}

	public void insertTextMessage(String id, String source, String target, String content, long sentTime,
			MessageFlag flag, int msgType) {

		lock.lock();

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
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
	}

	public void insertTextMessage(TextMessage msg) {
		insertTextMessage(msg.getId(), msg.getSource(), msg.getTarget(), msg.getContent(), msg.getSentTime(),
				msg.getFlag(), msg.getMessageType());
	}

	public void updateUnreadTextMessageFlag(String selfId, String fellowId) {
		lock.lock();

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
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
	}

	public List<TextMessage> queryAllTextMessage(String selfId, String fellowId) {

		lock.lock();

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<TextMessage> messages = new ArrayList<TextMessage>();

		try {
			Cursor cursor = db.rawQuery(
					SQL_TEXTMESSAGE_ALLSELECT,
					new String[] { selfId, fellowId, MessageFlag.SELF.toString(), fellowId, selfId,
							MessageFlag.READ.toString(), MessageFlag.UNREAD.toString() });
			if (null != cursor && cursor.getCount() > 0) {

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
			}

			cursor.close();
		} finally {
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}

		return messages;
	}

	public List<TextMessage> queryUnreadTextMessage(String selfId, String fellowId) {

		lock.lock();

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<TextMessage> messages = new ArrayList<TextMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_TEXTMESSAGE_FLAGSELECT, new String[] { MessageFlag.UNREAD.toString(),
					fellowId, selfId });
			if (null != cursor && cursor.getCount() > 0) {

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
			}

			cursor.close();
		} finally {
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
		return messages;
	}

	public long insertFileMessage(String id, String source, String target, String path, long sentTime,
			MessageFlag flag, int type) {

		lock.lock();
		SQLiteDatabase db = mDBHelper.getWritableDatabase();

		try {
			SQLiteStatement insertStmt = db.compileStatement(SQL_FILEMESSAGE_INSERT);
			insertStmt.clearBindings();
			insertStmt.bindString(1, id);
			insertStmt.bindString(2, source);
			insertStmt.bindString(3, target);

			insertStmt.bindString(4, path);
			insertStmt.bindLong(5, sentTime);
			insertStmt.bindString(6, flag.toString());
			insertStmt.bindLong(7, type);
			return insertStmt.executeInsert();

		} finally {
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
	}

	public void insertFileMessage(FileMessage msg) {

		insertFileMessage(msg.getToken(), msg.getSource(), msg.getTarget(), msg.getPath(), msg.getSentTime(),
				msg.getFlag(), msg.getMessageType());
	}

	public void updateUnreadFileMessageFlag(String selfId, String fellowId) {
		lock.lock();

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
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
	}

	public List<FileMessage> queryAllFileMessage(String selfId, String fellowId) {
		lock.lock();

		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		List<FileMessage> messages = new ArrayList<FileMessage>();

		try {
			Cursor cursor = db.rawQuery(
					SQL_FILEMESSAGE_ALLSELECT,
					new String[] { selfId, fellowId, MessageFlag.SELF.toString(), fellowId, selfId,
							MessageFlag.READ.toString(), MessageFlag.UNREAD.toString() });

			if (null != cursor && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {

					FileMessage msg = new FileMessage();

					msg.setToken(cursor.getString(cursor.getColumnIndex("TOKEN")));

					msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

					msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

					msg.setPath(cursor.getString(cursor.getColumnIndex("PATH")));

					msg.setSentTime(cursor.getLong(cursor.getColumnIndex("SENTTIME")));

					msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

					msg.setMsgType(cursor.getInt(cursor.getColumnIndex("MSGTYPE")));
					messages.add(msg);
				}

				cursor.close();
			}
		} finally {
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
		return messages;
	}

	public List<FileMessage> queryUnreadFileMessage(String selfId, String fellowId) {

		lock.lock();

		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		List<FileMessage> messages = new ArrayList<FileMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_FILEMESSAGE_FLAGSELECT, new String[] { MessageFlag.UNREAD.toString(),
					fellowId, selfId });

			if (null != cursor && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {

					FileMessage msg = new FileMessage();

					msg.setToken(cursor.getString(cursor.getColumnIndex("TOKEN")));

					msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

					msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

					msg.setPath(cursor.getString(cursor.getColumnIndex("PATH")));

					msg.setSentTime(cursor.getLong(cursor.getColumnIndex("SENTTIME")));

					msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));
					msg.setMsgType(cursor.getInt(cursor.getColumnIndex("MSGTYPE")));
					messages.add(msg);
				}
			}

			cursor.close();
		} finally {
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
		return messages;
	}

	public void insertInvitationVerdictMessage(String invitationId, String source, String target,
			InvitationVerdict verdict, MessageFlag flag) {

		lock.lock();

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
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
	}

	public void insertInvitationVerdictMessage(InvitationVerdictMessage msg) {
		insertInvitationVerdictMessage(msg.getInvitationId(), msg.getSource(), msg.getTarget(), msg.getVerdict(),
				msg.getFlag());
	}

	public void updateUnreadInvitationVerdictMessageFlag(String invitationId) {
		lock.lock();

		SQLiteDatabase db = mDBHelper.getReadableDatabase();

		try {
			SQLiteStatement updateStmt = db.compileStatement(SQL_INVVERDICTMESSAGE_FLAGUPDATE);
			updateStmt.clearBindings();
			updateStmt.bindString(1, MessageFlag.READ.toString());
			updateStmt.bindString(2, invitationId);
			updateStmt.bindString(3, MessageFlag.UNREAD.toString());
			updateStmt.executeUpdateDelete();
		} finally {
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
	}

	public List<InvitationVerdictMessage> queryAllInvitationVerdictMessage(String selfId) {

		lock.lock();

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<InvitationVerdictMessage> messages = new ArrayList<InvitationVerdictMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_INVVERDICTMESSAGE_ALLSELECT,
					new String[] { selfId, MessageFlag.READ.toString(), MessageFlag.UNREAD.toString() });

			if (null != cursor && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {

					InvitationVerdictMessage msg = new InvitationVerdictMessage();

					msg.setInvitationId(cursor.getString(cursor.getColumnIndex("INVITATIONID")));

					msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

					msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

					msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

					msg.setVerdict(InvitationVerdict.valueOf(cursor.getString(cursor.getColumnIndex("VERDICT"))));

					messages.add(msg);
				}
			}

			cursor.close();
		} finally {
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
		return messages;
	}

	public List<InvitationVerdictMessage> queryUnreadInvitationVerdictMessage(String selfId) {

		lock.lock();

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<InvitationVerdictMessage> messages = new ArrayList<InvitationVerdictMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_INVVERDICTMESSAGE_FLAGSELECT,
					new String[] { selfId, MessageFlag.UNREAD.toString() });

			if (null != cursor && cursor.getCount() > 0) {

				while (cursor.moveToNext()) {

					InvitationVerdictMessage msg = new InvitationVerdictMessage();

					msg.setInvitationId(cursor.getString(cursor.getColumnIndex("INVITATIONID")));

					msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

					msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

					msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

					msg.setVerdict(InvitationVerdict.valueOf(cursor.getString(cursor.getColumnIndex("VERDICT"))));

					messages.add(msg);
				}
			}

			cursor.close();
		} finally {
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			lock.unlock();
		}
		return messages;
	}

	public void insertFollowshipInvMessage(String invitationId, String source, String target, long sentTime,
			MessageFlag flag) {

		lock.lock();
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
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
	}

	public void insertFollowshipInvMessage(FollowshipInvitationMessage msg) {
		insertFollowshipInvMessage(msg.getId(), msg.getSource(), msg.getTarget(), msg.getSentTime(), msg.getFlag());
	}

	public void updateUnreadFollowshipInvMessageFlag(String invitationId) {
		lock.lock();
		SQLiteDatabase db = mDBHelper.getReadableDatabase();

		try {
			SQLiteStatement updateStmt = db.compileStatement(SQL_FOLLOWINV_FLAGUPDATE);
			updateStmt.clearBindings();
			updateStmt.bindString(1, MessageFlag.READ.toString());
			updateStmt.bindString(2, invitationId);
			updateStmt.bindString(3, MessageFlag.UNREAD.toString());
			updateStmt.executeUpdateDelete();
		} finally {
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
	}

	public List<FollowshipInvitationMessage> queryAllFollowshipInvMessage(String selfId) {
		lock.lock();

		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<FollowshipInvitationMessage> messages = new ArrayList<FollowshipInvitationMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_FOLLOWINV_ALLSELECT, new String[] { selfId, MessageFlag.READ.toString(),
					MessageFlag.UNREAD.toString() });

			if (null != cursor && cursor.getCount() > 0) {

				while (cursor.moveToNext()) {

					FollowshipInvitationMessage msg = new FollowshipInvitationMessage();

					msg.setId(cursor.getString(cursor.getColumnIndex("ID")));

					msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

					msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

					msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

					msg.setSentTime(cursor.getLong(cursor.getColumnIndex("SENTTIME")));

					messages.add(msg);
				}
			}

			cursor.close();
		} finally {
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
		return messages;
	}

	public List<FollowshipInvitationMessage> queryUnreadFollowshipInvMessage(String target) {
		lock.lock();
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<FollowshipInvitationMessage> messages = new ArrayList<FollowshipInvitationMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_FOLLOWINV_FLAGSELECT,
					new String[] { target, MessageFlag.UNREAD.toString() });
			if (null != cursor && cursor.getCount() > 0) {

				while (cursor.moveToNext()) {

					FollowshipInvitationMessage msg = new FollowshipInvitationMessage();

					msg.setId(cursor.getString(cursor.getColumnIndex("ID")));

					msg.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));

					msg.setTarget(cursor.getString(cursor.getColumnIndex("TARGET")));

					msg.setFlag(MessageFlag.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

					msg.setSentTime(cursor.getLong(cursor.getColumnIndex("SENTTIME")));

					messages.add(msg);
				}
			}

			cursor.close();
		} finally {
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
		return messages;
	}

	public void insertOrUpdateRecentMessage(RecentMessage recentMsg) {
		insertOrUpdateRecentMessage(recentMsg.getSelfId(), recentMsg.getFellowId(), recentMsg.getMessageId(),
				recentMsg.getMessageType(), recentMsg.getContent(), recentMsg.getSentTime());
	}

	public void insertOrUpdateRecentMessage(String selfId, String fellowId, String messageId, int messageType,
			String content, long sentTime) {
		lock.lock();

		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		try {
			db.beginTransaction();

			boolean update = false;
			Cursor cursor = db.rawQuery(SQL_RECENTMSG_ONESELECT, new String[] { selfId, fellowId });
			if (null == cursor || 0 == cursor.getCount()) {
				update = false;
			} else if (1 == cursor.getCount()) {
				/**
				 * cursor.moveToFirst(); long curTime =
				 * cursor.getLong(cursor.getColumnIndex("SENTTIME")); if
				 * (sentTime > curTime) { update = true; }
				 */
				update = true;
			} else {
				SQLiteStatement deleteRecentMsgStmt = db.compileStatement(SQL_RECENTMSG_DELETEALL);
				deleteRecentMsgStmt.clearBindings();

				deleteRecentMsgStmt.bindString(1, selfId);
				deleteRecentMsgStmt.bindString(2, fellowId);

				deleteRecentMsgStmt.executeUpdateDelete();

				update = false;
				/**
				 * throw new IllegalStateException(String.format(
				 * "wrong number of recent message for %s and %s", selfId,
				 * fellowId));
				 */
			}

			cursor.close();
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
			try {
				if (null != db) {
					db.endTransaction();
					db.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
	}

	public void deleteOneRecentMessage(String selfId, String fellowId) {
		lock.lock();

		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		try {
			SQLiteStatement deleteStmt = db.compileStatement(SQL_RECENTMSG_ONEDELETE);
			deleteStmt.clearBindings();
			deleteStmt.bindString(1, selfId);
			deleteStmt.bindString(2, fellowId);
			deleteStmt.executeUpdateDelete();
		} finally {
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
	}

	public void deleteAllRecentMessages(String selfId) {
		lock.lock();
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		try {
			SQLiteStatement deleteStmt = db.compileStatement(SQL_RECENTMSG_ALLDELETE);
			deleteStmt.clearBindings();
			deleteStmt.bindString(1, selfId);
			deleteStmt.executeUpdateDelete();
		} finally {
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}
	}

	public List<RecentMessage> queryRecentMessage(String selfId) {
		lock.lock();
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		List<RecentMessage> recentMsgs = new ArrayList<RecentMessage>();

		try {
			Cursor cursor = db.rawQuery(SQL_RECENTMSG_ALLSELECT, new String[] { selfId });
			if (null != cursor && cursor.getCount() > 0) {

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
			}

			cursor.close();
		} finally {
			try {
				if (null != db)
					db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			lock.unlock();
		}

		return recentMsgs;
	}
}
