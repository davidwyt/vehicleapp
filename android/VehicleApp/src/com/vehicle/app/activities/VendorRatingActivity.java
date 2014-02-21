package com.vehicle.app.activities;

import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.CommentMessage;
import com.vehicle.app.msg.worker.IMessageCourier;
import com.vehicle.app.msg.worker.VendorCommentMessageCourier;
import com.vehicle.app.utils.Constants;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class VendorRatingActivity extends Activity {

	private Vendor mVendor;

	private RadioGroup mRadioGroupProject;
	private RatingBar mRatingPrice;
	private RatingBar mRatingTech;
	private RatingBar mRatingEfficiency;
	private RatingBar mRatingRecption;
	private RatingBar mRatingEnvironment;

	private TextView mVendorName;

	private EditText mComment;
	private Button mBtnOK;

	private BroadcastReceiver mReceiver;

	public static final String KEY_VENDORRATING_VENDORID = "com.vehicle.app.vendorrating.vendor";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_vendorrating);

		initView();
	}

	private void initView() {

		this.mVendorName = (TextView) this.findViewById(R.id.vendorrating_name);

		this.mRadioGroupProject = (RadioGroup) this.findViewById(R.id.vendorrating_project_radiogroup);

		this.mRatingPrice = (RatingBar) this.findViewById(R.id.ratingbar_price);
		this.mRatingTech = (RatingBar) this.findViewById(R.id.ratingbar_technology);
		this.mRatingRecption = (RatingBar) this.findViewById(R.id.ratingbar_reception);
		this.mRatingEfficiency = (RatingBar) this.findViewById(R.id.ratingbar_efficiency);
		this.mRatingEnvironment = (RatingBar) this.findViewById(R.id.ratingbar_environment);

		this.mComment = (EditText) this.findViewById(R.id.vendorrating_et_comment);
		this.mBtnOK = (Button) this.findViewById(R.id.vendorrating_submit);

		this.mBtnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				submitCommit();
			}
		});
	}

	private void submitCommit() {
		if (null == this.mVendor) {
			return;
		}

		int checkId = mRadioGroupProject.getCheckedRadioButtonId();
		int mainProjectId = 0;
		switch (checkId) {
		case R.id.rd_vehiclerepair:
			mainProjectId = 0;
			break;
		case R.id.rd_vehiclebeauty:
			mainProjectId = 1;
			break;
		case R.id.rd_vehiclemaintain:
			mainProjectId = 2;
			break;
		case R.id.rd_vehiclewash:
			mainProjectId = 3;
			break;
		}

		double price = this.mRatingPrice.getRating();
		double tech = this.mRatingTech.getRating();
		double env = this.mRatingEnvironment.getRating();
		double eff = this.mRatingEfficiency.getRating();
		double rec = this.mRatingRecption.getRating();

		String comment = this.mComment.getText().toString();

		CommentMessage msg = new CommentMessage();
		msg.setSource(SelfMgr.getInstance().getId());
		msg.setTarget(this.mVendor.getId());
		msg.setMainProjectId(mainProjectId);
		msg.setComment(comment);
		msg.setEfficiencyScore(eff);
		msg.setPriceScore(price);
		msg.setEnvironmentScore(env);
		msg.setReceptionScore(rec);
		msg.setTechnologyScore(tech);

		IMessageCourier courier = new VendorCommentMessageCourier(this.getApplicationContext());
		courier.dispatch(msg);
	}

	private void initData() {
		Bundle bundle = this.getIntent().getExtras();

		if (null != bundle) {
			this.mVendor = (Vendor) bundle.getSerializable(KEY_VENDORRATING_VENDORID);

			if (null != this.mVendor) {
				this.mVendorName.setText(mVendor.getName());
			}
		}
	}

	private void registerMessageReceiver() {

		mReceiver = new CommentResultReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(Constants.ACTION_VENDORCOMMENT_SUCCESS);
		filter.addAction(Constants.ACTION_VENDORCOMMENT_FAILED);

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

	class CommentResultReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();

			if (Constants.ACTION_VENDORCOMMENT_SUCCESS.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_commentsuccess),
						Toast.LENGTH_LONG).show();
			} else if (Constants.ACTION_VENDORCOMMENT_FAILED.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_commentfailed),
						Toast.LENGTH_LONG).show();
			}
		}

	}
}
