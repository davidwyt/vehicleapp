package com.vehicle.sdk.client;

import java.io.File;

import com.vehicle.imserver.service.bean.FileFetchRequest;
import com.vehicle.imserver.service.bean.FollowshipRequest;
import com.vehicle.imserver.service.bean.MessageACKRequest;
import com.vehicle.imserver.service.bean.MessageOne2FolloweesRequest;
import com.vehicle.imserver.service.bean.MessageOne2FollowersRequest;
import com.vehicle.imserver.service.bean.MessageOne2OneRequest;
import com.vehicle.sdk.utils.JerseyUtil;
import com.vehicle.sdk.utils.URLUtil;

public class VehicleClient {

	private static final String URL_SERVERROOT = "http://localhost:8080/VehicleIMServer/rest";

	private static final String URL_MESSAGE_ROOT = "message";
	private static final String URL_MESSAGE_ONE2ONE = "one2one";
	private static final String URL_MESSAGE_ACK = "ack";
	private static final String URL_MESSAGE_ONE2FOLLOWERS = "one2followers";
	private static final String URL_MESSAGE_ONE2FOLLOWEES = "one2followees";

	private static final String URL_FILETRANSMISSION_ROOT = "fileTransmission";
	private static final String URL_FILETRANSMISSION_SEND = "send/source=%s&&target=%s&&fileName=%s";
	private static final String URL_FILETRANSMISSION_FETCH = "fetch/{token}";

	private static final String URL_FOLLOWSHIP_ROOT = "followship";
	private static final String URL_FOLLOWSHIP_FOLLOW = "follow";
	private static final String URL_FOLLOWSHIP_DROP = "drop";
	private static final String URL_FOLLOWSHIP_FOLLOWEES = "followees/%s";
	private static final String URL_FOLLOWSHIP_FOLLOWERS = "followers/%s";

	private String source;

	public VehicleClient(String source) {
		this.source = source;
	}

	public void SendMessage(String target, String content) {
		MessageOne2OneRequest request = new MessageOne2OneRequest();
		request.setSource(source);
		request.setTarget(target);
		request.setContent(content);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT,
				URL_MESSAGE_ONE2ONE);

		JerseyUtil.HttpPost(url, request);
	}

	public void AckMessage(String msgId) {
		MessageACKRequest request = new MessageACKRequest();
		request.setMsgId(msgId);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT,
				URL_MESSAGE_ACK);

		JerseyUtil.HttpPost(url, request);
	}

	public void SendMessageToFollowers(String content) {
		MessageOne2FollowersRequest request = new MessageOne2FollowersRequest();
		request.setSource(source);
		request.setContent(content);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT,
				URL_MESSAGE_ONE2FOLLOWERS);

		JerseyUtil.HttpPost(url, request);
	}

	public void SendMessageToFollowees(String content) {
		MessageOne2FolloweesRequest request = new MessageOne2FolloweesRequest();
		request.setSource(source);
		request.setContent(content);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT,
				URL_MESSAGE_ONE2FOLLOWEES);

		JerseyUtil.HttpPost(url, request);
	}

	public void SendFile(String target, String filePath) {
		File file = new File(filePath);

		String url = URLUtil.UrlAppend(
				URL_SERVERROOT,
				URL_FILETRANSMISSION_ROOT,
				String.format(URL_FILETRANSMISSION_SEND, source, target,
						file.getName()));

		JerseyUtil.UploadFile(url, filePath);
	}

	public void FetchFile(String token, String filePath) {
		FileFetchRequest request = new FileFetchRequest();
		request.setToken(token);

		String url = URLUtil.UrlAppend(URL_SERVERROOT,
				URL_FILETRANSMISSION_ROOT, URL_FILETRANSMISSION_FETCH);

		JerseyUtil.DownloadFile(url, filePath);
	}

	public void Follow(String target) {
		FollowshipRequest request = new FollowshipRequest();
		request.setFollower(source);
		request.setFollowee(target);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT,
				URL_FOLLOWSHIP_FOLLOW);

		JerseyUtil.HttpPost(url, request);
	}

	public void DropFollow(String target) {
		FollowshipRequest request = new FollowshipRequest();
		request.setFollower(source);
		request.setFollowee(target);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT,
				URL_FOLLOWSHIP_DROP);

		JerseyUtil.HttpPost(url, request);
	}

	public void GetFollowees() {
		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT,
				String.format(URL_FOLLOWSHIP_FOLLOWEES, source));

		JerseyUtil.HttpGet(url);
	}

	public void GetFollowers() {
		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT,
				String.format(URL_FOLLOWSHIP_FOLLOWERS, source));

		JerseyUtil.HttpGet(url);
	}
}
