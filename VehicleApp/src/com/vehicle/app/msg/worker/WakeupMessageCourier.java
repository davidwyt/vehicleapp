package com.vehicle.app.msg.worker;

import java.util.List;

import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.InvitationVerdict;
import com.vehicle.app.msg.bean.InvitationVerdictMessage;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.imserver.dao.bean.FollowshipInvitation;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.service.bean.LoginResponse;

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

			IMessageRecipient recipient = new FollowshipInvMessageRecipient(context);
			recipient.receive(msg);

		} else if (FollowshipInvitation.STATUS_ACCEPTED == invitation.getStatus()) {
			InvitationVerdictMessage msg = new InvitationVerdictMessage();
			msg.setInvitationId(invitation.getID());
			msg.setSource(invitation.getTarget());
			msg.setTarget(invitation.getSource());
			msg.setVerdict(InvitationVerdict.ACCEPTED);
			msg.setFlag(MessageFlag.UNREAD);

			IMessageRecipient recipient = new InvitationVerdictMessageRecipient(context);
			recipient.receive(msg);
		} else if (FollowshipInvitation.STATUS_REJECTED == invitation.getStatus()) {
			InvitationVerdictMessage msg = new InvitationVerdictMessage();
			msg.setInvitationId(invitation.getID());
			msg.setSource(invitation.getTarget());
			msg.setTarget(invitation.getSource());
			msg.setVerdict(InvitationVerdict.REJECTED);
			msg.setFlag(MessageFlag.UNREAD);

			IMessageRecipient recipient = new InvitationVerdictMessageRecipient(context);
			recipient.receive(msg);

		} else {
			System.out.println(String.format("%s is not new invitation message", invitation.getID()));
		}

	}

	@Override
	public void dispatch(IMessageItem item) {
		// TODO Auto-generated method stub
		AsyncTask<Void, Void, LoginResponse> asyncTask = new AsyncTask<Void, Void, LoginResponse>() {

			@Override
			protected LoginResponse doInBackground(Void... params) {
				// TODO Auto-generated method stub

				LoginResponse resp = null;
				try {
					String id = SelfMgr.getInstance().getId();

					VehicleClient client = new VehicleClient(id);
					resp = client.Login(id);
				} catch (Exception e) {
					e.printStackTrace();
				}

				return resp;
			}

			@Override
			protected void onPostExecute(LoginResponse result) {
				if (null == result) {
					System.out.println("login to imserver failed");
					return;
				}

				List<FollowshipInvitation> newInvitations = result.getNewInvitations();

				for (FollowshipInvitation invitation : newInvitations) {
					processInvitation(invitation);
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
