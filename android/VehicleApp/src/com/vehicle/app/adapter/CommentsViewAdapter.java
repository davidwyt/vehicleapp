package com.vehicle.app.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.bean.Comment;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.ImageUtil;

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
			String[] paths = imgNames.split(Constants.IMGNAME_DIVIDER);
			setImg(img1, paths, 0);
			setImg(img2, paths, 1);
			setImg(img3, paths, 2);
			setImg(img4, paths, 3);
		}
		return view;
	}

	private void setImg(ImageView img, String[] paths, int index) {
		if (index < paths.length) {
			String url = paths[index];
			ImageUtil.RenderImageView(url, img, -1, -1);
		}

	}
}
