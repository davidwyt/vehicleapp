package com.vehicle.app.activities;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MsgMgrActivity extends Activity implements OnClickListener {

	private Button mBtnBack;
	private Button mBtnFavMsg;
	private Button mBtnClearMsg;

	private ImageView mIVHead;
	private TextView mTVName;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_msgmgr);

		initView();
		initData();
	}

	private void initView() {
		this.mBtnBack = (Button) this.findViewById(R.id.msgmgr_goback);
		this.mBtnBack.setOnClickListener(this);

		this.mBtnFavMsg = (Button) this.findViewById(R.id.msgmgr_favmsg);
		this.mBtnFavMsg.setOnClickListener(this);

		this.mBtnClearMsg = (Button) this.findViewById(R.id.msgmgr_clearmsg);
		this.mBtnClearMsg.setOnClickListener(this);

		this.mIVHead = (ImageView) this.findViewById(R.id.msgmgr_head);
		this.mTVName = (TextView) this.findViewById(R.id.msgmgr_fellowname);
	}

	private void initData() {

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (R.id.msgmgr_goback == view.getId()) {
			this.onBackPressed();
		} else if (R.id.msgmgr_favmsg == view.getId()) {

		} else if (R.id.msgmgr_clearmsg == view.getId()) {

		} else {
			System.err.println("invalid id of clicked button in msgmgr form");
		}
	}
}
