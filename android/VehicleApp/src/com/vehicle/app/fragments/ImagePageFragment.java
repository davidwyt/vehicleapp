package com.vehicle.app.fragments;

import com.vehicle.app.mgrs.BitmapCache;
import com.vehicle.app.utils.ImageUtil;

import cn.edu.sjtu.vehicleapp.R;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

@SuppressLint("ValidFragment")
public class ImagePageFragment extends Fragment {

	private String url;

	public ImagePageFragment(String url) {
		this.url = url;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		System.out.println("in create view of img page");

		View view = inflater.inflate(R.layout.layout_imageviewframe, container, false);
		ImageView img = (ImageView) view.findViewById(R.id.frame_img);
		/**
		Bitmap bitmap = BitmapCache.getInstance().get(url);
		if (null != bitmap) {
			img.setImageBitmap(bitmap);
		} else {
			System.out.println("bitmap is null");
		}
		*/
		ImageUtil.RenderImageView(url, img, -1, -1);
		return view;
	}
}
