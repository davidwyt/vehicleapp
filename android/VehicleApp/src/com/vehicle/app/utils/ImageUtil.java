package com.vehicle.app.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;
import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.mgrs.BitmapCache;
import com.vehicle.app.msg.worker.ImageViewBitmapLoader;

public class ImageUtil {

	public static void RenderImageView(String url, ImageView iv, int width, int height) {
		if (null == iv)
			return;

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
}
