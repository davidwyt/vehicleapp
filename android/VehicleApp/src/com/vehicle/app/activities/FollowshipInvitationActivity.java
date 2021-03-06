package com.vehicle.app.activities;

import java.util.Date;

import com.vehicle.app.bean.VendorDetail;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.bean.InvitationVerdict;
import com.vehicle.app.msg.bean.InvitationVerdictMessage;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.worker.IMessageCourier;
import com.vehicle.app.msg.worker.InvitationVerdictMessageCourier;
import com.vehicle.app.utils.ActivityUtil;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.web.bean.VendorSpecViewResult;
import com.vehicle.app.web.bean.WebCallBaseResult;
import com.vehicle.sdk.client.VehicleWebClient;

import cn.edu.sjtu.vehicleapp.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FollowshipInvitationActivity extends TemplateActivity implements OnClickListener {

	private Button mBtnShopDetail;
	private Button mBtnAccept;
	private Button mBtnRejected;
	private Button mBtnBak;
	private TextView mTvInvitation;

	private FollowshipInvitationMessage mInvitation;

	public static final String KEY_FOLLOWSHIPINVITATION = "com.vehicle.app.key.followshipinvitation";

	private View mInvFormView;
	private View mInvStatusView;
	private TextView mInvStatusMessageView;

	private RefreshVendorDetailTask mRefreshTask;
	private BroadcastReceiver mReceiver;

	public static final String KEY_INVVERDICT_ERRORMSG = "com.vehicle.app.key.invverdict.result.errmsg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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

		mInvFormView = findViewById(R.id.invitation_form);
		mInvStatusView = findViewById(R.id.followinv_status);
		mInvStatusMessageView = (TextView) findViewById(R.id.followinv_status_message);
	}

	private void initData() {
		Bundle bundle = this.getIntent().getExtras();
		if (null == bundle) {
			return;
		}

		this.mInvitation = (FollowshipInvitationMessage) bundle.getSerializable(KEY_FOLLOWSHIPINVITATION);

		if (null == this.mInvitation)
			return;

		String name = SelfMgr.getInstance().getFellowName(getApplicationContext(), this.mInvitation.getSource());

		String strInvitation = this.getResources().getString(R.string.followshipinvitationformatstr, name);

		this.mTvInvitation.setText(strInvitation);
	}

	@Override
	public void onStart() {
		super.onStart();

		initData();

		registerMessageReceiver();
	}

	@Override
	public void onStop() {
		super.onStop();
		this.unregisterMessageReceiver();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void attemptVendorHome() {
		if (null == this.mInvitation)
			return;

		if (mRefreshTask != null) {
			return;
		}

		mInvStatusMessageView.setText(R.string.tip_getvendordetailinprogress);
		ActivityUtil.showProgress(getApplicationContext(), mInvStatusView, mInvFormView, true);
		mRefreshTask = new RefreshVendorDetailTask();
		mRefreshTask.execute((Void) null);
	}

	@Override
	public void onClick(View view) {

		if (R.id.followshipinvitation_shopdetail == view.getId()) {
			attemptVendorHome();
		} else if (R.id.followshipinvitation_accept == view.getId()) {
			sendInvitationVerdict(true);
		} else if (R.id.followshipinvitation_reject == view.getId()) {
			sendInvitationVerdict(false);
		} else if (R.id.invitation_goback == view.getId()) {
			this.onBackPressed();
			this.finish();
		} else {
			System.err.println("invalid id of clicked button in followshipinvitation form");
		}
	}

	private void sendInvitationVerdict(boolean isAccept) {
		if (null == this.mInvitation)
			return;

		InvitationVerdictMessage msg = new InvitationVerdictMessage();
		msg.setInvitationId(mInvitation.getId());
		msg.setSource(SelfMgr.getInstance().getId());
		msg.setTarget(mInvitation.getSource());
		msg.setFlag(MessageFlag.SELF);
		msg.setVerdict(isAccept ? InvitationVerdict.ACCEPTED : InvitationVerdict.REJECTED);
		msg.setSentTime(new Date().getTime());

		IMessageCourier cpu = new InvitationVerdictMessageCourier(this.getApplicationContext());
		cpu.dispatch(msg);
	}

	public class RefreshVendorDetailTask extends AsyncTask<Void, Void, WebCallBaseResult> {
		@Override
		protected WebCallBaseResult doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			WebCallBaseResult result = null;
			try {
				VehicleWebClient webClient = new VehicleWebClient();
				result = webClient.ViewVendorAllDetail(mInvitation.getSource());

				if (null != result && result.isSuccess()) {
					VendorSpecViewResult vendorView = (VendorSpecViewResult) result;
					VendorDetail vendor = vendorView.getInfoBean();

					SelfMgr.getInstance().putUnknownVendorDetail(vendor);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(final WebCallBaseResult result) {
			mRefreshTask = null;

			ActivityUtil.showProgress(getApplicationContext(), mInvStatusView, mInvFormView, false);

			if (null == result || !result.isSuccess()) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_getvendordetailfailed),
						Toast.LENGTH_LONG).show();
			} else {
				Intent intent = new Intent(getApplicationContext(), VendorHomeActivity.class);
				intent.putExtra(VendorHomeActivity.KEY_VENDORID, mInvitation.getSource());
				intent.putExtra(VendorHomeActivity.KEY_PERSPECTIVE, VendorHomeActivity.PERSPECTIVE_INVITATION);
				startActivity(intent);
			}
		}

		@Override
		protected void onCancelled() {
			mRefreshTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mInvStatusView, mInvFormView, false);
		}
	}

	private void registerMessageReceiver() {

		mReceiver = new InvVerdictResultReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(Constants.ACTION_INVVERDICT_ACCEPTFAILED);
		filter.addAction(Constants.ACTION_INVVERDICT_ACCEPTSUCCESS);
		filter.addAction(Constants.ACTION_INVVERDICT_REJECTFAILED);
		filter.addAction(Constants.ACTION_INVVERDICT_REJECTSUCCESS);

		try {
			registerReceiver(mReceiver, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void unregisterMessageReceiver() {
		try {
			unregisterReceiver(this.mReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class InvVerdictResultReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();

			if (Constants.ACTION_INVVERDICT_ACCEPTSUCCESS.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_invverdictacceptsuccess),
						Toast.LENGTH_LONG).show();
				try {
					Intent contactIntent = new Intent();
					contactIntent.setClass(getApplicationContext(), RecentContactListActivity.class);
					startActivity(contactIntent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (Constants.ACTION_INVVERDICT_ACCEPTFAILED.equals(action)) {
				String error = intent.getStringExtra(KEY_INVVERDICT_ERRORMSG);
				Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
			} else if (Constants.ACTION_INVVERDICT_REJECTSUCCESS.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_invverdictrejectsuccess),
						Toast.LENGTH_LONG).show();
				onBackPressed();
				finish();
			} else if (Constants.ACTION_INVVERDICT_REJECTFAILED.equals(action)) {
				String error = intent.getStringExtra(KEY_INVVERDICT_ERRORMSG);
				Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
			}
		}
	}
}
