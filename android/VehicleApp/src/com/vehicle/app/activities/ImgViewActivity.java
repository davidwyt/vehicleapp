package com.vehicle.app.activities;

import java.io.File;

import com.vehicle.app.utils.ImageUtil;

import cn.edu.sjtu.vehicleapp.R;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class ImgViewActivity extends TemplateActivity {

	public static final String KEY_IMGPATH = "com.vehicle.app.imgview.key.path";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_imgview);
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

					ImageView img = (ImageView) this.findViewById(R.id.imgview_img);
					img.setImageBitmap(bitmap);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}
