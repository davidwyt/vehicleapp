package com.vehicle.app.utils;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.mgrs.BitmapCache;
import com.vehicle.app.msg.worker.ImageViewBitmapLoader;

public class ImageUtil {

	public static void RenderImageView(String url, ImageView iv, int width, int height) {
		if (null == iv) {
			return;
		}

		Bitmap bitmap = BitmapCache.getInstance().get(url);
		if (null != bitmap) {
			if (width > 0 && height > 0 && (width != bitmap.getWidth() || height != bitmap.getHeight())) {
				iv.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width, height, true));
			} else {
				iv.setImageBitmap(bitmap);
			}
		} else {
			iv.setTag(R.id.TAGKEY_BITMAP_URL, url);

			if (width > 0) {
				iv.setTag(R.id.TAGKEY_BITMAP_WIDTH, width);
			}

			if (height > 0) {
				iv.setTag(R.id.TAGKEY_BITMAP_HEIGHT, height);
			}

			ImageViewBitmapLoader loader = new ImageViewBitmapLoader(iv);
			loader.load();
		}
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {

		File file = new File(filePath);
		if (!file.isFile() || !file.exists()) {
			return null;
		}

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	public static Bitmap decodeSampledBitmapFromByteArray(byte[] bytes, int reqWidth, int reqHeight) {
		if (null == bytes)
			return null;

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
	}
}
