package com.vehicle.app.adapter;

import cn.edu.sjtu.vehicleapp.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImagePageFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_imageviewframe, container, false);
		ImageView img = (ImageView) view.findViewById(R.id.frame_img);
		img.setImageResource(R.drawable.activity_bg_start);
		return view;
	}
}
