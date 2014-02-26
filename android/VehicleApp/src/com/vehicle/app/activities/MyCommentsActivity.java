package com.vehicle.app.activities;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.adapter.CommentsViewAdapter;
import com.vehicle.app.mgrs.SelfMgr;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MyCommentsActivity extends Activity {
	private BaseAdapter mAdapter;

	private ListView mLVComments;

	private Button onBtnBak;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_driverselfcomments);

		initView();
	}

	private void initView() {
		this.mLVComments = (ListView) this.findViewById(R.id.driver_comments);
		this.onBtnBak = (Button) this.findViewById(R.id.driver_btn_back);
		this.onBtnBak.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
				finish();
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
		initData();
	}

	private void initData() {
		if (SelfMgr.getInstance().isDriver()) {
			this.mAdapter = new CommentsViewAdapter(LayoutInflater.from(getApplicationContext()), SelfMgr.getInstance()
					.getSelfDriver().getComments(), true);
		} else {
			this.mAdapter = new CommentsViewAdapter(LayoutInflater.from(getApplicationContext()), SelfMgr.getInstance()
					.getSelfVendor().getComments(), false);
		}

		mLVComments.setAdapter(mAdapter);

		this.mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
