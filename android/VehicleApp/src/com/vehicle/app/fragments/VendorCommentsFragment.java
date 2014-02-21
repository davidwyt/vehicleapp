package com.vehicle.app.fragments;

import java.util.List;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.bean.Comment;
import com.vehicle.app.bean.Vendor;
import com.vehicle.app.mgrs.BitmapCache;
import com.vehicle.app.msg.worker.ImageViewBitmapLoader;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class VendorCommentsFragment extends Fragment {

	private Vendor vendor;

	public VendorCommentsFragment(Vendor vendor) {
		this.vendor = vendor;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View parent = inflater.inflate(R.layout.layout_vendorcomments, container, false);

		ListView commentsView = (ListView) parent.findViewById(R.id.vendorcomments);
		CommentsViewAdapter adapter = new CommentsViewAdapter(inflater, vendor.getReviews());
		commentsView.setAdapter(adapter);

		return parent;
	}

	class CommentsViewAdapter extends BaseAdapter {

		private List<Comment> comments;
		private LayoutInflater inflater;

		CommentsViewAdapter(LayoutInflater inflater, List<Comment> comments) {
			this.comments = comments;
			this.inflater = inflater;
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
			Bitmap bitmap = BitmapCache.getInstance().get(url);

			if (null != bitmap) {
				ivHead.setImageBitmap(bitmap);
			} else {
				ivHead.setTag(R.id.TAGKEY_BITMAP_URL, url);

				ImageViewBitmapLoader loader = new ImageViewBitmapLoader(ivHead);
				loader.load();
			}

			TextView tvName = (TextView) view.findViewById(R.id.commentitem_tv_username);
			tvName.setText(comment.getName());

			RatingBar rbScore = (RatingBar) view.findViewById(R.id.ratingbar_score);
			rbScore.setRating((float) comment.getScore());

			TextView tvReviews = (TextView) view.findViewById(R.id.commentitem_tv_reviews);
			tvReviews.setText(comment.getReviews());

			return view;
		}
	}
}
