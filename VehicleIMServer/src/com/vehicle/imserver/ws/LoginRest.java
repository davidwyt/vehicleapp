package com.vehicle.imserver.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.vehicle.imserver.dao.bean.FollowshipInvitation;
import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.interfaces.LoginService;
import com.vehicle.imserver.utils.ErrorCodes;
import com.vehicle.imserver.utils.JsonUtil;
import com.vehicle.imserver.utils.StringUtil;
import com.vehicle.service.bean.ACKAllRequest;
import com.vehicle.service.bean.ACKAllResponse;
import com.vehicle.service.bean.NewFileNotification;
import com.vehicle.service.bean.WakeupRequest;
import com.vehicle.service.bean.WakeupResponse;

@Path("login")
public class LoginRest {

	private LoginService loginService;

	public LoginService getLoginService() {
		return this.loginService;
	}

	public void setLoginService(LoginService service) {
		this.loginService = service;
	}

	@POST
	@Path("ackall")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ackAll(@Context HttpServletRequest request,
			ACKAllRequest ackRequest) {
		ACKAllResponse ackResponse = new ACKAllResponse();
		if (null == ackRequest
				|| StringUtil.isEmptyOrNull(ackRequest.getMemberId())) {
			ackResponse.setErrorCode(ErrorCodes.ACKALL_INVALID_ERRORCODE);
			ackResponse.setErrorMsg(String
					.format(ErrorCodes.ACKALL_INVALID_ERRMSG));

			return Response.status(Status.BAD_REQUEST).entity(ackResponse)
					.build();
		}

		try {
			this.loginService.updateAllNewTextAndFileMessage(ackRequest
					.getMemberId());

			return Response.status(Status.OK).entity(ackResponse).build();
		} catch (Exception e) {
			e.printStackTrace();
			ackResponse.setErrorCode(ErrorCodes.UNKNOWN_ERROR_ERRCODE);
			ackResponse.setErrorMsg(String
					.format(ErrorCodes.UNKNOWN_ERROR_ERRMSG));

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(ackResponse).build();
		}
	}

	@POST
	@Path("wakeup")
	@Consumes("application/json")
	@Produces("application/json")
	public Response login(@Context HttpServletRequest request,
			WakeupRequest loginRequest) {

		System.out.println("in login request");

		WakeupResponse loginResponse = new WakeupResponse();

		if (null == loginRequest
				|| StringUtil.isEmptyOrNull(loginRequest.getId())) {
			loginResponse.setErrorCode(ErrorCodes.LOGIN_INVALID_ERRORCODE);
			loginResponse.setErrorMsg(String
					.format(ErrorCodes.LOGIN_INVALID_ERRMSG));

			return Response.status(Status.BAD_REQUEST).entity(loginResponse)
					.build();
		}

		try {
			List<FollowshipInvitation> newInvitations = this.loginService
					.GetNewInvitations(loginRequest);
			loginResponse.setNewInvitations(newInvitations);

			List<NewFileNotification> newFiles = this.loginService
					.GetNewFiles(loginRequest);
			loginResponse.setNewFiles(newFiles);

			List<Message> newMsgs = this.loginService
					.GetNewMessages(loginRequest);
			loginResponse.setNewMessages(newMsgs);

			System.out.println(JsonUtil.toJsonString(loginResponse));

			return Response.status(Status.OK).entity(loginResponse).build();
		} catch (PersistenceException e) {
			e.printStackTrace();

			loginResponse.setErrorCode(ErrorCodes.MESSAGE_PERSISTENCE_ERRCODE);
			loginResponse.setErrorMsg(String.format(
					ErrorCodes.MESSAGE_PERSISTENCE_ERRMSG, e.getMessage(),
					loginRequest.toString()));

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(loginResponse).build();

		} catch (Exception e) {

			e.printStackTrace();

			loginResponse.setErrorCode(ErrorCodes.UNKNOWN_ERROR_ERRCODE);
			loginResponse.setErrorMsg(String.format(
					ErrorCodes.UNKNOWN_ERROR_ERRMSG, e.getMessage()));

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(loginResponse).build();
		}
	}

}
