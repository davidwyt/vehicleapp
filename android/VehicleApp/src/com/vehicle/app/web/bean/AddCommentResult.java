package com.vehicle.app.web.bean;

public class AddCommentResult extends WebCallBaseResult {

	@Override
	public Object getInfoBean() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean isUploadFailed = false;

	public void setIsUploadFailed(boolean upload) {
		this.isUploadFailed = upload;
	}

	public boolean getIsUploadFailed() {
		return this.isUploadFailed;
	}
}
