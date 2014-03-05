package com.vehicle.app.activities;

import com.vehicle.app.bean.Driver;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.worker.FollowshipInvMessageCourier;
import com.vehicle.app.msg.worker.IMessageCourier;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.ImageUtil;

import cn.edu.sjtu.vehicleapp.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DriverInfoOldActivity extends TemplateActivity {

	private Button mBtnBack;
	private Button mBtnConnect;
	private TextView mTvTitleName;

	private ImageView mIvHead;
	private TextView mTvName;
	private TextView mTvAge;
	private TextView mTvSex;
	private TextView mTvPerInfo;
	private TextView mTvCarInfo;

	private Driver mDriver;

	public static final String KEY_DRIVERID = "com.vehicle.app.key.driver.id";
	public static final String KEY_ISNEARBY = "com.vehicle.app.key.driver.isnearby";

	private BroadcastReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_driverhome);
		initView();
	}

	private void initView() {
		this.mBtnBack = (Button) this.findViewById(R.id.driver_btn_back);
		this.mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DriverInfoOldActivity.this.onBackPressed();
			}
		});

		this.mBtnConnect = (Button) this.findViewById(R.id.driver_btn_connect);
		this.mBtnConnect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				connectDriver();
			}

		});

		this.mTvTitleName = (TextView) this.findViewById(R.id.driver_title_name);

		this.mIvHead = (ImageView) this.findViewById(R.id.driverinfo_icon);
		this.mTvName = (TextView) this.findViewById(R.id.driverinfo_name);
		this.mTvAge = (TextView) this.findViewById(R.id.driverinfo_age);
		this.mTvSex = (TextView) this.findViewById(R.id.driverinfo_sex);
		this.mTvPerInfo = (TextView) this.findViewById(R.id.driver_driverinfo);
		this.mTvCarInfo = (TextView) this.findViewById(R.id.driver_carinfo);
	}

	private void connectDriver() {

		if (null == this.mDriver)
			return;

		if (SelfMgr.getInstance().isVendorFellow(this.mDriver.getId())) {
			Intent intent = new Intent(this, ChatActivity.class);
			intent.putExtra(ChatActivity.KEY_FELLOWID, this.mDriver.getId());
			intent.putExtra(ChatActivity.KEY_CHATSTYLE, ChatActivity.CHAT_STYLE_2ONE);
			this.startActivity(intent);
		} else {
			FollowshipInvitationMessage msg = new FollowshipInvitationMessage();
			msg.setFlag(MessageFlag.SELF);
			msg.setId("");
			msg.setSource(SelfMgr.getInstance().getId());
			msg.setTarget(this.mDriver.getId());

			IMessageCourier msgCourier = new FollowshipInvMessageCourier(this.getApplicationContext());
			msgCourier.dispatch(msg);
		}
	}

	private void initData() {
		Bundle bundle = this.getIntent().getExtras();
		if (null == bundle) {
			return;
		}

		String driverId = bundle.getString(KEY_DRIVERID);
		boolean isNearby = bundle.getBoolean(KEY_ISNEARBY);
		if (isNearby) {
			this.mDriver = SelfMgr.getInstance().getNearbyDriver(driverId);
		} else {
			this.mDriver = SelfMgr.getInstance().getVendorFellow(driverId);
		}

		if (null == mDriver) {
			return;
		}

		this.mTvTitleName.setText(this.mDriver.getAlias());
		this.mTvName.setText(this.mDriver.getAlias());

		this.mTvAge.setText(this.mDriver.getBirthday());
		this.mTvSex.setText(this.mDriver.getSex());
		this.mTvPerInfo.setText(this.mDriver.getIntroduction());
		this.mTvCarInfo.setText("");

		String url = mDriver.getAvatar();
		ImageUtil.RenderImageView(url, mIvHead, -1, -1);
	}

	private void registerMessageReceiver() {

		mReceiver = new InvitationResultReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(Constants.ACTION_INVITATION_SUCCESS);
		filter.addAction(Constants.ACTION_INVITATION_FAILED);

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

	@Override
	protected void onStart() {
		super.onStart();
		initData();
		registerMessageReceiver();
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterMessageReceiver();
	}

	class InvitationResultReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();

			if (Constants.ACTION_INVITATION_SUCCESS.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_invitationsuccess),
						Toast.LENGTH_LONG).show();
			} else if (Constants.ACTION_INVITATION_FAILED.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_invitationfailed),
						Toast.LENGTH_LONG).show();
			}
		}

	}

}
