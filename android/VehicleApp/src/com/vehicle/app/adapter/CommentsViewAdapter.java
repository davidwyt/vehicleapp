package com.vehicle.app.adapter;

import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.activities.ImgViewActivity;
import com.vehicle.app.bean.Comment;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.ImageUtil;
import com.vehicle.app.utils.StringUtil;

public class CommentsViewAdapter extends BaseAdapter {

	private List<Comment> comments;
	private LayoutInflater inflater;
	private boolean driverView;

	public CommentsViewAdapter(LayoutInflater inflater, List<Comment> comments, boolean driverView) {
		this.comments = comments;
		this.inflater = inflater;
		this.driverView = driverView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == this.comments ? 0 : this.comments.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return null == this.comments ? null : this.comments.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup viewGroup) {
		// TODO Auto-generated method stub

		if (null == view) {
			view = this.inflater.inflate(R.layout.layout_comment_item, null);
		}

		Comment comment = this.comments.get(pos);

		ImageView ivHead = (ImageView) view.findViewById(R.id.commentitem_iv_userhead);
		String url = comment.getAvatar();
		ImageUtil.RenderImageView(url, ivHead, -1, -1);

		TextView tvName = (TextView) view.findViewById(R.id.commentitem_tv_username);
		if (this.driverView) {
			tvName.setText(comment.getName());
		} else {
			tvName.setText(comment.getAlias());
		}

		RatingBar rbScore = (RatingBar) view.findViewById(R.id.ratingbar_score);
		rbScore.setRating((float) comment.getScore());

		TextView tvDate = (TextView) view.findViewById(R.id.commentitem_tv_date);
		tvDate.setText(comment.getAddedDate());

		TextView tvReviews = (TextView) view.findViewById(R.id.commentitem_tv_reviews);
		tvReviews.setText(comment.getReviews());

		ImageView img1 = (ImageView) view.findViewById(R.id.vendorrating_img1);

		ImageView img2 = (ImageView) view.findViewById(R.id.vendorrating_img2);

		ImageView img3 = (ImageView) view.findViewById(R.id.vendorrating_img3);

		ImageView img4 = (ImageView) view.findViewById(R.id.vendorrating_img4);

		String imgNames = comment.getImgNamesS();

		if (null != imgNames && imgNames.length() > 0) {

			String[] paths = imgNames.split(Constants.IMGNAME_DIVIDER_DECODER);

			setImg(comment, img1, paths, 0);
			setImg(comment, img2, paths, 1);
			setImg(comment, img3, paths, 2);
			setImg(comment, img4, paths, 3);
		} else {
			view.findViewById(R.id.vendorrating_imgs).setVisibility(View.GONE);
		}
		return view;
	}

	private void setImg(final Comment comment, ImageView img, String[] paths, final int index) {
		if (index < paths.length) {
			String url = paths[index];
			if (!StringUtil.IsNullOrEmpty(url))
				ImageUtil.RenderImageView(url, img, -1, -1);

			img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					try {
						String imgNames = comment.getImgNamesL();
						if (null != imgNames && imgNames.length() > 0) {

							String[] largePaths = imgNames.split(Constants.IMGNAME_DIVIDER_DECODER);
							if (null != largePaths && largePaths.length > index) {
								Intent intent = new Intent(inflater.getContext(), ImgViewActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

								intent.putExtra(ImgViewActivity.KEY_IMGURL, largePaths[index]);
								inflater.getContext().startActivity(intent);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}
