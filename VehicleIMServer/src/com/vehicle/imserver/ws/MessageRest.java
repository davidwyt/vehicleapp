package com.vehicle.imserver.ws;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.exception.PushMessageFailedException;
import com.vehicle.imserver.service.interfaces.MessageService;
import com.vehicle.imserver.utils.ErrorCodes;
import com.vehicle.imserver.utils.StringUtil;
import com.vehicle.service.bean.MessageOne2FolloweesRequest;
import com.vehicle.service.bean.MessageOne2FolloweesResponse;
import com.vehicle.service.bean.MessageOne2FollowersRequest;
import com.vehicle.service.bean.MessageOne2FollowersResponse;
import com.vehicle.service.bean.MessageOne2MultiRequest;
import com.vehicle.service.bean.MessageOne2MultiResponse;
import com.vehicle.service.bean.MessageOne2OneRequest;
import com.vehicle.service.bean.MessageOne2OneResponse;
import com.vehicle.service.bean.OfflineAckRequest;
import com.vehicle.service.bean.OfflineAckResponse;
import com.vehicle.service.bean.OfflineMessageRequest;
import com.vehicle.service.bean.OfflineMessageResponse;
import com.vehicle.service.bean.RespMessage;

@Path("message")
public class MessageRest {

	MessageService messageService;

