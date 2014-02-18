package com.vehicle.app.msg.worker;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.mgrs.BitmapCache;

import junit.framework.Assert;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ImageView;

public class ImageViewBitmapLoader {

	private ImageView mImageView;

	public ImageViewBitmapLoader(ImageView view) {
		this.mImageView = view;

		Assert.assertEquals(null != view, true);
		Assert.assertEquals(null != view.getTag(R.id.TAGKEY_BITMAP_URL), true);
	}

	private Bitmap loadBitmap(String url) {
		Bitmap bm = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		System.out.println("urllllllllll:" + url);
		try {
			URLConnection conn = new URL(url).openConnection();
			conn.connect();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(bis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bm;
	}

	public void load() {
		LoadBitmapTask task = new LoadBitmapTask();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			task.execute();
		}
	}

	class LoadBitmapTask extends AsyncTask<Void, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			String url = (String) mImageView.getTag(R.id.TAGKEY_BITMAP_URL);

			Bitmap result = BitmapCache.getInstance().get(url);

			if (null == result) {
				result = loadBitmap(url);
			}

			return loadBitmap(url);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (null == result) {
				System.err.println("load bitmap failed");
				return;
			}

			String url = (String) mImageView.getTag(R.id.TAGKEY_BITMAP_URL);
			BitmapCache.getInstance().put(url, result);

			Object disWidth = mImageView.getTag(R.id.TAGKEY_BITMAP_WIDTH);
			Object disHeight = mImageView.getTag(R.id.TAGKEY_BITMAP_HEIGHT);

			int width = result.getWidth();
			int height = result.getHeight();

			if (null != disWidth) {
				width = (Integer) disWidth;
			}

			if (null != disHeight) {
				height = (Integer) disHeight;
			}

			if (width != result.getWidth() || height != result.getHeight()) {
				mImageView.setImageBitmap(Bitmap.createScaledBitmap(result, width, height, true));
			} else {
				mImageView.setImageBitmap(result);
			}
		}

	}

}
