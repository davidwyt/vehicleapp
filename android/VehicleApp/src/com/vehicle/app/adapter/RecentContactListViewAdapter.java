package com.vehicle.app.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.mgrs.TopMsgerMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.InvitationVerdict;
import com.vehicle.app.msg.bean.RecentMessage;
import com.vehicle.app.utils.ImageUtil;

import cn.edu.sjtu.vehicleapp.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecentContactListViewAdapter extends BaseAdapter {

	private Vector<RecentMessage> recentMsgVecotr;
	private LayoutInflater inflater;

	private Context context;

	private static final int ICON_WIDTH = 64;
	private static final int ICON_HEIGHT = 64;

	public RecentContactListViewAdapter(Context context, Vector<RecentMessage> msgs) {
		this.inflater = LayoutInflater.from(context);
		this.recentMsgVecotr = msgs;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return recentMsgVecotr.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return recentMsgVecotr.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup viewgroup) {
		// TODO Auto-generated method stub
		if (null == view) {
			view = this.inflater.inflate(R.layout.layout_recentmsg_item, null);
		}

		TextView tvAlias = (TextView) view.findViewById(R.id.useritem_tv_username);
		TextView tvLastMessage = (TextView) view.findViewById(R.id.useritem_tv_lastmessage);
		TextView tvLastMessageDate = (TextView) view.findViewById(R.id.useritem_tv_lastmessagetime);
		ImageView ivHead = (ImageView) view.findViewById(R.id.useritem_iv_userhead);

		RecentMessage msg = this.recentMsgVecotr.get(pos);
		if (null == msg) {
			return view;
		}

		if (TopMsgerMgr.getInstance().isTop(msg.getFellowId())) {
			view.setBackgroundColor(inflater.getContext().getResources().getColor(R.color.white));
		} else {
			view.setBackgroundColor(inflater.getContext().getResources().getColor(R.color.gray));
		}

		String alias = "";

		String url = "";

		if (SelfMgr.getInstance().isDriver()) {

			Vendor vendor = SelfMgr.getInstance().getVendorInfo(msg.getFellowId());
			if (null != vendor) {
				alias = vendor.getName();
				url = vendor.getAvatar();
			}

		} else {
			Driver driver = SelfMgr.getInstance().getDriverInfo(msg.getFellowId());
			if (null != driver) {
				alias = driver.getAlias();
				url = driver.getAvatar();
			}
		}

		String lastMessage = formatLastMessage(msg);

		tvAlias.setText(alias);

		tvLastMessage.setText(lastMessage);

		tvLastMessageDate.setText(formatLastMessageTime(msg));

		ImageUtil.RenderImageView(url, ivHead, ICON_WIDTH, ICON_HEIGHT);

		return view;
	}

	private String formatLastMessage(RecentMessage msg) {
		if (IMessageItem.MESSAGE_TYPE_TEXT == msg.getMessageType()) {
			return msg.getContent();
		} else if (IMessageItem.MESSAGE_TYPE_LOCATION == msg.getMessageType()) {
			return context.getResources().getString(R.string.format_msg_location);
		} else if (IMessageItem.MESSAGE_TYPE_IMAGE == msg.getMessageType()) {
			return context.getResources().getString(R.string.format_msg_picture);
		} else if (IMessageItem.MESSAGE_TYPE_FELLOWSHIPINVITATION == msg.getMessageType()) {
			throw new IllegalArgumentException("invitation should not show here");
		} else if (IMessageItem.MESSAGE_TYPE_INVITATIONVERDICT == msg.getMessageType()) {
			if (msg.getContent().equalsIgnoreCase(InvitationVerdict.ACCEPTED.toString())) {
				return context.getResources().getString(R.string.format_msg_newfellow);
			} else {
				throw new IllegalArgumentException("rejuect invitaion verdict should not show here");
			}
		} else if (IMessageItem.MESSAGE_TYPE_FOLLOW == msg.getMessageType()) {
			return context.getResources().getString(R.string.format_msg_newfellow);
		} else if (IMessageItem.MESSAGE_TYPE_AUDIO == msg.getMessageType()) {
			return context.getResources().getString(R.string.tip_recentaudio);
		} else {
			throw new IllegalArgumentException("wrong message type");
		}
	}

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("deprecation")
	private String formatLastMessageTime(RecentMessage msg) {
		Calendar cal = Calendar.getInstance();
		Date msgDate = new Date(msg.getSentTime());
		/**
		 * if (msgDate.after(cal.getTime())) { throw new
		 * IllegalArgumentException("what a fucking time"); }
		 */

		if (msgDate.getYear() == cal.get(Calendar.YEAR) && msgDate.getMonth() == cal.get(Calendar.MONTH)
				&& msgDate.getDate() == cal.get(Calendar.DATE)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			return dateFormat.format(msgDate);
		}

		cal.add(Calendar.DATE, -1);
		if (msgDate.getYear() == cal.get(Calendar.YEAR) && msgDate.getMonth() == cal.get(Calendar.MONTH)
				&& msgDate.getDate() == cal.get(Calendar.DATE)) {
			return context.getResources().getString(R.string.yesterday);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(msgDate);
	}
}
