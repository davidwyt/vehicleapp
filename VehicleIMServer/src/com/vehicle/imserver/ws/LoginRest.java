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
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.interfaces.LoginService;
import com.vehicle.imserver.utils.ErrorCodes;
import com.vehicle.imserver.utils.StringUtil;
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
					.ShakeNewInvitations(loginRequest);
			loginResponse.setNewInvitations(newInvitations);

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
