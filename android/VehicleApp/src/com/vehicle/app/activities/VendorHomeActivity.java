package com.vehicle.app.activities;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import com.vehicle.app.adapter.VendorImagePageAdapter;
import com.vehicle.app.adapter.VendorDetailFragmentAdapter;
import com.vehicle.app.bean.VendorDetail;
import com.vehicle.app.bean.VendorImage;
import com.vehicle.app.mgrs.BitmapCache;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.FollowshipMessage;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.bean.TextMessage;
import com.vehicle.app.msg.worker.FollowshipMessageCourier;
import com.vehicle.app.msg.worker.IMessageCourier;
import com.vehicle.app.msg.worker.TextMessageCourier;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.HttpUtil;
import com.vehicle.app.web.bean.VendorImgViewResult;
import com.vehicle.app.web.bean.VendorSpecViewResult;
import com.vehicle.app.web.bean.WebCallBaseResult;
import com.vehicle.sdk.client.VehicleWebClient;

import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;
import com.viewpagerindicator.UnderlinePageIndicator;

import cn.edu.sjtu.vehicleapp.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VendorHomeActivity extends FragmentActivity implements OnClickListener {

	VendorDetailFragmentAdapter mDetailAdapter;
	ViewPager mDetailPager;
	TitlePageIndicator mDetailIndicator;

	VendorImagePageAdapter mImgAdapter;
	ViewPager mImgPager;
	UnderlinePageIndicator mImgIndicator;

	private Button mBtnShake;
	private Button mBtnNext;
	private Button mBtnYelp;
	private Button mBtnEcc;

	private Button mBtnBak;
	private Button mBtnFollow;

	private TextView mTvName;

	public static final String KEY_VENDORID = "com.vehicle.app.key.vendorhomeactivity.vendor.id";
	public static final String KEY_ISNEARBY = "com.vehicle.app.key.vendorhomeactivity.vendor.isnearby";
	public static final String KEY_NEARBYVENDORS = "com.vehicle.app.key.vendorhomeactivity.nearbyvendors";

	private boolean isNearby;

	private ArrayList<String> mNearbyVendors;
	private int mCurIndex;
	private VendorDetail mCurVendorDetail;

	private View mVendorFormView;
	private View mVendorStatusView;
	private TextView mVendorStatusMessageView;

	private ViewFellowTask mViewFellowTask = null;

	private BroadcastReceiver mReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_vendorhome);

		mDetailAdapter = new VendorDetailFragmentAdapter(getSupportFragmentManager());

		mDetailPager = (ViewPager) findViewById(R.id.vendorhome_pager);
		mDetailPager.setAdapter(mDetailAdapter);

		mDetailIndicator = (TitlePageIndicator) findViewById(R.id.vendorhome_indicator);
		mDetailIndicator.setViewPager(mDetailPager);
		mDetailIndicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
		mDetailIndicator.setSelectedBold(true);

		mImgAdapter = new VendorImagePageAdapter(getSupportFragmentManager());
		mImgPager = (ViewPager) this.findViewById(R.id.vendorhome_imagepager);
		mImgPager.setAdapter(mImgAdapter);

		mImgIndicator = (UnderlinePageIndicator) this.findViewById(R.id.vendorhome_imageindicator);
		mImgIndicator.setViewPager(mImgPager);

		mBtnShake = (Button) this.findViewById(R.id.vendorbar_shake);
		this.mBtnShake.setOnClickListener(this);

		mBtnYelp = (Button) this.findViewById(R.id.vendorbar_yelp);
		this.mBtnYelp.setOnClickListener(this);

		mBtnEcc = (Button) this.findViewById(R.id.vendorbar_ecc);
		this.mBtnEcc.setOnClickListener(this);

		mBtnNext = (Button) this.findViewById(R.id.vendorbar_next);
		this.mBtnNext.setOnClickListener(this);

		this.mBtnBak = (Button) this.findViewById(R.id.vendortitle_btn_back);
		this.mBtnBak.setOnClickListener(this);

		this.mBtnFollow = (Button) this.findViewById(R.id.vendortitle_btn_follow);
		this.mBtnFollow.setOnClickListener(this);

		this.mTvName = (TextView) this.findViewById(R.id.vendortitle_tv_name);

		this.mVendorFormView = this.findViewById(R.id.vendorhome_form);
		this.mVendorStatusView = this.findViewById(R.id.vendorhome_status);
		this.mVendorStatusMessageView = (TextView) this.findViewById(R.id.vendorhome_status_message);
	}

	@Override
	protected void onStart() {
		super.onStart();

		initData();

		this.registerMessageReceiver();
	}

	private void initData() {
		Bundle bundle = this.getIntent().getExtras();
		if (null != bundle) {
			isNearby = bundle.getBoolean(KEY_ISNEARBY);
			String vendorId = bundle.getString(KEY_VENDORID);
			VendorDetail vendor = null;
			updateView();

			if (isNearby) {
				this.mNearbyVendors = bundle.getStringArrayList(KEY_NEARBYVENDORS);

				if (null != this.mNearbyVendors) {
					int index = -1;
					for (int i = 0; i < this.mNearbyVendors.size(); i++) {
						if (vendorId.equals(this.mNearbyVendors.get(i))) {
							index = i;
							break;
						}
					}

					Assert.assertEquals(index >= 0, true);

					this.mCurIndex = index;
				}
				vendor = SelfMgr.getInstance().getNearbyVendorDetail(vendorId);
			} else {
				vendor = SelfMgr.getInstance().getFavVendorDetail(vendorId);
			}

			setVendorDetail(vendor);
		}
	}

	private void updateView() {
		if (this.isNearby) {
			this.mBtnFollow.setBackgroundResource(R.drawable.selector_btn_follow);
			this.mBtnShake.setVisibility(View.VISIBLE);
			this.mBtnNext.setVisibility(View.VISIBLE);

			this.mBtnYelp.setBackgroundResource(R.drawable.selector_button_vendorbar_yelp);
			this.mBtnEcc.setBackgroundResource(R.drawable.selector_button_vendorbar_ecc);

		} else {
			this.mBtnFollow.setBackgroundResource(R.drawable.selector_btn_msg);
			this.mBtnShake.setVisibility(View.GONE);
			this.mBtnNext.setVisibility(View.GONE);

			this.mBtnYelp.setBackgroundResource(R.drawable.selector_button_vendorbar_longyelp);
			this.mBtnEcc.setBackgroundResource(R.drawable.selector_button_vendorbar_longecc);

		}
	}

	private void setVendorDetail(VendorDetail detail) {

		this.mCurVendorDetail = detail;

		this.mTvName.setText(detail.getVendor().getName());

		this.mDetailAdapter.setVendorDetail(detail);
		this.mDetailAdapter.notifyDataSetChanged();

		this.mImgAdapter.setImgs(detail.getImgs());
		this.mImgAdapter.notifyDataSetChanged();
	}

	private void registerMessageReceiver() {

		mReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(Constants.ACTION_FOLLOWSHIP_FAILED);
		filter.addAction(Constants.ACTION_FOLLOWSHIP_SUCCESS);
		filter.addAction(Constants.ACTION_TEXTMESSAGE_SENTFAILED);
		filter.addAction(Constants.ACTION_TEXTMESSAGE_SENTOK);
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
	protected void onStop() {
		super.onStop();
		this.unregisterMessageReceiver();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		switch (id) {
		case R.id.vendorbar_shake:
			shake();
			break;
		case R.id.vendorbar_yelp:
			yelp();
			break;
		case R.id.vendorbar_ecc:
			break;
		case R.id.vendorbar_next:
			gotoNext();
			break;
		case R.id.vendortitle_btn_back:
			gobak();
			break;
		case R.id.vendortitle_btn_follow:
			follow();
			break;
		}
	}

	private void yelp() {
		Intent intent = new Intent(this, VendorRatingActivity.class);

		intent.putExtra(VendorRatingActivity.KEY_VENDORRATING_VENDOR, this.mCurVendorDetail.getVendor());
		this.startActivity(intent);
	}

	private void shake() {
		Assert.assertEquals(true, isNearby);
		if (!isNearby) {
			return;
		}

		TextMessage entity = new TextMessage();
		entity.setSource(SelfMgr.getInstance().getId());
		entity.setTarget(this.mNearbyVendors.get(mCurIndex));
		entity.setContent(this.getResources().getString(R.string.tip_vendorshaketext));
		entity.setFlag(MessageFlag.SELF);
		entity.setMessageType(IMessageItem.MESSAGE_TYPE_TEXT);
		IMessageCourier msgCourier = new TextMessageCourier(this.getApplicationContext(), false);
		msgCourier.dispatch(entity);
	}

	private void gotoNext() {

		Assert.assertEquals(true, isNearby);
		if (!this.isNearby) {
			return;
		}

		mCurIndex++;
		if (mCurIndex >= this.mNearbyVendors.size()) {
			Toast.makeText(VendorHomeActivity.this, getResources().getString(R.string.tip_nonextvendor),
					Toast.LENGTH_LONG).show();
			return;
		}

		String nextId = this.mNearbyVendors.get(mCurIndex);

		boolean isExist = SelfMgr.getInstance().isNearbyVendorDetailExist(nextId);

		if (isExist) {
			setVendorDetail(SelfMgr.getInstance().getNearbyVendorDetail(nextId));
			return;
		}

		if (null != this.mViewFellowTask)
			return;

		mVendorStatusMessageView.setText(R.string.nearbyvendorstatustext);

		showProgress(true);
		mViewFellowTask = new ViewFellowTask();
		mViewFellowTask.execute(nextId);
	}

	private void follow() {

		if (this.isNearby) {
			String curId = this.mNearbyVendors.get(mCurIndex);

			if (SelfMgr.getInstance().isFavoriteVendor(curId)) {
				Toast.makeText(VendorHomeActivity.this, getResources().getString(R.string.tip_favvendorexist),
						Toast.LENGTH_LONG).show();
			} else {
				FollowshipMessage msg = new FollowshipMessage();
				msg.setSource(SelfMgr.getInstance().getId());
				msg.setTarget(this.mNearbyVendors.get(mCurIndex));
				msg.setSentTime(new Date().getTime());
				msg.setFlag(MessageFlag.SELF);
				msg.setId("followship");

				IMessageCourier courier = new FollowshipMessageCourier(this.getApplicationContext());
				courier.dispatch(msg);
			}
		} else {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), ChatActivity.class);

			intent.putExtra(ChatActivity.KEY_FELLOWID, this.mCurVendorDetail.getVendor().getId());
			intent.putExtra(ChatActivity.KEY_CHATSTYLE, ChatActivity.CHAT_STYLE_2ONE);

			this.startActivity(intent);
		}
	}

	private void gobak() {
		this.onBackPressed();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mVendorStatusView.setVisibility(View.VISIBLE);
			mVendorStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mVendorStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

			mVendorFormView.setVisibility(View.VISIBLE);
			mVendorFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mVendorFormView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mVendorStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mVendorStatusView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	public class ViewFellowTask extends AsyncTask<String, Void, WebCallBaseResult> {
		private String fellowId;

		@Override
		protected WebCallBaseResult doInBackground(String... params) {
			// TODO: attempt authentication against a network service.
			fellowId = params[0];
			WebCallBaseResult result = null;
			try {
				VehicleWebClient webClient = new VehicleWebClient();
				result = webClient.VendorSpecView(fellowId);

				if (null != result && result.isSuccess()) {
					VendorSpecViewResult vendorView = (VendorSpecViewResult) result;
					VendorDetail vendor = vendorView.getInfoBean();

					SelfMgr.getInstance().updateNearbyVendorDetail(vendor);
					/**
					 * List<Comment> comments = vendor.getReviews(); if (null !=
					 * comments && comments.size() > 0) { for (Comment comment :
					 * comments) { String imgs = comment.getImgNamesM();
					 * String[] names = imgs.split(Constants.IMGNAME_DIVIDER);
					 * 
					 * for (String name : names) { if (null != name &&
					 * name.length() > 0) { String imgUrl =
					 * Constants.getMiddleVendorImg(name); InputStream input =
					 * HttpUtil.DownloadFile(imgUrl); Bitmap bitmap =
					 * BitmapFactory.decodeStream(input);
					 * BitmapCache.getInstance().put(imgUrl, bitmap); } } } }
					 */
				} else {
					return null;
				}

				result = webClient.VendorImgView(fellowId);
				if (null != result && result.isSuccess()) {
					VendorDetail vendor = SelfMgr.getInstance().getNearbyVendorDetail(fellowId);
					List<VendorImage> imgs = ((VendorImgViewResult) result).getInfoBean();
					if (null != imgs) {
						for (VendorImage img : imgs) {
							String imgUrl = img.getSrc();
							InputStream input = HttpUtil.DownloadFile(imgUrl);
							Bitmap bitmap = BitmapFactory.decodeStream(input);
							BitmapCache.getInstance().put(imgUrl, bitmap);
						}
					}

					vendor.setImgs(imgs);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(final WebCallBaseResult result) {
			mViewFellowTask = null;
			showProgress(false);

			if (null != result && result.isSuccess()) {
				setVendorDetail(SelfMgr.getInstance().getNearbyVendorDetail(fellowId));
			} else {
				Toast.makeText(VendorHomeActivity.this, getResources().getString(R.string.tip_viewnearbyvendorfailed),
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mViewFellowTask = null;
			showProgress(false);
		}
	}

	class MessageReceiver extends BroadcastReceiver {

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
			} else if (Constants.ACTION_TEXTMESSAGE_SENTOK.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_vendortextsent),
						Toast.LENGTH_LONG).show();

			} else if (Constants.ACTION_TEXTMESSAGE_SENTFAILED.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_textmsgfailed),
						Toast.LENGTH_LONG).show();
			}
		}
	}

}
