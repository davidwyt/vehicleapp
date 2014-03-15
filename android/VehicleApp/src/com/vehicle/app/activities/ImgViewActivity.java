package com.vehicle.app.activities;

import java.io.File;

import com.vehicle.app.mgrs.BitmapCache;
import com.vehicle.app.utils.ActivityUtil;
import com.vehicle.app.utils.ImageUtil;

import cn.edu.sjtu.vehicleapp.R;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImgViewActivity extends TemplateActivity {

	public static final String KEY_IMGPATH = "com.vehicle.app.imgview.key.path";
	public static final String KEY_IMGURL = "com.vehicle.app.imgview.key.url";

	private Button mBtnClose;
	private ImageView mImgView;

	private LoadBitmapTask mTask;

	private View mImgFormView;
	private View mImgStatusView;
	private TextView mImgStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_imgview);
		mBtnClose = (Button) this.findViewById(R.id.imgview_bak);
		mBtnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mImgView = (ImageView) this.findViewById(R.id.imgview_img);
		mImgFormView = findViewById(R.id.imgview_form);
		mImgStatusView = findViewById(R.id.imgview_status);
		mImgStatusMessageView = (TextView) findViewById(R.id.imgview_status_message);
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
	protected void onStart() {
		super.onStart();

		try {
			Bundle bundle = this.getIntent().getExtras();

			if (null != bundle) {
				if (bundle.containsKey(KEY_IMGPATH)) {
					String path = bundle.getString(KEY_IMGPATH);

					DisplayMetrics dm = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(dm);

					float density = dm.density;

					int screenWidth = (int) (dm.widthPixels * density + 0.5f);
					int screenHeight = (int) (dm.heightPixels * density + 0.5f);

					System.out.println("screenWidth:" + screenWidth + " height:" + screenHeight);

					File file = new File(path);
					if (file.isFile() && file.exists()) {
						Bitmap bitmap = ImageUtil.decodeSampledBitmapFromFile(path, 400, 500);

						mImgView.setImageBitmap(bitmap);
					}
				} else if (bundle.containsKey(KEY_IMGURL)) {
					String url = bundle.getString(KEY_IMGURL);
					Bitmap bitmap = BitmapCache.getInstance().get(url);
					if (null == bitmap) {
						attemptLoadImg(url);
					} else {
						mImgView.setImageBitmap(bitmap);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	private void attemptLoadImg(String url) {
		if (null != this.mTask)
			return;

		mImgStatusMessageView.setText(R.string.tip_imgloading);
		ActivityUtil.showProgress(getApplicationContext(), mImgStatusView, mImgFormView, true);
		this.mTask = new LoadBitmapTask(url);
		this.mTask.execute();
	}

	class LoadBitmapTask extends AsyncTask<Void, Void, Void> {

		String url;

		LoadBitmapTask(String url) {
			this.url = url;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			if (!BitmapCache.getInstance().contains(url)) {
				Bitmap bitmap = ImageUtil.loadBitmap(url);
				BitmapCache.getInstance().put(url, bitmap);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mImgStatusView, mImgFormView, false);

			Bitmap bitmap = BitmapCache.getInstance().get(url);
			if (null != bitmap) {
				mImgView.setImageBitmap(bitmap);
			}
		}

		@Override
		protected void onCancelled() {
			mTask = null;
			ActivityUtil.showProgress(getApplicationContext(), mImgStatusView, mImgFormView, false);
		}
	}

}
