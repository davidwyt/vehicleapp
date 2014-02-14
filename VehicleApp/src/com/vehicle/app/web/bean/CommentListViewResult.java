package com.vehicle.app.web.bean;

import java.util.List;
import java.util.Map;

import com.vehicle.app.bean.Comment;

public class CommentListViewResult extends WebCallBaseResult {

	private Map<String, List<Comment>> result;

	@Override
	public List<Comment> getInfoBean() {
		// TODO Auto-generated method stub
		//return isSuccess() ? JsonUtil.getSomeValueList(getResult(), "Review.list", Comment.class) : null;
		return result.get("Review.list");
	}

	@Override
	public Map<String, List<Comment>> getResult() {
		// TODO Auto-generated method stub
		return result;
	}
}
