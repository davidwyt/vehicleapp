package com.vehicle.sdk.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.IMessageItem;
import com.vehicle.app.utils.FileUtil;
import com.vehicle.app.utils.HttpUtil;
import com.vehicle.app.utils.URLUtil;
import com.vehicle.service.bean.ACKAllRequest;
import com.vehicle.service.bean.ACKAllResponse;
import com.vehicle.service.bean.AddLocateRequest;
import com.vehicle.service.bean.AddLocateResponse;
import com.vehicle.service.bean.CommentFileResponse;
import com.vehicle.service.bean.FileMultiTransmissionResponse;
import com.vehicle.service.bean.FileTransmissionResponse;
import com.vehicle.service.bean.FolloweesResponse;
import com.vehicle.service.bean.FollowersResponse;
import com.vehicle.service.bean.FollowshipAddedRequest;
import com.vehicle.service.bean.FollowshipAddedResponse;
import com.vehicle.service.bean.FollowshipDroppedRequest;
import com.vehicle.service.bean.FollowshipDroppedResponse;
import com.vehicle.service.bean.FollowshipInvitationACKRequest;
import com.vehicle.service.bean.FollowshipInvitationACKResponse;
import com.vehicle.service.bean.FollowshipInvitationRequest;
import com.vehicle.service.bean.FollowshipInvitationResponse;
import com.vehicle.service.bean.FollowshipInvitationResultRequest;
import com.vehicle.service.bean.FollowshipInvitationResultResponse;
import com.vehicle.service.bean.FollowshipRequest;
import com.vehicle.service.bean.FollowshipResponse;
import com.vehicle.service.bean.MessageOne2MultiRequest;
import com.vehicle.service.bean.MessageOne2MultiResponse;
import com.vehicle.service.bean.OfflineAckRequest;
import com.vehicle.service.bean.OfflineAckResponse;
import com.vehicle.service.bean.OfflineMessageRequest;
import com.vehicle.service.bean.OfflineMessageResponse;
import com.vehicle.service.bean.RangeRequest;
import com.vehicle.service.bean.RangeResponse;
import com.vehicle.service.bean.WakeupRequest;
import com.vehicle.service.bean.WakeupResponse;
import com.vehicle.service.bean.MessageACKRequest;
import com.vehicle.service.bean.MessageACKResponse;
import com.vehicle.service.bean.MessageOne2FolloweesRequest;
import com.vehicle.service.bean.MessageOne2FolloweesResponse;
import com.vehicle.service.bean.MessageOne2FollowersRequest;
import com.vehicle.service.bean.MessageOne2FollowersResponse;
import com.vehicle.service.bean.MessageOne2OneRequest;
import com.vehicle.service.bean.MessageOne2OneResponse;

public class VehicleClient {

	private static String URL_DEFAULTSERVERROOT = "http://103.21.140.232:81/VehicleIMServer/rest";
	// private static String URL_DEFAULTSERVERROOT =
	// "http://192.168.20.20:8080/VehicleIMServer/rest";
	// private static String URL_DEFAULTSERVERROOT =
	// "http://10.0.2.2:8080/VehicleIMServer/rest";

	// private static String URL_DEFAULTSERVERROOT =
	// "http://192.168.1.100:8080/VehicleIMServer/rest";

	private static final String URL_MESSAGE_ROOT = "message";
	private static final String URL_MESSAGE_ONE2ONE = "one2one";
	private static final String URL_MESSAGE_ACK = "ack";
	private static final String URL_MESSAGE_ONE2FOLLOWERS = "one2followers";
	private static final String URL_MESSAGE_ONE2FOLLOWEES = "one2followees";
	private static final String URL_MESSAGE_ONE2MULTI = "one2multi";
	private static final String URL_MESSAGE_OFFLINE = "offline";
	private static final String URL_MESSAGE_OFFLINEACK = "offlineAck";

