package com.vehicle.app.adapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.bean.Driver;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.SelfMgr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NearbyFellowsViewAdapter extends BaseAdapter {

	private List<?> fellows;
	private LayoutInflater inflater;

	private final static int ICON_WIDTH = 64;
	private final static int ICON_HEIGHT = 64;

	public NearbyFellowsViewAdapter(Context context, List<?> fellows) {
		this.inflater = LayoutInflater.from(context);
		this.fellows = fellows;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.fellows.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return this.fellows.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	private Bitmap loadBitmap(String url) {
		Bitmap bm = null;
		InputStream is = null;
		BufferedInputStream bis = null;
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

	private void loadBitmapAsync(ImageView view) {
		LoadBitmapTask task = new LoadBitmapTask();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, view);
		} else {
			task.execute(view);
		}
	}

	@Override
	public View getView(int pos, View view, ViewGroup viewGroup) {
		// TODO Auto-generated method stub

		if (null == view) {
			view = this.inflater.inflate(R.layout.layout_nearbyfellow_item, null);
		}

		TextView tvAlias = (TextView) view.findViewById(R.id.nearbyfellow_tv_name);
		TextView tvFeature = (TextView) view.findViewById(R.id.nearbyfellow_tv_feature);
		TextView tvDistance = (TextView) view.findViewById(R.id.nearbyfellow_tv_distance);
		ImageView ivHead = (ImageView) view.findViewById(R.id.nearbyfellow_iv_head);

		Object fellow = this.fellows.get(pos);

		if (fellow instanceof Driver && !SelfMgr.getInstance().isDriver()) {

			Driver driver = (Driver) fellow;

			tvAlias.setText(driver.getAlias());

			tvFeature.setText(driver.getIntroduction());

			tvDistance.setText(driver.getDistance());

			if (null == driver.getIcon()) {
				ivHead.setTag(driver);
				loadBitmapAsync(ivHead);
			} else {
				ivHead.setImageBitmap(Bitmap.createScaledBitmap(driver.getIcon(), ICON_WIDTH, ICON_HEIGHT, true));
			}

		} else if (fellow instanceof Vendor && SelfMgr.getInstance().isDriver()) {

			Vendor vendor = (Vendor) fellow;

			tvAlias.setText(vendor.getName());

			tvFeature.setText(vendor.getAddress());

			tvDistance.setText(vendor.getDistance());

			if (null == vendor.getIcon()) {
				ivHead.setTag(vendor);
				loadBitmapAsync(ivHead);
			} else {
				ivHead.setImageBitmap(Bitmap.createScaledBitmap(vendor.getIcon(), ICON_WIDTH, ICON_HEIGHT, true));
			}
		} else {
			System.err.println("what the type of fellow...");
		}

		return view;
	}

	class LoadBitmapTask extends AsyncTask<ImageView, Void, Bitmap> {
		private ImageView imageView;
		private Object fellow;

		@Override
		protected Bitmap doInBackground(ImageView... arg0) {
			// TODO Auto-generated method stub
			imageView = arg0[0];
			fellow = imageView.getTag();

			String url = "";
			if (fellow instanceof Driver) {
				url = ((Driver) fellow).getAvatar();
			} else if (fellow instanceof Vendor) {
				url = ((Vendor) fellow).getAvatar();
			}

			return loadBitmap(url);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (null == result) {
				System.err.println("load bitmap failed");
				return;
			}

			if (fellow instanceof Driver) {
				((Driver) fellow).setIcon(result);
			} else if (fellow instanceof Vendor) {
				((Vendor) fellow).setIcon(result);
			}
			this.imageView.setImageBitmap(Bitmap.createScaledBitmap(result, ICON_WIDTH, ICON_HEIGHT, true));
		}

	}
}
