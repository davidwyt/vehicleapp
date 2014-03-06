package com.vehicle.app.msg.worker;

import java.util.List;

import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.FileMessage;
import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.InvitationVerdict;
import com.vehicle.app.msg.bean.InvitationVerdictMessage;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.bean.TextMessage;
import com.vehicle.imserver.dao.bean.FollowshipInvitation;
import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.service.bean.NewFileNotification;
import com.vehicle.service.bean.WakeupResponse;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

public class WakeupMessageCourier extends MessageBaseCourier {

	public WakeupMessageCourier(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private void processInvitation(FollowshipInvitation invitation) {
		if (FollowshipInvitation.STATUS_REQUESTED == invitation.getStatus()) {
			FollowshipInvitationMessage msg = new FollowshipInvitationMessage();
			msg.setId(invitation.getID());
			msg.setSource(invitation.getSource());
			msg.setTarget(invitation.getTarget());
			msg.setSentTime(invitation.getReqTime());
			msg.setFlag(MessageFlag.UNREAD);

			IMessageRecipient recipient = new FollowshipInvMessageRecipient(context, true);
			recipient.receive(msg);

		} else if (FollowshipInvitation.STATUS_ACCEPTED == invitation.getStatus()) {
			InvitationVerdictMessage msg = new InvitationVerdictMessage();
			msg.setInvitationId(invitation.getID());
			msg.setSource(invitation.getTarget());
			msg.setTarget(invitation.getSource());
			msg.setVerdict(InvitationVerdict.ACCEPTED);
			msg.setFlag(MessageFlag.UNREAD);

			IMessageRecipient recipient = new InvitationVerdictMessageRecipient(context, true);
			recipient.receive(msg);
		} else if (FollowshipInvitation.STATUS_REJECTED == invitation.getStatus()) {
			InvitationVerdictMessage msg = new InvitationVerdictMessage();
			msg.setInvitationId(invitation.getID());
			msg.setSource(invitation.getTarget());
			msg.setTarget(invitation.getSource());
			msg.setVerdict(InvitationVerdict.REJECTED);
			msg.setFlag(MessageFlag.UNREAD);

			IMessageRecipient recipient = new InvitationVerdictMessageRecipient(context, true);
			recipient.receive(msg);

		} else {
			System.out.println(String.format("%s is not new invitation message", invitation.getID()));
		}
	}

	private void processMessage(Message msg) {
		TextMessage textMsg = new TextMessage();
		textMsg.setId(msg.getId());
		textMsg.setSource(msg.getSource());
		textMsg.setMessageType(msg.getMessageType());
		textMsg.setContent(msg.getContent());
		textMsg.setSentTime(msg.getSentTime());
		textMsg.setTarget(msg.getTarget());

		IMessageRecipient cpu = new TextMessageRecipient(context, true);
		cpu.receive(textMsg);
	}

	private void processNewFile(NewFileNotification file) {
		System.out.println("notification token:" + file.getId());

		FileMessage msg = new FileMessage();
		msg.fromRawNotification(file);

		System.out.println("file token:" + msg.getToken());

		IMessageRecipient cpu = new FileMessageRecipient(context, true);
		cpu.receive(msg);
	}

	@Override
	public void dispatch(IMessageItem item) {
		// TODO Auto-generated method stub
		AsyncTask<Void, Void, WakeupResponse> asyncTask = new AsyncTask<Void, Void, WakeupResponse>() {

			@Override
			protected WakeupResponse doInBackground(Void... params) {
				// TODO Auto-generated method stub

				WakeupResponse resp = null;
				try {
					String id = SelfMgr.getInstance().getId();

					VehicleClient client = new VehicleClient(id);
					resp = client.Login(id);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (null != resp && resp.isSucess()) {
					List<FollowshipInvitation> newInvitations = resp.getNewInvitations();

					if (null != newInvitations) {
						for (FollowshipInvitation invitation : newInvitations) {
							processInvitation(invitation);
						}
					}

					List<Message> newMessages = resp.getNewMessages();
					if (null != newMessages) {
						for (Message msg : newMessages) {
							processMessage(msg);
						}
					}

					List<NewFileNotification> newFiles = resp.getNewFiles();
					if (null != newFiles) {
						for (NewFileNotification file : newFiles) {
							processNewFile(file);
						}
					}

					String id = SelfMgr.getInstance().getId();

					try {
						VehicleClient client = new VehicleClient(id);
						client.AllAck(id);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				return resp;
			}

			@Override
			protected void onPostExecute(WakeupResponse result) {
				if (null == result || !result.isSucess()) {
					System.out.println("login to imserver failed");
					return;
				}
			}
		};

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			asyncTask.execute();
		}
	}

}