	private static final String URL_FILETRANSMISSION_ROOT = "fileTransmission";
	private static final String URL_FILETRANSMISSION_SEND = "send/source=%s&&target=%s&&fileName=%s&&fileType=%s";
	private static final String URL_FILETRANSMISSION_COMMENTIMG = "commentfile/fileName=%s";
	private static final String URL_FILETRANSMISSION_FETCH = "fetch/%s";
	private static final String URL_FILEMULTITRANSMISSION_SEND = "sendtomulti/source=%s&&targets=%s&&fileName=%s&&fileType=%s";

	private static final String URL_FOLLOWSHIP_ROOT = "followship";
	private static final String URL_FOLLOWSHIP_FOLLOW = "follow";
	private static final String URL_FOLLOWSHIP_DROP = "drop";
	private static final String URL_FOLLOWSHIP_FOLLOWEES = "followees/%s";
	private static final String URL_FOLLOWSHIP_FOLLOWERS = "followers/%s";
	private static final String URL_FOLLOWSHIP_ADDED = "added";
	private static final String URL_FOLLOWSHIP_DROPPED = "dropped";
	private static final String URL_FOLLOWSHIP_INVITATION = "invitation";
	private static final String URL_FOLLOWSHIP_INVVERDICT = "invitationresult";
	private static final String URL_FOLLOWSHIPINV_ACK = "invack";

	private static final String URL_LOGIN_ROOT = "login";
	private static final String URL_LOGIN_WAKEUP = "wakeup";
	private static final String URL_LOGIN_ALLACK = "ackall";

	private static final String URL_LOCATE_ROOT = "locate";
	private static final String URL_LOCATE_RANGE = "range";
	private static final String URL_LOCATE_UPDATE = "add";

	private String URL_SERVERROOT;
	private String source;

	public static void setRootServer(String root) {
		URL_DEFAULTSERVERROOT = root;
	}

	public static String getRootServer() {
		return URL_DEFAULTSERVERROOT;
	}

	public VehicleClient(String serverUrl, String source) {
		this.URL_SERVERROOT = serverUrl;
		this.source = source;
	}

	public VehicleClient(String source) {
		this(URL_DEFAULTSERVERROOT, source);
	}

