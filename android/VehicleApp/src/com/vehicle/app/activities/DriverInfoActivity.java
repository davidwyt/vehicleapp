package com.vehicle.app.activities;

import com.vehicle.app.bean.Driver;
import com.vehicle.app.mgrs.BitmapCache;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.worker.FollowshipInvMessageCourier;
import com.vehicle.app.msg.worker.IMessageCourier;
import com.vehicle.app.msg.worker.ImageViewBitmapLoader;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DriverInfoActivity extends Activity {

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

	public static final String KEY_NEARBYDRIVERID = "com.vehicle.app.key.nearbydriver.id";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_driverinfo);
		initView();
	}

	private void initView() {
		this.mBtnBack = (Button) this.findViewById(R.id.driver_btn_back);
		this.mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DriverInfoActivity.this.onBackPressed();
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
		
		if(null == this.mDriver)
			return;
		
		FollowshipInvitationMessage msg = new FollowshipInvitationMessage();
		msg.setFlag(MessageFlag.READ);
		msg.setId("");
		msg.setSource(SelfMgr.getInstance().getId());
		msg.setTarget(this.mDriver.getId());
		
		IMessageCourier msgCourier = new FollowshipInvMessageCourier(this.getApplicationContext());
		msgCourier.dispatch(msg);
	}

	private void initData() {
		Bundle bundle = this.getIntent().getExtras();
		if (null == bundle) {
			return;
		}

		String driverId = bundle.getString(KEY_NEARBYDRIVERID);

		this.mDriver = SelfMgr.getInstance().getNearbyDriver(driverId);

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
		Bitmap bitmap = BitmapCache.getInstance().get(url);

		if (null != bitmap) {
			mIvHead.setImageBitmap(bitmap);
		} else {
			mIvHead.setTag(R.id.TAGKEY_BITMAP_URL, url);
			ImageViewBitmapLoader loader = new ImageViewBitmapLoader(mIvHead);
			loader.load();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		initData();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
