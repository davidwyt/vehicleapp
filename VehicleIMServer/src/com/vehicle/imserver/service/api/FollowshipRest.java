package com.vehicle.imserver.service.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.vehicle.imserver.service.bean.FollowshipRequest;
import com.vehicle.imserver.service.bean.FollowshipResponse;
import com.vehicle.imserver.service.exception.FollowshipAlreadyExistException;
import com.vehicle.imserver.service.exception.FollowshipNotExistException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.handler.FollowshipServiceHandler;
import com.vehicle.imserver.utils.ErrorCodes;
import com.vehicle.imserver.utils.StringUtil;

@Path("followship")
public class FollowshipRest {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response AddFollowship(@Context HttpServletRequest request,
			FollowshipRequest followshipReq) {
		FollowshipResponse resp = new FollowshipResponse();
		if (null == followshipReq
				|| StringUtil.isEmptyOrNull(followshipReq.getFollower())
				|| StringUtil.isEmptyOrNull(followshipReq.getFollowee())) {
			resp.setErrorCode(ErrorCodes.FOLLOWSHIP_NULL_ERRCODE);
			resp.setErrorMsg(ErrorCodes.FOLLOWSHIP_NULL_ERRMSG);

			return Response.status(Status.BAD_REQUEST).entity(resp).build();
		}

		try {
			FollowshipServiceHandler.AddFollowship(followshipReq);

			return Response.status(Status.OK).entity(resp).build();
		} catch (FollowshipAlreadyExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.FOLLOWSHIP_ALREXIST_ERRCODE);
			resp.setErrorMsg(String.format(
					ErrorCodes.FOLLOWSHIP_ALREXIST_ERRMSG,
					followshipReq.getFollower(), followshipReq.getFollowee()));

			return Response.status(Status.BAD_REQUEST).entity(resp).build();
		} catch (PersistenceException e) {
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.MESSAGE_PERSISTENCE_ERRCODE);
			resp.setErrorMsg(String.format(
					ErrorCodes.MESSAGE_PERSISTENCE_ERRMSG, e.getMessage(),
					followshipReq.toString()));

			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(resp)
					.build();
		} catch (Exception e) {
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.UNKNOWN_ERROR_ERRCODE);
			resp.setErrorMsg(String.format(ErrorCodes.UNKNOWN_ERROR_ERRMSG,
					e.getMessage()));

			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(resp)
					.build();
		}
	}

	@POST
	@Path("drop")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response DropFollowship(@Context HttpServletRequest request,
			FollowshipRequest followshipReq) {
		FollowshipResponse resp = new FollowshipResponse();
		if (null == followshipReq
				|| StringUtil.isEmptyOrNull(followshipReq.getFollower())
				|| StringUtil.isEmptyOrNull(followshipReq.getFollowee())) {
			resp.setErrorCode(ErrorCodes.FOLLOWSHIP_NULL_ERRCODE);
			resp.setErrorMsg(ErrorCodes.FOLLOWSHIP_NULL_ERRMSG);

			return Response.status(Status.BAD_REQUEST).entity(resp).build();
		}

		try {
			FollowshipServiceHandler.DropFollowship(followshipReq);

			return Response.status(Status.OK).entity(resp).build();
		} catch (FollowshipNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.FOLLOWSHIP_NOTEXIST_ERRCODE);
			resp.setErrorMsg(String.format(
					ErrorCodes.FOLLOWSHIP_NOTEXIST_ERRMSG,
					followshipReq.getFollower(), followshipReq.getFollowee()));

			return Response.status(Status.NOT_FOUND).entity(resp).build();
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.MESSAGE_PERSISTENCE_ERRCODE);
			resp.setErrorMsg(String.format(
					ErrorCodes.MESSAGE_PERSISTENCE_ERRMSG, e.getMessage(),
					followshipReq.toString()));

			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(resp)
					.build();
		} catch (Exception e) {
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.UNKNOWN_ERROR_ERRCODE);
			resp.setErrorMsg(String.format(ErrorCodes.UNKNOWN_ERROR_ERRMSG,
					e.getMessage()));

			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(resp)
					.build();
		}

	}

}