	public MessageService getMessageService() {
		return this.messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	@POST
	@Path("offlineAck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response offlineAck(@Context HttpServletRequest request,
			OfflineAckRequest omReq) {
		OfflineAckResponse oar = new OfflineAckResponse();
		if (null == omReq || StringUtil.isEmptyOrNull(omReq.getId())) {
			oar.setErrorCode(ErrorCodes.MESSAGE_INVALID_ERRCODE);
			oar.setErrorMsg(String.format(ErrorCodes.MESSAGE_INVALID_ERRMSG));
			return Response.status(Status.BAD_REQUEST).entity(oar).build();
		}
		try {
			this.messageService.offlineAck(omReq);
			return Response.status(Status.OK).entity(oar).build();
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			oar.setErrorCode(ErrorCodes.MESSAGE_PERSISTENCE_ERRCODE);
			oar.setErrorMsg(String.format(
					ErrorCodes.MESSAGE_PERSISTENCE_ERRMSG, e.getMessage(),
					omReq.toString()));

			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(oar)
					.build();
		}

	}

	@POST
	@Path("offline")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response SendOfflineMsg(@Context HttpServletRequest request,
			OfflineMessageRequest omReq) {
		OfflineMessageResponse omr = new OfflineMessageResponse();
		if (null == omReq || StringUtil.isEmptyOrNull(omReq.getTarget())) {
			omr.setErrorCode(ErrorCodes.MESSAGE_INVALID_ERRCODE);
			omr.setErrorMsg(String.format(ErrorCodes.MESSAGE_INVALID_ERRMSG));
			return Response.status(Status.BAD_REQUEST).entity(omr).build();
		}
		try {
			List<RespMessage> list = this.messageService.sendOfflineMsgs(omReq);
			omr.setMessages(list);
			return Response.status(Status.OK).entity(omr).build();
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			omr.setErrorCode(ErrorCodes.MESSAGE_PERSISTENCE_ERRCODE);
			omr.setErrorMsg(String.format(
					ErrorCodes.MESSAGE_PERSISTENCE_ERRMSG, e.getMessage(),
					omReq.toString()));

			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(omr)
					.build();
		}

	}

	@POST
	@Path("one2one")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response SendMessage(@Context HttpServletRequest request,
			MessageOne2OneRequest msgRequest) {

		MessageOne2OneResponse msgResp = new MessageOne2OneResponse();

		if (null == msgRequest
				|| StringUtil.isEmptyOrNull(msgRequest.getSource())
				|| StringUtil.isEmptyOrNull(msgRequest.getTarget())
				|| StringUtil.isEmptyOrNull(msgRequest.getContent())) {

			msgResp.setErrorCode(ErrorCodes.MESSAGE_INVALID_ERRCODE);
			msgResp.setErrorMsg(String
					.format(ErrorCodes.MESSAGE_INVALID_ERRMSG));

			return Response.status(Status.BAD_REQUEST).entity(msgResp).build();
		}

		try {
			Message msg = messageService.SendMessage(msgRequest);
			msgResp.setMsgId(msg.getId());
			msgResp.setMsgSentTime(msg.getSentTime());

			return Response.status(Status.OK).entity(msgResp).build();
		} catch (PersistenceException e) {
			e.printStackTrace();

			msgResp.setErrorCode(ErrorCodes.MESSAGE_PERSISTENCE_ERRCODE);
			msgResp.setErrorMsg(String.format(
					ErrorCodes.MESSAGE_PERSISTENCE_ERRMSG, e.getMessage(),
					msgRequest.toString()));

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(msgResp).build();
		} catch (PushMessageFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			msgResp.setErrorCode(ErrorCodes.MESSAGE_JPUSH_ERRCODE);
			msgResp.setErrorMsg(String.format(ErrorCodes.MESSAGE_JPUSH_ERRMSG,
					e.getMessage(), msgRequest.toString()));

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(msgResp).build();
		} catch (Exception e) {
			e.printStackTrace();

			msgResp.setErrorCode(ErrorCodes.UNKNOWN_ERROR_ERRCODE);
			msgResp.setErrorMsg(String.format(ErrorCodes.UNKNOWN_ERROR_ERRMSG,
					e.getMessage()));

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(msgResp).build();
		}

	}

	@POST
	@Path("one2multi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response SendMessage2Multie(@Context HttpServletRequest request,
			MessageOne2MultiRequest msgRequest) {
		MessageOne2MultiResponse msgResp = new MessageOne2MultiResponse();
		if (null == msgRequest
				|| StringUtil.isEmptyOrNull(msgRequest.getSource())
				|| StringUtil.isEmptyOrNull(msgRequest.getContent())) {
			msgResp.setErrorCode(ErrorCodes.MESSAGE_INVALID_ERRCODE);
			msgResp.setErrorMsg(String
					.format(ErrorCodes.MESSAGE_INVALID_ERRMSG));

			return Response.status(Status.BAD_REQUEST).entity(msgResp).build();
		}
		try {
			msgResp.setMsgSentTime(new Date().getTime());
			messageService.sendMessage2Multi(msgRequest);
			return Response.status(Status.OK).entity(msgResp).build();
		} catch (PersistenceException e) {
			e.printStackTrace();

			msgResp.setErrorCode(ErrorCodes.MESSAGE_PERSISTENCE_ERRCODE);
			msgResp.setErrorMsg(String.format(
					ErrorCodes.MESSAGE_PERSISTENCE_ERRMSG, e.getMessage(),
					msgRequest.toString()));

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(msgResp).build();
		} catch (PushMessageFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			msgResp.setErrorCode(ErrorCodes.MESSAGE_JPUSH_ERRCODE);
			msgResp.setErrorMsg(String.format(ErrorCodes.MESSAGE_JPUSH_ERRMSG,
					e.getMessage(), msgRequest.toString()));

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(msgResp).build();
		} catch (Exception e) {
			e.printStackTrace();

			msgResp.setErrorCode(ErrorCodes.UNKNOWN_ERROR_ERRCODE);
			msgResp.setErrorMsg(String.format(ErrorCodes.UNKNOWN_ERROR_ERRMSG,
					e.getMessage()));

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(msgResp).build();
		}
	}

	@POST
	@Path("one2followees")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response SendMessage2Followees(@Context HttpServletRequest request,
			MessageOne2FolloweesRequest msgRequest) {
		MessageOne2FolloweesResponse msgResp = new MessageOne2FolloweesResponse();

		if (null == msgRequest
				|| StringUtil.isEmptyOrNull(msgRequest.getSource())
				|| StringUtil.isEmptyOrNull(msgRequest.getContent())) {
			msgResp.setErrorCode(ErrorCodes.MESSAGE_INVALID_ERRCODE);
			msgResp.setErrorMsg(String
					.format(ErrorCodes.MESSAGE_INVALID_ERRMSG));

			return Response.status(Status.BAD_REQUEST).entity(msgResp).build();
		}

		try {
			messageService.SendMessage2Followees(msgRequest);
			return Response.status(Status.OK).entity(msgResp).build();
		} catch (PersistenceException e) {
			e.printStackTrace();

			msgResp.setErrorCode(ErrorCodes.MESSAGE_PERSISTENCE_ERRCODE);
			msgResp.setErrorMsg(String.format(
					ErrorCodes.MESSAGE_PERSISTENCE_ERRMSG, e.getMessage(),
					msgRequest.toString()));

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(msgResp).build();
		} catch (PushMessageFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			msgResp.setErrorCode(ErrorCodes.MESSAGE_JPUSH_ERRCODE);
			msgResp.setErrorMsg(String.format(ErrorCodes.MESSAGE_JPUSH_ERRMSG,
					e.getMessage(), msgRequest.toString()));

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(msgResp).build();
		} catch (Exception e) {
			e.printStackTrace();

			msgResp.setErrorCode(ErrorCodes.UNKNOWN_ERROR_ERRCODE);
			msgResp.setErrorMsg(String.format(ErrorCodes.UNKNOWN_ERROR_ERRMSG,
					e.getMessage()));

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(msgResp).build();
		}
	}

	@POST
	@Path("one2followers")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response SendMessage2Followers(@Context HttpServletRequest request,
			MessageOne2FollowersRequest msgRequest) {
		MessageOne2FollowersResponse msgResp = new MessageOne2FollowersResponse();

		if (null == msgRequest
				|| StringUtil.isEmptyOrNull(msgRequest.getSource())
				|| StringUtil.isEmptyOrNull(msgRequest.getContent())) {
			msgResp.setErrorCode(ErrorCodes.MESSAGE_INVALID_ERRCODE);
			msgResp.setErrorMsg(String
					.format(ErrorCodes.MESSAGE_INVALID_ERRMSG));

			return Response.status(Status.BAD_REQUEST).entity(msgResp).build();
		}

		try {
			messageService.SendMessage2Followers(msgRequest);
			return Response.status(Status.OK).entity(msgResp).build();
		} catch (PersistenceException e) {
			e.printStackTrace();

			msgResp.setErrorCode(ErrorCodes.MESSAGE_PERSISTENCE_ERRCODE);
			msgResp.setErrorMsg(String.format(
					ErrorCodes.MESSAGE_PERSISTENCE_ERRMSG, e.getMessage(),
					msgRequest.toString()));

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(msgResp).build();
		} catch (PushMessageFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			msgResp.setErrorCode(ErrorCodes.MESSAGE_JPUSH_ERRCODE);
			msgResp.setErrorMsg(String.format(ErrorCodes.MESSAGE_JPUSH_ERRMSG,
					e.getMessage(), msgRequest.toString()));

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(msgResp).build();
		} catch (Exception e) {

			e.printStackTrace();

			msgResp.setErrorCode(ErrorCodes.UNKNOWN_ERROR_ERRCODE);
			msgResp.setErrorMsg(String.format(ErrorCodes.UNKNOWN_ERROR_ERRMSG,
					e.getMessage()));

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(msgResp).build();
		}
	}
}
