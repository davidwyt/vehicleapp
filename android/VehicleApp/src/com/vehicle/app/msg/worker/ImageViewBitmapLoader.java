package com.vehicle.app.msg.worker;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.mgrs.BitmapCache;
import com.vehicle.app.utils.ImageUtil;

import junit.framework.Assert;

import android.graphics.Bitmap;
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
				result = ImageUtil.loadBitmap(url);
			}

			return result;
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
