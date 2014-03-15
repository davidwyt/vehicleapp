package com.vehicle.app.activities;

import java.util.List;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.adapter.CommentsViewAdapter;
import com.vehicle.app.bean.Comment;
import com.vehicle.app.mgrs.SelfMgr;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MyCommentsActivity extends TemplateActivity {
	private BaseAdapter mAdapter;

	private ListView mLVComments;

	private Button onBtnBak;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_driverselfcomments);

		initView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
		try {
			initData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initData() {
		if (SelfMgr.getInstance().isDriver()) {
			List<Comment> comments = SelfMgr.getInstance().getSelfDriver().getComments();
			this.mAdapter = new CommentsViewAdapter(LayoutInflater.from(getApplicationContext()), comments, true);
			if (null == comments || comments.size() <= 0) {
				Toast.makeText(getApplicationContext(), this.getString(R.string.tip_nocomments), Toast.LENGTH_LONG)
						.show();
				return;
			}
		} else {
			List<Comment> comments = SelfMgr.getInstance().getSelfVendorDetail().getReviews();
			if (null == comments || comments.size() <= 0) {
				Toast.makeText(getApplicationContext(), this.getString(R.string.tip_nocomments), Toast.LENGTH_LONG)
						.show();
				return;
			}

			this.mAdapter = new CommentsViewAdapter(LayoutInflater.from(getApplicationContext()), comments, false);
		}

		mLVComments.setAdapter(mAdapter);

		this.mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
