package com.vehicle.app.msg.worker;

import com.vehicle.app.msg.bean.CommentMessage;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.web.bean.AddCommentResult;
import com.vehicle.sdk.client.VehicleWebClient;

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
					VehicleWebClient client = new VehicleWebClient();
					result = client.AddComment(msg.getSource(), msg.getTarget(), msg.getComment(), msg.getPriceScore(),
							msg.getTechnologyScore(), msg.getEfficiencyScore(), msg.getReceptionScore(),
							msg.getEnvironmentScore(), msg.getMainProjectId());

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
