package com.vehicle.app.mgrs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;

import com.vehicle.app.db.DBManager;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.StringUtil;

public class TopMsgerMgr {

	private TopMsgerMgr() {

	}

	private static class InstanceHolder {
		private static TopMsgerMgr instance = new TopMsgerMgr();
	}

	public static TopMsgerMgr getInstance() {
		return InstanceHolder.instance;
	}

	private List<String> topMsgs = Collections.synchronizedList(new ArrayList<String>());

	private Context context;

	public synchronized void init(Context context, String hostId) {
		this.context = context;

		try {
			DBManager dbMgr = new DBManager(context);
			String tops = dbMgr.selectMsgTops(hostId);

			topMsgs.clear();
			String[] temp = tops.split(Constants.COMMA);

			if (null != temp) {
				for (String top : temp) {
					topMsgs.add(top);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void addTopMsg(String host, String memberId) {

		try {
			List<String> newOne = new ArrayList<String>();
			Collections.copy(newOne, this.topMsgs);
			newOne.add(0, memberId);

			String tops = StringUtil.JointString(newOne, Constants.COMMA);

			DBManager dbMgr = new DBManager(context);
			dbMgr.insertOrUpdateTopMsg(host, tops);
			topMsgs.add(0, memberId);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void removeTopMsg(String host, String memberId) {
		try {
			List<String> newOne = new ArrayList<String>();
			Collections.copy(newOne, this.topMsgs);

			while (true) {
				int index = -1;
				for (int i = 0; i < newOne.size(); i++) {
					if (newOne.get(i).equals(memberId)) {
						index = i;
						break;
					}
				}

				if (index == -1) {
					break;
				} else {
					newOne.remove(index);
				}
			}

			String tops = StringUtil.JointString(newOne, Constants.COMMA);

			DBManager dbMgr = new DBManager(context);
			dbMgr.insertOrUpdateTopMsg(host, tops);
			topMsgs.clear();
			topMsgs.addAll(newOne);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized boolean isTop(String memberId) {
		for (String top : this.topMsgs) {
			if (top.equals(memberId)) {
				return true;
			}
		}

		return false;
	}

	public synchronized List<String> getTops() {
		return this.topMsgs;
	}
}
