package com.vehicle.app.msg.worker;

import java.util.List;

import cn.edu.sjtu.vehicleapp.R;

import com.vehicle.app.activities.VendorRatingActivity;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.CommentMessage;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.web.bean.AddCommentResult;
import com.vehicle.sdk.client.VehicleClient;
import com.vehicle.sdk.client.VehicleWebClient;
import com.vehicle.service.bean.CommentFileResponse;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

public class VendorCommentMessageCourier extends MessageBaseCourier {

	private Context context;

	public VendorCommentMessageCourier(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public void dispatch(IMessageItem item) {
		// TODO Auto-generated method stub
		if (!(item instanceof CommentMessage)) {
			throw new IllegalArgumentException("item not CommentMessage");
		}

		final CommentMessage msg = (CommentMessage) item;

		AsyncTask<Void, Void, AddCommentResult> asyncTask = new AsyncTask<Void, Void, AddCommentResult>() {

			@Override
			protected AddCommentResult doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				AddCommentResult result = null;
				try {
					String imgNames = "";

					List<String> imgPaths = msg.getImgPaths();
					if (null != imgPaths) {
						for (int i = 0; i < imgPaths.size(); i++) {
							String path = imgPaths.get(i);

							VehicleClient client = new VehicleClient(SelfMgr.getInstance().getId());
							CommentFileResponse resp = client.UploadCommentImg(path);
							if (null != resp && resp.isSucess()) {
								String name = resp.getFileName();
								imgNames += name;
								if (i != imgPaths.size() - 1) {
									imgNames += Constants.IMGNAME_DIVIDER;
								}
							} else {
								throw new Exception("upload img failed");
							}
						}
					}

					System.out.println("img:" + imgNames);

					VehicleWebClient client = new VehicleWebClient();
					result = client.AddComment(msg.getSource(), msg.getTarget(), msg.getComment(), msg.getPriceScore(),
							msg.getTechnologyScore(), msg.getEfficiencyScore(), msg.getReceptionScore(),
							msg.getEnvironmentScore(), msg.getMainProjectId(), imgNames);

				} catch (Exception e) {
					e.printStackTrace();
				}
				return result;
			}

			@Override
			protected void onPostExecute(AddCommentResult result) {

				if (null != result && result.isSuccess()) {
					System.out.println("add comment success");

					Intent intent = new Intent(Constants.ACTION_VENDORCOMMENT_SUCCESS);
					context.sendBroadcast(intent);
				} else {
					Intent intent = new Intent(Constants.ACTION_VENDORCOMMENT_FAILED);
					String msg = null == result ? context.getResources().getString(R.string.tip_commentfailed)
							: context.getResources().getString(R.string.tip_commentfailedformat, result.getMessage());
					intent.putExtra(VendorRatingActivity.KEY_COMMENT_ERRMSG, msg);
					context.sendBroadcast(intent);
				}
			}
		};

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			asyncTask.execute();
		}
	}
}
