package com.vehicle.app.activities;

import java.util.List;

import junit.framework.Assert;

import com.vehicle.app.bean.Car;
import com.vehicle.app.bean.Driver;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.FollowshipInvitationMessage;
import com.vehicle.app.msg.bean.MessageFlag;
import com.vehicle.app.msg.worker.FollowshipInvMessageCourier;
import com.vehicle.app.msg.worker.IMessageCourier;
import com.vehicle.app.utils.ActivityUtil;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.ImageUtil;
import com.vehicle.app.web.bean.CarListViewResult;
import com.vehicle.app.web.bean.WebCallBaseResult;
import com.vehicle.sdk.client.VehicleWebClient;

import cn.edu.sjtu.vehicleapp.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class DriverHomeActivity extends TemplateActivity implements OnClickListener {

	private Button mBtnBack;
	private ImageView mIvHead;
	private TextView mTvName;
	private TextView mTvAge;
	private TextView mTvSex;
	private TextView mTvPerInfo;

	private TableLayout mCarTable;

	private Button mBtnMsg;
	private Button mBtnShake;
	private Button mBtnNext;
	private TextView mTitleName;
	// private ImageView mTitleIV;

	private View mBottomBar;

	private int mPerspective;
	private List<String> mNearbyDrivers;
	private int mCurIndex;

	private String mDriverId;

	private View mDriverFormView;
	private View mDriverStatusView;
	private TextView mDriverStatusMessageView;

	private RefreshDriverInfoTask mRefreshTask;

	private BroadcastReceiver mReceiver;

	public static final int PERSPECTIVE_SELF = 1;
	public static final int PERSPECTIVE_NEARBY = 2;
	public static final int PERSPECTIVE_FELLOW = 3;

	public static final String KEY_PERSPECTIVE = "com.vehicle.app.driverinfoactivity.pers";

	public static final String KEY_DRIVERINFOID = "com.vehicle.app.driverinfoactivity.id";
	public static final String KEY_DRIVERINFO_NEARBYS = "com.vehicle.app.driverinfoactivity.nearbys";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_driverhome);
		initView();
	}

	private void initView() {
		this.mBtnBack = (Button) this.findViewById(R.id.driverinfo_goback);
		this.mBtnBack.setOnClickListener(this);

		this.mIvHead = (ImageView) this.findViewById(R.id.driverinfo_icon);
		this.mTvName = (TextView) this.findViewById(R.id.driverinfo_addr);
		this.mTvAge = (TextView) this.findViewById(R.id.driverinfo_age);
		this.mTvSex = (TextView) this.findViewById(R.id.driverinfo_sex);
		this.mTvPerInfo = (TextView) this.findViewById(R.id.driver_driverinfo);
		this.mCarTable = (TableLayout) this.findViewById(R.id.table_cars);

		this.mBtnMsg = (Button) this.findViewById(R.id.driverinfo_title_btn_msg);
		this.mBtnMsg.setOnClickListener(this);

		this.mBtnShake = (Button) this.findViewById(R.id.driverinfo_shake);
		this.mBtnShake.setOnClickListener(this);

		this.mBtnNext = (Button) this.findViewById(R.id.driverinfo_next);
		this.mBtnNext.setOnClickListener(this);

		this.mTitleName = (TextView) this.findViewById(R.id.driverinfo_title_nametv);
		// this.mTitleIV = (ImageView)
		// this.findViewById(R.id.driverinfo_title_nameiv);
		this.mBottomBar = this.findViewById(R.id.driverinfo_bottombar);

		mDriverFormView = this.findViewById(R.id.driverinfo_form);
		mDriverStatusView = this.findViewById(R.id.driverinfo_status);
		mDriverStatusMessageView = (TextView) this.findViewById(R.id.driverinfo_status_message);
	}

	private void updateView(int pers) {

		this.mBottomBar.setVisibility(PERSPECTIVE_NEARBY == pers ? View.VISIBLE : View.GONE);
		this.mBtnMsg.setVisibility(PERSPECTIVE_FELLOW == pers ? View.VISIBLE : View.GONE);
	}

	private void initData() {

		Bundle bundle = this.getIntent().getExtras();

		if (null == bundle)
			return;

		this.mPerspective = bundle.getInt(KEY_PERSPECTIVE);
		updateView(mPerspective);

		if (PERSPECTIVE_SELF == this.mPerspective) {
			setViewData(SelfMgr.getInstance().getSelfDriver());
		} else if (PERSPECTIVE_FELLOW == this.mPerspective) {
			mDriverId = bundle.getString(KEY_DRIVERINFOID);
			setViewData(SelfMgr.getInstance().getVendorFellow(mDriverId));
		} else if (PERSPECTIVE_NEARBY == this.mPerspective) {
			mDriverId = bundle.getString(KEY_DRIVERINFOID);
			this.mNearbyDrivers = bundle.getStringArrayList(KEY_DRIVERINFO_NEARBYS);

			this.mCurIndex = -1;
			for (int i = 0; i < this.mNearbyDrivers.size(); i++) {
				if (mDriverId.equals(this.mNearbyDrivers.get(i))) {
					this.mCurIndex = i;
					break;
				}
			}

			Assert.assertEquals(true, this.mCurIndex >= 0);

			setViewData(SelfMgr.getInstance().getNearbyDriver(mDriverId));
		}
	}

	private void setViewData(Driver driver) {

		if (null == driver) {
			return;
		}

		ImageUtil.RenderImageView(driver.getAvatar(), mIvHead, -1, -1);
		this.mTitleName.setText(driver.getAlias());
		this.mTvName.setText(this.getString(R.string.zh_addr, null == driver.getProvince() ? "" : driver.getProvince(),
				null == driver.getCity() ? "" : driver.getCity()));

		this.mTvAge.setText(this.getString(R.string.zh_brithdayformat,
				null == driver.getBirthday() ? "" : driver.getBirthday()));

		this.mTvSex.setText(this.getString(R.string.zh_sex, null == driver.getSex() ? "" : driver.getSex()));

		this.mTvPerInfo.setText(driver.getIntroduction());
		this.mCarTable.removeAllViews();

		List<Car> cars = driver.getCars();

		if (null != cars) {
			for (int i = 0; i < cars.size(); i++) {

				Car car = cars.get(i);

				TableRow row = new TableRow(this);

				TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
						TableRow.LayoutParams.WRAP_CONTENT);
				row.setLayoutParams(lp);

				TableRow.LayoutParams brandLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						0.8f);
				TextView brand = new TextView(this);
				brand.setLayoutParams(brandLayout);
				brand.setText(car.getBrandName());
				brand.setGravity(Gravity.CENTER);
				row.addView(brand);

				TableRow.LayoutParams dateLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						1.2f);
				TextView date = new TextView(this);
				date.setLayoutParams(dateLayout);
				date.setText(car.getBuyDate());
				date.setGravity(Gravity.CENTER);
				row.addView(date);

				TextView type = new TextView(this);
				TableRow.LayoutParams typeLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						1.2f);
				type.setLayoutParams(typeLayout);
				type.setText(car.getTypeName());
				type.setGravity(Gravity.CENTER);
				row.addView(type);

				TextView plate = new TextView(this);
				TableRow.LayoutParams plateLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						1.0f);
				plate.setLayoutParams(plateLayout);
				plate.setText(car.getPlate());
				plate.setGravity(Gravity.CENTER);
				row.addView(plate);

				this.mCarTable.addView(row, 2 * i);

				TableRow.LayoutParams dividerLayout = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
				dividerLayout.topMargin = 2;

				View divider = new View(this);
				divider.setLayoutParams(dividerLayout);
				divider.setBackgroundColor(getResources().getColor(R.color.azure));

				this.mCarTable.addView(divider, 2 * i + 1);
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		mRefreshTask = null;
		try {
			initData();
		} catch (Exception e) {
			e.printStackTrace();
		}

		registerMessageReceiver();
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterMessageReceiver();
	}

	private void bak() {
		this.onBackPressed();
		this.finish();
	}

	private void shake() {
		Assert.assertEquals(PERSPECTIVE_NEARBY, mPerspective);

		if (this.mPerspective != PERSPECTIVE_NEARBY)
			return;

		if (!SelfMgr.getInstance().isLogin()) {
			Intent intent = new Intent(this, LoginActivity.class);
			this.startActivity(intent);
			return;
		}

		String curId = this.mNearbyDrivers.get(mCurIndex);
		if (SelfMgr.getInstance().isVendorFellow(curId)) {
			Toast.makeText(this, getResources().getString(R.string.tip_vendorfellowexits), Toast.LENGTH_LONG).show();
			return;
		}

		FollowshipInvitationMessage msg = new FollowshipInvitationMessage();
		msg.setFlag(MessageFlag.SELF);
		msg.setId("");
		msg.setSource(SelfMgr.getInstance().getId());
		msg.setTarget(curId);

		IMessageCourier msgCourier = new FollowshipInvMessageCourier(this.getApplicationContext());
		msgCourier.dispatch(msg);
	}

	private void next() {
		Assert.assertEquals(PERSPECTIVE_NEARBY, mPerspective);

		if (this.mPerspective != PERSPECTIVE_NEARBY)
			return;

		if (mCurIndex + 1 >= this.mNearbyDrivers.size()) {
			Toast.makeText(this, getResources().getString(R.string.tip_nonextdriver), Toast.LENGTH_LONG).show();
			return;
		}

		String nextId = this.mNearbyDrivers.get(mCurIndex + 1);

		if (null != this.mRefreshTask)
			return;

		mDriverStatusMessageView.setText(R.string.tip_nearbydriverstatustext);

		ActivityUtil.showProgress(getApplicationContext(), mDriverStatusView, mDriverFormView, true);
		mRefreshTask = new RefreshDriverInfoTask(nextId);
		mRefreshTask.execute((Void) null);
	}

	private void chat() {
		Assert.assertEquals(PERSPECTIVE_FELLOW, mPerspective);

		if (this.mPerspective != PERSPECTIVE_FELLOW)
			return;

		Intent intent = new Intent(this, ChatActivity.class);
		intent.putExtra(ChatActivity.KEY_FELLOWID, this.mDriverId);
		intent.putExtra(ChatActivity.KEY_CHATSTYLE, ChatActivity.CHAT_STYLE_2ONE);
		this.startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.driverinfo_goback:
			bak();
			break;
		case R.id.driverinfo_shake:
			shake();
			break;
		case R.id.driverinfo_next:
			next();
			break;
		case R.id.driverinfo_title_btn_msg:
			chat();
			break;
		}
	}

	public class RefreshDriverInfoTask extends AsyncTask<Void, Void, WebCallBaseResult> {

		String driverId;

		RefreshDriverInfoTask(String id) {
			this.driverId = id;
		}

		@Override
		protected WebCallBaseResult doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			WebCallBaseResult result = null;
			try {

				VehicleWebClient webClient = new VehicleWebClient();
				result = webClient.CarListView(driverId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (null == result || !result.isSuccess()) {
				result = new CarListViewResult();
				result.setCode(WebCallBaseResult.CODE_SUCCESS);
			}

			return result;
		}

		@Override
		protected void onPostExecute(final WebCallBaseResult result) {
			mRefreshTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mDriverStatusView, mDriverFormView, false);

			if (null != result && result.isSuccess()) {
				CarListViewResult carListResult = (CarListViewResult) result;
				Driver driver = SelfMgr.getInstance().getNearbyDriver(driverId);

				try {
					if (null != carListResult.getResult())
						driver.setCars(carListResult.getInfoBean());
				} catch (Exception e) {
					e.printStackTrace();
				}

				setViewData(driver);
				mDriverId = driverId;

				mCurIndex++;

				System.out.println("new driverIdddddd:" + mDriverId);
			} else {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_getdriverinfofailed),
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mRefreshTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mDriverStatusView, mDriverFormView, false);
		}
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
