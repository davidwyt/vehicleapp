package com.vehicle.sdk.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.vehicle.sdk.utils.FileUtil;
import com.vehicle.sdk.utils.HttpUtil;
import com.vehicle.sdk.utils.URLUtil;
import com.vehicle.service.bean.FileFetchRequest;
import com.vehicle.service.bean.FileTransmissionResponse;
import com.vehicle.service.bean.FolloweesResponse;
import com.vehicle.service.bean.FollowersResponse;
import com.vehicle.service.bean.FollowshipAddedRequest;
import com.vehicle.service.bean.FollowshipAddedResponse;
import com.vehicle.service.bean.FollowshipDroppedRequest;
import com.vehicle.service.bean.FollowshipDroppedResponse;
import com.vehicle.service.bean.FollowshipRequest;
import com.vehicle.service.bean.FollowshipResponse;
import com.vehicle.service.bean.MessageACKRequest;
import com.vehicle.service.bean.MessageACKResponse;
import com.vehicle.service.bean.MessageOne2FolloweesRequest;
import com.vehicle.service.bean.MessageOne2FolloweesResponse;
import com.vehicle.service.bean.MessageOne2FollowersRequest;
import com.vehicle.service.bean.MessageOne2FollowersResponse;
import com.vehicle.service.bean.MessageOne2OneRequest;
import com.vehicle.service.bean.MessageOne2OneResponse;

public class VehicleClient {

	private static final String URL_DEFAULTSERVERROOT = "http://10.0.2.2:8080/VehicleIMServer/rest";

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
	private static final String URL_FOLLOWSHIP_ADDED = "added";
	private static final String URL_FOLLOWSHIP_DROPPED = "dropped";

	private String URL_SERVERROOT;
	private String source;

	public VehicleClient(String serverUrl, String source) {
		this.URL_SERVERROOT = serverUrl;
		this.source = source;
	}

	public VehicleClient(String source) {
		this(URL_DEFAULTSERVERROOT, source);
	}

	public MessageOne2OneResponse SendMessage(String target, String content) {
		MessageOne2OneRequest request = new MessageOne2OneRequest();
		request.setSource(source);
		request.setTarget(target);
		request.setContent(content);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT,
				URL_MESSAGE_ONE2ONE);

		System.out.println("url:------------" + url);

		return HttpUtil.PostJson(url, request, MessageOne2OneResponse.class);

		// JerseyUtil.HttpPost(url, request, MessageOne2OneResponse.class);
	}

	public void AckMessage(String msgId) {
		MessageACKRequest request = new MessageACKRequest();
		request.setMsgId(msgId);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT,
				URL_MESSAGE_ACK);

		HttpUtil.PostJson(url, request, MessageACKResponse.class);
		// JerseyUtil.HttpPost(url, request, MessageACKResponse.class);
	}

	public void SendMessageToFollowers(String content) {
		MessageOne2FollowersRequest request = new MessageOne2FollowersRequest();
		request.setSource(source);
		request.setContent(content);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT,
				URL_MESSAGE_ONE2FOLLOWERS);

		HttpUtil.PostJson(url, request, MessageOne2FollowersResponse.class);
		// JerseyUtil.HttpPost(url, request,
		// MessageOne2FollowersResponse.class);
	}

	public void SendMessageToFollowees(String content) {
		MessageOne2FolloweesRequest request = new MessageOne2FolloweesRequest();
		request.setSource(source);
		request.setContent(content);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT,
				URL_MESSAGE_ONE2FOLLOWEES);

		HttpUtil.PostJson(url, request, MessageOne2FolloweesResponse.class);
		// JerseyUtil.HttpPost(url, request,
		// MessageOne2FolloweesResponse.class);
	}

	public void SendFile(String target, String filePath) {
		File file = new File(filePath);

		String url = URLUtil.UrlAppend(
				URL_SERVERROOT,
				URL_FILETRANSMISSION_ROOT,
				String.format(URL_FILETRANSMISSION_SEND, source, target,
						file.getName()));

		HttpUtil.UploadFile(url, filePath, FileTransmissionResponse.class);
		// JerseyUtil.UploadFile(url, filePath);
	}

	public void FetchFile(String token, String filePath) {
		FileFetchRequest request = new FileFetchRequest();
		request.setToken(token);

		String url = URLUtil.UrlAppend(URL_SERVERROOT,
				URL_FILETRANSMISSION_ROOT, URL_FILETRANSMISSION_FETCH);

		InputStream input = HttpUtil.DownloadFile(url);
		try {
			FileUtil.SaveFile(filePath, input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// JerseyUtil.DownloadFile(url, filePath);
	}

	public void Follow(String target) {
		FollowshipRequest request = new FollowshipRequest();
		request.setFollower(source);
		request.setFollowee(target);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT,
				URL_FOLLOWSHIP_FOLLOW);

		HttpUtil.PostJson(url, request, FollowshipRequest.class);
		// JerseyUtil.HttpPost(url, request, FollowshipRequest.class);
	}

	public void DropFollow(String target) {
		FollowshipRequest request = new FollowshipRequest();
		request.setFollower(source);
		request.setFollowee(target);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT,
				URL_FOLLOWSHIP_DROP);

		HttpUtil.PostJson(url, request, FollowshipResponse.class);
		// JerseyUtil.HttpPost(url, request, FollowshipResponse.class);
	}

	public List<String> GetFollowees() {
		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT,
				String.format(URL_FOLLOWSHIP_FOLLOWEES, source));

		FolloweesResponse resp = (FolloweesResponse) HttpUtil.GetJson(url,
				FolloweesResponse.class);

		// FolloweesResponse resp = (FolloweesResponse) JerseyUtil.HttpGet(url,
		// FolloweesResponse.class);
		return resp.getFollowees();
	}

	public List<String> GetFollowers() {
		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT,
				String.format(URL_FOLLOWSHIP_FOLLOWERS, source));

		FollowersResponse resp = (FollowersResponse) HttpUtil.GetJson(url,
				FollowersResponse.class);

		// FollowersResponse resp = (FollowersResponse) JerseyUtil.HttpGet(url,
		// FollowersResponse.class);
		return resp.getFollowers();
	}
	
	public void FollowshipAdded(String shopId)
	{
		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT, URL_FOLLOWSHIP_ADDED);
		
		FollowshipAddedRequest request = new FollowshipAddedRequest();
		request.setMemberId(source);
		request.setShopId(shopId);
		
		FollowshipAddedResponse response = HttpUtil.PostJson(url, request, FollowshipAddedResponse.class);
		
	}
	
	public void FollowshipDropped(String shopId)
	{
		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT, URL_FOLLOWSHIP_DROPPED);
		
		FollowshipDroppedRequest request = new FollowshipDroppedRequest();
		request.setMemberId(source);
		request.setShopId(shopId);
		
		FollowshipDroppedResponse response = HttpUtil.PostJson(url, request, FollowshipDroppedResponse.class);
	}
}
