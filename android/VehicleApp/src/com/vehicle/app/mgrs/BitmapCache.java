package com.vehicle.app.mgrs;

import java.util.Hashtable;
import java.util.Map;

import android.graphics.Bitmap;

public class BitmapCache {

	private Map<String, Bitmap> bitmapCache;

	private BitmapCache() {
		bitmapCache = new Hashtable<String, Bitmap>();
	}

	private static class InstanceHolder {
		private static BitmapCache instance = new BitmapCache();

		private InstanceHolder() {
		}
	}

	public static BitmapCache getInstance() {
		return InstanceHolder.instance;
	}

	public void clear() {
		bitmapCache.clear();
	}

	public Bitmap get(String key) {
		return this.bitmapCache.get(key);
	}

	public void put(String key, Bitmap bitmap) {
		this.bitmapCache.put(key, bitmap);
	}

	public boolean contains(String key) {
		return this.bitmapCache.containsKey(key);
	}

}
