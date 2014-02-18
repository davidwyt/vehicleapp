package com.vehicle.app.activities;

import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.bean.InvitationVerdict;
import com.vehicle.app.msg.bean.InvitationVerdictMessage;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.worker.IMessageCourier;
import com.vehicle.app.msg.worker.InvitationVerdictMessageCourier;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FollowshipInvitationActivity extends Activity implements OnClickListener {

	private Button mBtnShopDetail;
	private Button mBtnAccept;
	private Button mBtnRejected;
	private Button mBtnBak;
	private TextView mTvInvitation;

	private FollowshipInvitationMessage mInvitation;

	public static final String KEY_FOLLOWSHIPINVITATION = "com.vehicle.app.key.followshipinvitation";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_followshipinvitation);
		initView();
	}

	private void initView() {
		this.mBtnShopDetail = (Button) this.findViewById(R.id.followshipinvitation_shopdetail);
		this.mBtnShopDetail.setOnClickListener(this);

		this.mBtnAccept = (Button) this.findViewById(R.id.followshipinvitation_accept);
		this.mBtnAccept.setOnClickListener(this);

		this.mBtnRejected = (Button) this.findViewById(R.id.followshipinvitation_reject);
		this.mBtnRejected.setOnClickListener(this);

		this.mBtnBak = (Button) this.findViewById(R.id.invitation_goback);
		this.mBtnBak.setOnClickListener(this);

		this.mTvInvitation = (TextView) this.findViewById(R.id.followshipinvitation_content);
	}

	private void initData() {
		Bundle bundle = this.getIntent().getExtras();
		if (null == bundle) {
			return;
		}

		this.mInvitation = (FollowshipInvitationMessage) bundle.getSerializable(KEY_FOLLOWSHIPINVITATION);

		String strInvitation = this.getResources().getString(R.string.followshipinvitationformatstr,
				this.mInvitation.getSource());
		
		this.mTvInvitation.setText(strInvitation);
	}

	@Override
	public void onStart() {
		super.onStart();
		
		initData();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View view) {
		if (R.id.followshipinvitation_shopdetail == view.getId()) {

		} else if (R.id.followshipinvitation_accept == view.getId()) {
			sendInvitationVerdict(true);
		} else if (R.id.followshipinvitation_reject == view.getId()) {
			sendInvitationVerdict(false);
		} else if (R.id.invitation_goback == view.getId()) {
			this.onBackPressed();
		} else {
			System.err.println("invalid id of clicked button in followshipinvitation form");
		}
	}

	private void sendInvitationVerdict(boolean isAccept) {
		InvitationVerdictMessage msg = new InvitationVerdictMessage();
		msg.setInvitationId(mInvitation.getId());
		msg.setSource(SelfMgr.getInstance().getId());
		msg.setTarget(mInvitation.getSource());
		msg.setFlag(MessageFlag.SELF);
		msg.setVerdict(isAccept ? InvitationVerdict.ACCEPTED : InvitationVerdict.REJECTED);

		IMessageCourier cpu = new InvitationVerdictMessageCourier(this.getApplicationContext());
		cpu.dispatch(msg);
	}
}
