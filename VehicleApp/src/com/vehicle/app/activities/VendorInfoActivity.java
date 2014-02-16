package com.vehicle.app.activities;

import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.web.bean.AddFavVendorResult;
import com.vehicle.sdk.client.VehicleWebClient;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

	public static final String KEY_NEARBYVENDORID = "com.vehicle.app.key.nearbyvendor.id";

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

	private void initData() {
		Bundle bundle = this.getIntent().getExtras();
		if (null == bundle) {
			return;
		}
		String vendorId = bundle.getString(KEY_NEARBYVENDORID);

		mVendor = SelfMgr.getInstance().getNearbyVendor(vendorId);

		if (null == mVendor) {
			return;
		}

		this.mTvTitleName.setText(mVendor.getName());

		this.mTvName.setText(mVendor.getName());
		this.mTvBusinessTime.setText(mVendor.getAnswerTime());
		this.mIvhead.setImageBitmap(mVendor.getIcon());

		this.mTvScore.setText(Float.toString(mVendor.getScore()));
		this.mTvEfficiency.setText(Float.toString(mVendor.getEfficiencyScore()));
		this.mTvEnvironment.setText(Float.toString(mVendor.getEnvironmentScore()));
		this.mTvPrice.setText(Float.toString(mVendor.getPriceScore()));
		this.mTvReception.setText(Float.toString(mVendor.getReceptionScore()));
		this.mTvTechnology.setText(Float.toString(mVendor.getTechnologyScore()));

		this.mTvAddr.setText(mVendor.getAddress());
		this.mTvMobileNum.setText(mVendor.getMobile());

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

	private void connectVendor() {
		if(null == this.mVendor)
			return;
		
		AsyncTask<Void, Void, AddFavVendorResult> asyncTask = new AsyncTask<Void, Void, AddFavVendorResult>() {

			@Override
			protected AddFavVendorResult doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				AddFavVendorResult result = null;
				try {
					VehicleWebClient client = new VehicleWebClient();
					result = client.AddFavVendor(SelfMgr.getInstance().getId(), mVendor.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return result;
			}

			@Override
			protected void onPostExecute(AddFavVendorResult result) {
				if (null == result) {
					System.out.println("send add favorite request failed");
					return;
				}

				if (result.isSuccess()) {
					System.out.println("add favorite success");
				} else {
					System.out.println("add favorite failed:" + result.getMessage());
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
