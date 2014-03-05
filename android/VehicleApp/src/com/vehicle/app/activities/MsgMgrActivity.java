package com.vehicle.app.activities;

import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.db.DBManager;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.mgrs.TopMsgerMgr;
import com.vehicle.app.utils.ImageUtil;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MsgMgrActivity extends Activity implements OnClickListener {

	private Button mBtnBack;

	private ImageView mIVHead;
	private TextView mTVName;

	private Object mFellow;
	private String mFellowId;

	public final static String KEY_FELLOWID = "com.vehicle.app.msgmgr.fellowid";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_msgmgr);

		initView();
	}

	private void initView() {
		this.mBtnBack = (Button) this.findViewById(R.id.msgmgr_goback);
		this.mBtnBack.setOnClickListener(this);

		this.findViewById(R.id.msgmgr_clearrow).setOnClickListener(this);
		this.findViewById(R.id.msgmgr_uprow).setOnClickListener(this);

		this.mIVHead = (ImageView) this.findViewById(R.id.msgmgr_head);
		this.mTVName = (TextView) this.findViewById(R.id.msgmgr_fellowname);

		this.findViewById(R.id.msgmgr_favmsg).setOnClickListener(this);
		this.findViewById(R.id.msgmgr_clearmsg).setOnClickListener(this);
	}

	private void initData() {
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();

		String name = "";
		String url = "";

		if (null != bundle) {
			mFellowId = bundle.getString(KEY_FELLOWID);
			if (SelfMgr.getInstance().isDriver()) {
				this.mFellow = SelfMgr.getInstance().getFavVendor(mFellowId);
				if (null != this.mFellow) {
					name = ((Vendor) mFellow).getName();
					url = ((Vendor) mFellow).getAvatar();
				}
			} else {
				this.mFellow = SelfMgr.getInstance().getVendorFellow(mFellowId);
				if (null != this.mFellow) {
					name = ((Driver) mFellow).getAlias();
					url = ((Driver) mFellow).getAvatar();
				}
			}
		}

		this.mTVName.setText(name);

		ImageUtil.RenderImageView(url, mIVHead, -1, -1);
		refreshOpeTip();
	}

	private void refreshOpeTip() {
		TextView tvOper = (TextView) this.findViewById(R.id.msgmgr_topmsgope);
		if (TopMsgerMgr.getInstance().isTop(mFellowId)) {
			tvOper.setText(this.getResources().getString(R.string.tip_canceltop));
		} else {
			tvOper.setText(this.getResources().getString(R.string.tip_addtotop));
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

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (R.id.msgmgr_goback == view.getId()) {
			this.onBackPressed();
		} else if (R.id.msgmgr_uprow == view.getId() || R.id.msgmgr_favmsg == view.getId()) {
			if (TopMsgerMgr.getInstance().isTop(mFellowId)) {
				try {
					TopMsgerMgr.getInstance().removeTopMsg(SelfMgr.getInstance().getId(), mFellowId);
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_removetopsuccess),
							Toast.LENGTH_LONG).show();
					refreshOpeTip();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					TopMsgerMgr.getInstance().addTopMsg(SelfMgr.getInstance().getId(), mFellowId);
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_addtopsuccess),
							Toast.LENGTH_LONG).show();
					refreshOpeTip();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (R.id.msgmgr_clearrow == view.getId() || R.id.msgmgr_clearmsg == view.getId()) {
			attemptDeleteMsg();
		} else {
			System.err.println("invalid id of clicked button in msgmgr form");
		}
	}

	private void attemptDeleteMsg() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle(this.getResources().getString(R.string.alert_zh));

		alertDialogBuilder
				.setMessage(getResources().getString(R.string.clearmsg_confirmtxt))
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.positive_zh),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								clearMessage();
							}
						})
				.setNegativeButton(getResources().getString(R.string.negative_zh),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}

	private void clearMessage() {
		try {
			DBManager dbMgr = new DBManager(this.getApplicationContext());
			dbMgr.deleteAllMessages(SelfMgr.getInstance().getId(), mFellowId);
			TopMsgerMgr.getInstance().removeTopMsg(SelfMgr.getInstance().getId(), mFellowId);

			Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_deletegroupmsgsuccess),
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