	public FollowshipInvitationACKResponse FollowshipInvAck(String invId) {
		FollowshipInvitationACKRequest request = new FollowshipInvitationACKRequest();
		request.setInvId(invId);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT, URL_FOLLOWSHIPINV_ACK);
		return HttpUtil.PostJson(url, request, FollowshipInvitationACKResponse.class);
	}

	public ACKAllResponse AllAck(String memberId) {
		ACKAllRequest request = new ACKAllRequest();
		request.setMemberId(memberId);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_LOGIN_ROOT, URL_LOGIN_ALLACK);
		return HttpUtil.PostJson(url, request, ACKAllResponse.class);
	}

	public OfflineMessageResponse GetOfflineMessage(String id, long since) {
		OfflineMessageRequest request = new OfflineMessageRequest();
		request.setTarget(id);
		request.setSince(since);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT, URL_MESSAGE_OFFLINE);
		return HttpUtil.PostJson(url, request, OfflineMessageResponse.class);
	}

	public OfflineAckResponse OfflineAck(String id) {
		OfflineAckRequest request = new OfflineAckRequest();
		request.setId(id);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT, URL_MESSAGE_OFFLINEACK);
		return HttpUtil.PostJson(url, request, OfflineAckResponse.class);
	}

	public MessageOne2OneResponse SendLocationMessage(String target, String content) {
		MessageOne2OneRequest request = new MessageOne2OneRequest();
		request.setSource(source);
		request.setTarget(target);
		request.setContent(content);
		request.setMessageType(IMessageItem.MESSAGE_TYPE_LOCATION);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT, URL_MESSAGE_ONE2ONE);

		System.out.println("url:------------" + url);

		return HttpUtil.PostJson(url, request, MessageOne2OneResponse.class);
	}

	public MessageOne2OneResponse SendTextMessage(String target, String content) {
		MessageOne2OneRequest request = new MessageOne2OneRequest();
		request.setSource(source);
		request.setTarget(target);
		request.setContent(content);
		request.setMessageType(IMessageItem.MESSAGE_TYPE_TEXT);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT, URL_MESSAGE_ONE2ONE);

		System.out.println("url:------------" + url);

		return HttpUtil.PostJson(url, request, MessageOne2OneResponse.class);
	}

	public MessageOne2MultiResponse SendMultiLocationMessage(String targets, String content) {
		MessageOne2MultiRequest request = new MessageOne2MultiRequest();
		request.setSource(source);
		request.setTargets(targets);
		request.setContent(content);
		request.setMessageType(IMessageItem.MESSAGE_TYPE_LOCATION);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT, URL_MESSAGE_ONE2MULTI);

		System.out.println("url:------------" + url);

		return HttpUtil.PostJson(url, request, MessageOne2MultiResponse.class);
	}

	public MessageOne2MultiResponse SendMultiTextMessage(String targets, String content) {
		MessageOne2MultiRequest request = new MessageOne2MultiRequest();
		request.setSource(source);
		request.setTargets(targets);
		request.setContent(content);
		request.setMessageType(IMessageItem.MESSAGE_TYPE_TEXT);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT, URL_MESSAGE_ONE2MULTI);

		System.out.println("url:------------" + url);

		return HttpUtil.PostJson(url, request, MessageOne2MultiResponse.class);
	}

	public void AckMessage(String msgId) {
		MessageACKRequest request = new MessageACKRequest();
		request.setMsgId(msgId);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT, URL_MESSAGE_ACK);

		HttpUtil.PostJson(url, request, MessageACKResponse.class);
	}

	public void SendMessageToFollowers(String content) {
		MessageOne2FollowersRequest request = new MessageOne2FollowersRequest();
		request.setSource(source);
		request.setContent(content);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT, URL_MESSAGE_ONE2FOLLOWERS);

		HttpUtil.PostJson(url, request, MessageOne2FollowersResponse.class);
	}

	public void SendMessageToFollowees(String content) {
		MessageOne2FolloweesRequest request = new MessageOne2FolloweesRequest();
		request.setSource(source);
		request.setContent(content);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_MESSAGE_ROOT, URL_MESSAGE_ONE2FOLLOWEES);

		HttpUtil.PostJson(url, request, MessageOne2FolloweesResponse.class);
	}

	public FileTransmissionResponse SendFile(String target, String filePath, int type) {
		File file = new File(filePath);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FILETRANSMISSION_ROOT,
				String.format(URL_FILETRANSMISSION_SEND, source, target, file.getName(), type));

		return HttpUtil.UploadFile(url, filePath, FileTransmissionResponse.class);
	}

	public FileMultiTransmissionResponse SendMultiFile(String targets, String filePath, int type) {
		File file = new File(filePath);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FILETRANSMISSION_ROOT,
				String.format(URL_FILEMULTITRANSMISSION_SEND, source, targets, file.getName(), type));

		return HttpUtil.UploadFile(url, filePath, FileMultiTransmissionResponse.class);
	}

	public void FetchFile(String token, String filePath) {

		try {
			InputStream input = FetchFile(token);
			if (null != input)
				FileUtil.SaveFile(filePath, input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public InputStream FetchFile(String token) {
		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FILETRANSMISSION_ROOT, URL_FILETRANSMISSION_FETCH);
		url = String.format(url, token);

		InputStream input = null;
		try {
			input = HttpUtil.DownloadFile(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return input;
	}

	public void Follow(String target) {
		FollowshipRequest request = new FollowshipRequest();
		request.setFollower(source);
		request.setFollowee(target);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT, URL_FOLLOWSHIP_FOLLOW);

		HttpUtil.PostJson(url, request, FollowshipRequest.class);
	}

	public void DropFollow(String target) {
		FollowshipRequest request = new FollowshipRequest();
		request.setFollower(source);
		request.setFollowee(target);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT, URL_FOLLOWSHIP_DROP);

		HttpUtil.PostJson(url, request, FollowshipResponse.class);
	}

	public List<String> GetFollowees() {
		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT,
				String.format(URL_FOLLOWSHIP_FOLLOWEES, source));

		FolloweesResponse resp = (FolloweesResponse) HttpUtil.GetJson(url, FolloweesResponse.class);

		return resp.getFollowees();
	}

	public List<String> GetFollowers() {
		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT,
				String.format(URL_FOLLOWSHIP_FOLLOWERS, source));

		FollowersResponse resp = (FollowersResponse) HttpUtil.GetJson(url, FollowersResponse.class);

		return resp.getFollowers();
	}

	public FollowshipAddedResponse FollowshipAdded(String shopId) {
		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT, URL_FOLLOWSHIP_ADDED);

		FollowshipAddedRequest request = new FollowshipAddedRequest();
		request.setMemberId(source);
		request.setShopId(shopId);

		FollowshipAddedResponse response = HttpUtil.PostJson(url, request, FollowshipAddedResponse.class);
		return response;
	}

	public FollowshipDroppedResponse FollowshipDropped(String shopId) {
		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT, URL_FOLLOWSHIP_DROPPED);

		FollowshipDroppedRequest request = new FollowshipDroppedRequest();
		request.setMemberId(source);
		request.setShopId(shopId);

		FollowshipDroppedResponse response = HttpUtil.PostJson(url, request, FollowshipDroppedResponse.class);
		return response;
	}

	public FollowshipInvitationResponse InviteFollowship(String driverId) {

		if (SelfMgr.getInstance().isDriver()) {
			throw new IllegalArgumentException("you are driver, could not invite invitation");
		}

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT, URL_FOLLOWSHIP_INVITATION);

		FollowshipInvitationRequest request = new FollowshipInvitationRequest();
		request.setMemberId(driverId);
		request.setShopId(source);

		FollowshipInvitationResponse response = HttpUtil.PostJson(url, request, FollowshipInvitationResponse.class);
		return response;
	}

	public FollowshipInvitationResultResponse FollowshipInvitationVerdict(String invitationId, boolean isAccepted) {
		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FOLLOWSHIP_ROOT, URL_FOLLOWSHIP_INVVERDICT);

		FollowshipInvitationResultRequest request = new FollowshipInvitationResultRequest();
		request.setInvitationId(invitationId);
		request.setIsAccepted(isAccepted);

		FollowshipInvitationResultResponse response = HttpUtil.PostJson(url, request,
				FollowshipInvitationResultResponse.class);
		return response;
	}

	public WakeupResponse Login(String id) {
		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_LOGIN_ROOT, URL_LOGIN_WAKEUP);

		WakeupRequest request = new WakeupRequest();
		request.setId(id);

		WakeupResponse response = HttpUtil.PostJson(url, request, WakeupResponse.class);
		return response;
	}

	public RangeResponse NearbyDrivers(double centerX, double centerY, int range) {
		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_LOCATE_ROOT, URL_LOCATE_RANGE);

		RangeRequest request = new RangeRequest();
		request.setCenterX(centerX);
		request.setCenterY(centerY);
		request.setRange(range);

		RangeResponse response = HttpUtil.PostJson(url, request, RangeResponse.class);
		return response;
	}

	public AddLocateResponse UpdateLocation(String id, double locationX, double locationY) {
		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_LOCATE_ROOT, URL_LOCATE_UPDATE);

		AddLocateRequest request = new AddLocateRequest();
		request.setId(id);
		request.setLocateX(locationX);
		request.setLocateY(locationY);

		AddLocateResponse response = HttpUtil.PostJson(url, request, AddLocateResponse.class);
		return response;
	}

	public CommentFileResponse UploadCommentImg(String filePath) {
		File file = new File(filePath);

		String url = URLUtil.UrlAppend(URL_SERVERROOT, URL_FILETRANSMISSION_ROOT,
				String.format(URL_FILETRANSMISSION_COMMENTIMG, file.getName()));

		return HttpUtil.UploadFile(url, filePath, CommentFileResponse.class);
	}
}
