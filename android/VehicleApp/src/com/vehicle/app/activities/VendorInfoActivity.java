package com.vehicle.app.activities;

import java.util.Date;

import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.FollowshipMessage;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.worker.FollowshipMessageCourier;
import com.vehicle.app.msg.worker.IMessageCourier;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.ImageUtil;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VendorInfoActivity extends Activity {

	private Button mBtnBack;
	private Button mBtnConnect;
	private TextView mTvTitleName;

	private ImageView mIvhead;
	private TextView mTvName;
	private TextView mTvBusinessTime;

	private TextView mTvScore;
	private TextView mTvReception;
	private TextView mTvEnvironment;
	private TextView mTvPrice;
	private TextView mTvTechnology;
	private TextView mTvEfficiency;

	private TextView mTvMobileNum;
	private TextView mTvAddr;

	private Vendor mVendor;

	public static final String KEY_VENDORID = "com.vehicle.app.key.vendorinfoactivity.vendor.id";
	public static final String KEY_ISNEARBY = "com.vehicle.app.key.vendorinfoactivity.vendor.isnearby";

	private BroadcastReceiver mReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_vendorinfo);

		initView();
	}

	private void initView() {
		this.mBtnBack = (Button) this.findViewById(R.id.vendor_btn_back);

		this.mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

		this.mBtnConnect = (Button) this.findViewById(R.id.vendor_btn_connect);
		this.mBtnConnect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				connectVendor();
			}

		});

		this.mIvhead = (ImageView) this.findViewById(R.id.vendor_icon);
		this.mTvTitleName = (TextView) this.findViewById(R.id.vendor_title_name);
		this.mTvBusinessTime = (TextView) this.findViewById(R.id.vendor_businesstime);

		this.mTvScore = (TextView) this.findViewById(R.id.vendor_score);
		this.mTvEfficiency = (TextView) this.findViewById(R.id.vendor_efficiency);
		this.mTvEnvironment = (TextView) this.findViewById(R.id.vendor_environment);
		this.mTvPrice = (TextView) this.findViewById(R.id.vendor_price);
		this.mTvReception = (TextView) this.findViewById(R.id.vendor_reception);
		this.mTvTechnology = (TextView) this.findViewById(R.id.vendor_technology);

		this.mTvName = (TextView) this.findViewById(R.id.vendor_name);
		this.mTvAddr = (TextView) this.findViewById(R.id.vendor_address);

		this.mTvMobileNum = (TextView) this.findViewById(R.id.vendor_mobilenum);
	}

	private void registerMessageReceiver() {

		mReceiver = new FollowshipResultReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(Constants.ACTION_FOLLOWSHIP_FAILED);
		filter.addAction(Constants.ACTION_FOLLOWSHIP_SUCCESS);

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

	private void initData() {
		Bundle bundle = this.getIntent().getExtras();
		if (null == bundle) {
			return;
		}
		String vendorId = bundle.getString(KEY_VENDORID);
		boolean isNearby = bundle.getBoolean(KEY_ISNEARBY);

		if (isNearby) {
			mVendor = SelfMgr.getInstance().getNearbyVendor(vendorId);
		} else {
			this.mVendor = SelfMgr.getInstance().getFavVendor(vendorId);
		}

		if (null == mVendor) {
			return;
		}

		this.mTvTitleName.setText(mVendor.getName());

		this.mTvName.setText(mVendor.getName());
		this.mTvBusinessTime.setText(mVendor.getAnswerTime());

		String url = mVendor.getAvatar();
		ImageUtil.RenderImageView(url, mIvhead, -1, -1);

		this.mTvScore.setText(Double.toString(mVendor.getScore()));
		this.mTvEfficiency.setText(Double.toString(mVendor.getEfficiencyScore()));
		this.mTvEnvironment.setText(Double.toString(mVendor.getEnvironmentScore()));
		this.mTvPrice.setText(Double.toString(mVendor.getPriceScore()));
		this.mTvReception.setText(Double.toString(mVendor.getReceptionScore()));
		this.mTvTechnology.setText(Double.toString(mVendor.getTechnologyScore()));

		this.mTvAddr.setText(mVendor.getAddress());
		this.mTvMobileNum.setText(mVendor.getMobile());
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

		unregisterMessageReceiver();
	}

	private void connectVendor() {
		if (null == this.mVendor)
			return;

		if (SelfMgr.getInstance().isFavoriteVendor(this.mVendor.getId())) {
			Intent intent = new Intent(this, ChatActivity.class);
			intent.putExtra(ChatActivity.KEY_FELLOWID, this.mVendor.getId());
			intent.putExtra(ChatActivity.KEY_CHATSTYLE, ChatActivity.CHAT_STYLE_2ONE);
			this.startActivity(intent);
		} else {
			FollowshipMessage msg = new FollowshipMessage();
			msg.setSource(SelfMgr.getInstance().getId());
			msg.setTarget(this.mVendor.getId());
			msg.setSentTime(new Date().getTime());
			msg.setFlag(MessageFlag.SELF);
			msg.setId("followship");

			IMessageCourier courier = new FollowshipMessageCourier(this.getApplicationContext());
			courier.dispatch(msg);
		}
	}

	class FollowshipResultReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();

			if (Constants.ACTION_FOLLOWSHIP_SUCCESS.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_followsuccess),
						Toast.LENGTH_LONG).show();
			} else if (Constants.ACTION_FOLLOWSHIP_FAILED.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_followfailed),
						Toast.LENGTH_LONG).show();
			}
		}

	}
}
