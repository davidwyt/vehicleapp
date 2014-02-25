package com.vehicle.app.activities;

import java.util.ArrayList;
import java.util.List;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public class ImgViewActivity extends Activity {

	public static final String KEY_IMGBYTES = "com.vehicle.app.imgview.key.bitmap";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_imgview);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Bundle bundle = this.getIntent().getExtras();
		if (null != bundle) {
			byte[] bytes = bundle.getByteArray(KEY_IMGBYTES);
			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			List<Bitmap> imgs = new ArrayList<Bitmap>();
			imgs.add(bitmap);

			Gallery gallery = (Gallery) this.findViewById(R.id.gallery);

			gallery.setAdapter(new ImageAdapter(this, imgs));
		}
	}

	class ImageAdapter extends BaseAdapter {
		List<Bitmap> imgs;

		public ImageAdapter(Context c, List<Bitmap> imgs) {
			mContext = c;
			this.imgs = imgs;
		}

		public int getCount() {
			return null == imgs ? 0 : imgs.size();
		}

		public Object getItem(int position) {
			return null == imgs ? null : imgs.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView img = new ImageView(mContext);
			img.setImageBitmap(imgs.get(position));
			img.setAdjustViewBounds(true);
			img.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			return img;
		}

		private Context mContext;

	}
}
