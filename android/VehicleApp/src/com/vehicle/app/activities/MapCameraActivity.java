package com.vehicle.app.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import cn.edu.sjtu.vehicleapp.R;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.CancelableCallback;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.vehicle.app.msg.bean.SimpleLocation;
import com.vehicle.app.utils.JsonUtil;

public class MapCameraActivity extends TemplateActivity implements OnClickListener, CancelableCallback {
	private MapView mapView;
	private AMap aMap;

	public static final String KEY_POSITION = "com.vehicle.app.camera.key.pos";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		mapView = (MapView) findViewById(R.id.camera_map);
		mapView.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		setUpMap();
	}

	/**
	 * 往地图上添加一个marker
	 */
	private void setUpMap() {

		Bundle bundle = this.getIntent().getExtras();
		if (null != bundle) {
			SimpleLocation loc = (SimpleLocation) bundle.getSerializable(KEY_POSITION);

			System.out.println(JsonUtil.toJsonString(loc));

			if (null != loc) {
				final LatLng pos = new LatLng(loc.getLatitude(), loc.getLongitude());
				Handler handler = new Handler();
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub

						try {
							aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(pos, 18, 0, 30)));
							aMap.addMarker(new MarkerOptions().position(pos).icon(
									BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};

				handler.postDelayed(runnable, 2000);
			}

		}

	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onCancel() {
	}

	@Override
	public void onFinish() {
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
