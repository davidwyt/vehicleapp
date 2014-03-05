package com.vehicle.app.fragments;

import com.vehicle.app.utils.ImageUtil;

import cn.edu.sjtu.vehicleapp.R;
import android.annotation.SuppressLint;
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

		View view = inflater.inflate(R.layout.layout_imageviewframe, container, false);
		ImageView img = (ImageView) view.findViewById(R.id.frame_img);
		ImageUtil.RenderImageView(url, img, -1, -1);
		return view;
	}
}
