package com.vehicle.app.fragments;

import com.vehicle.app.activities.ImgViewActivity;
import com.vehicle.app.mgrs.BitmapCache;
import com.vehicle.app.utils.ImageUtil;

import cn.edu.sjtu.vehicleapp.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

@SuppressLint("ValidFragment")
public class ImagePageFragment extends Fragment {

	private String url;

	public ImagePageFragment(String url) {
		this.url = url;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		System.out.println("in create view of img page");

		View view = inflater.inflate(R.layout.layout_imageviewframe, container, false);
		ImageView img = (ImageView) view.findViewById(R.id.frame_img);
		ImageUtil.RenderImageView(url, img, -1, -1);

		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!BitmapCache.getInstance().contains(url)) {
					return;
				}
				
				Intent intent = new Intent(inflater.getContext(), ImgViewActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(ImgViewActivity.KEY_IMGURL, url);
				inflater.getContext().startActivity(intent);
			}
		});

		return view;
	}
}
