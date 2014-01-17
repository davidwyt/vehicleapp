package com.vehicle.imserver.ws;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.vehicle.imserver.service.bean.MessageACKRequest;
import com.vehicle.imserver.service.bean.MessageACKResponse;
import com.vehicle.imserver.service.bean.One2FolloweesMessageRequest;
import com.vehicle.imserver.service.bean.One2FolloweesMessageResponse;
import com.vehicle.imserver.service.bean.One2FollowersMessageRequest;
import com.vehicle.imserver.service.bean.One2FollowersMessageResponse;
import com.vehicle.imserver.service.bean.One2OneMessageRequest;
import com.vehicle.imserver.service.bean.One2OneMessageResponse;
import com.vehicle.imserver.service.exception.JPushException;
import com.vehicle.imserver.service.exception.MessageNotFoundException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.interfaces.MessageService;
import com.vehicle.imserver.utils.ErrorCodes;
import com.vehicle.imserver.utils.StringUtil;

@Path("message")
public class MessageRest {

	MessageService messageService;
	
	public MessageService getMessageService(){
		return this.messageService;
	}
	
	public void setMessageService(MessageService messageService){
		this.messageService=messageService;
	}
	
	@POST
	@Path("one2one")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response SendMessage(@Context HttpServletRequest request,
			One2OneMessageRequest msgRequest) {

		One2OneMessageResponse msgResp = new One2OneMessageResponse();

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
			String msgId = messageService.SendMessage(msgRequest);
			msgResp.setMsgId(msgId);
			
			return Response.status(Status.OK).entity(msgResp).build();
		} catch (PersistenceException e) {
			e.printStackTrace();
			
			msgResp.setErrorCode(ErrorCodes.MESSAGE_PERSISTENCE_ERRCODE);
			msgResp.setErrorMsg(String.format(
					ErrorCodes.MESSAGE_PERSISTENCE_ERRMSG, e.getMessage(),
					msgRequest.toString()));
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(msgResp).build();
		} catch (JPushException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			msgResp.setErrorCode(ErrorCodes.MESSAGE_JPUSH_ERRCODE);
			msgResp.setErrorMsg(String.format(ErrorCodes.MESSAGE_JPUSH_ERRMSG,
					e.getMessage(), msgRequest.toString()));
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(msgResp).build();
		}catch(Exception e)
		{
			e.printStackTrace();
			
			msgResp.setErrorCode(ErrorCodes.UNKNOWN_ERROR_ERRCODE);
			msgResp.setErrorMsg(String.format(ErrorCodes.UNKNOWN_ERROR_ERRMSG, e.getMessage()));
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(msgResp).build();
		}
	}

	@POST
	@Path("ack")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response MessageReceived(@Context HttpServletRequest request,
			MessageACKRequest msgACKReq) {

		MessageACKResponse msgResp = new MessageACKResponse();

		if (null == msgACKReq || StringUtil.isEmptyOrNull(msgACKReq.getMsgId())) {
			msgResp.setErrorCode(ErrorCodes.MESSAGEID_NULL_ERRCODE);
			msgResp.setErrorMsg(ErrorCodes.MESSAGEID_NULL_ERRMSG);
			
			return Response.status(Status.BAD_REQUEST).entity(msgResp).build();
		}

		msgResp.setMsgId(msgACKReq.getMsgId());

		try {
			messageService.MessageReceived(msgACKReq);
			
			return Response.status(Status.OK).entity(msgResp).build();
		} catch (MessageNotFoundException e) {
			e.printStackTrace();

			msgResp.setErrorCode(ErrorCodes.MESSAGE_NOT_FOUND_ERRCODE);
			msgResp.setErrorMsg(String.format(
					ErrorCodes.MESSAGE_NOT_FOUND_ERRMSG, msgACKReq.getMsgId()));
			
			return Response.status(Status.NOT_FOUND).entity(msgResp).build();
		}catch (PersistenceException e) {
			e.printStackTrace();
			
			msgResp.setErrorCode(ErrorCodes.MESSAGE_PERSISTENCE_ERRCODE);
			msgResp.setErrorMsg(String.format(
					ErrorCodes.MESSAGE_PERSISTENCE_ERRMSG, e.getMessage(),
					msgACKReq.toString()));
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(msgResp).build();
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
	public Response SendMessage2Followees(@Context HttpServletRequest request, One2FolloweesMessageRequest msgRequest)
	{
		One2FolloweesMessageResponse msgResp = new One2FolloweesMessageResponse();
		
		if(null == msgRequest || StringUtil.isEmptyOrNull(msgRequest.getSource()) || StringUtil.isEmptyOrNull(msgRequest.getContent()))
		{
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
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(msgResp).build();
		} catch (JPushException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			msgResp.setErrorCode(ErrorCodes.MESSAGE_JPUSH_ERRCODE);
			msgResp.setErrorMsg(String.format(ErrorCodes.MESSAGE_JPUSH_ERRMSG,
					e.getMessage(), msgRequest.toString()));
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(msgResp).build();
		}catch(Exception e)
		{
			e.printStackTrace();
			
			msgResp.setErrorCode(ErrorCodes.UNKNOWN_ERROR_ERRCODE);
			msgResp.setErrorMsg(String.format(ErrorCodes.UNKNOWN_ERROR_ERRMSG, e.getMessage()));
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(msgResp).build();
		}
	}
	
	@POST
	@Path("one2followers")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response SendMessage2Followers(@Context HttpServletRequest request, One2FollowersMessageRequest msgRequest)
	{
		One2FollowersMessageResponse msgResp = new One2FollowersMessageResponse();
		
		if(null == msgRequest || StringUtil.isEmptyOrNull(msgRequest.getSource()) || StringUtil.isEmptyOrNull(msgRequest.getContent()))
		{
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
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(msgResp).build();
		} catch (JPushException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			msgResp.setErrorCode(ErrorCodes.MESSAGE_JPUSH_ERRCODE);
			msgResp.setErrorMsg(String.format(ErrorCodes.MESSAGE_JPUSH_ERRMSG,
					e.getMessage(), msgRequest.toString()));
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(msgResp).build();
		}catch(Exception e)
		{
			e.printStackTrace();
			
			msgResp.setErrorCode(ErrorCodes.UNKNOWN_ERROR_ERRCODE);
			msgResp.setErrorMsg(String.format(ErrorCodes.UNKNOWN_ERROR_ERRMSG, e.getMessage()));
			
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(msgResp).build();
		}
	}
}
