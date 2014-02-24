package com.vehicle.app.activities;

import java.util.List;

import com.vehicle.app.bean.Car;
import com.vehicle.app.bean.SelfDriver;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.utils.ImageUtil;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SelfDriverInfoActivity extends Activity {

	private Button mBtnBack;
	private ImageView mIvHead;
	private TextView mTvName;
	private TextView mTvAge;
	private TextView mTvSex;
	private TextView mTvPerInfo;

	private SelfDriver mSelfDriver;

	private TableLayout mCarTable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_selfdriverinfo);
		initView();
		initData();
	}

	private void initView() {
		this.mBtnBack = (Button) this.findViewById(R.id.selfdriverinfo_goback);
		this.mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SelfDriverInfoActivity.this.onBackPressed();
			}
		});

		this.mIvHead = (ImageView) this.findViewById(R.id.driverinfo_icon);
		this.mTvName = (TextView) this.findViewById(R.id.driverinfo_name);
		this.mTvAge = (TextView) this.findViewById(R.id.driverinfo_age);
		this.mTvSex = (TextView) this.findViewById(R.id.driverinfo_sex);
		this.mTvPerInfo = (TextView) this.findViewById(R.id.driver_driverinfo);
		this.mCarTable = (TableLayout) this.findViewById(R.id.table_cars);
	}

	private void initData() {
		this.mSelfDriver = SelfMgr.getInstance().getSelfDriver();

		ImageUtil.RenderImageView(this.mSelfDriver.getAvatar(), mIvHead, -1, -1);

		this.mTvName.setText(this.mSelfDriver.getAlias());
		this.mTvAge.setText(this.mSelfDriver.getBirthday());
		this.mTvSex.setText(this.mSelfDriver.getSex());

		this.mTvPerInfo.setText(this.mSelfDriver.getIntroduction());
		this.mCarTable.removeAllViews();

		List<Car> cars = this.mSelfDriver.getCars();

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
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
