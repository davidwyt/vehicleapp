package com.vehicle.imserver.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import com.vehicle.imserver.service.bean.FolloweesRequest;
import com.vehicle.imserver.service.bean.FolloweesResponse;
import com.vehicle.imserver.service.bean.FollowersRequest;
import com.vehicle.imserver.service.bean.FollowersResponse;
import com.vehicle.imserver.service.bean.FollowshipRequest;
import com.vehicle.imserver.service.bean.FollowshipResponse;
import com.vehicle.imserver.service.exception.FollowshipAlreadyExistException;
import com.vehicle.imserver.service.exception.FollowshipNotExistException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.interfaces.FollowshipService;
import com.vehicle.imserver.utils.ErrorCodes;
import com.vehicle.imserver.utils.StringUtil;

@Path("followship")
public class FollowshipRest {
	
	private FollowshipService followshipService;
	
	public FollowshipService getFollowshipService(){
		return this.followshipService;
	}
	
	public void setFollowshipService(FollowshipService followshipService){
		this.followshipService=followshipService;
	}

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
			followshipService.AddFollowship(followshipReq);

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
			followshipService.DropFollowship(followshipReq);

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

	@GET
	@Path("followees/{follower}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetFollowees(@Context HttpServletRequest request, @PathParam("follower") String follower) {
		
		FolloweesRequest followeesReq = new FolloweesRequest();
		followeesReq.setFollower(follower);
		
		FolloweesResponse resp = new FolloweesResponse();

		if (null == followeesReq
				|| StringUtil.isEmptyOrNull(followeesReq.getFollower())) {
			resp.setErrorCode(ErrorCodes.FOLLOWSHIP_FOLLOWERNULL_ERRCODE);
			resp.setErrorMsg(ErrorCodes.FOLLOWSHIP_FOLLOWERNULL_ERRMSG);

			return Response.status(Status.BAD_REQUEST).entity(resp).build();
		}

		resp.setFollower(followeesReq.getFollower());

		try {
			List<String> followees = followshipService
					.GetFollowees(followeesReq);
			resp.setFollowees(followees);
			return Response.status(Status.OK).entity(resp).build();
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.MESSAGE_PERSISTENCE_ERRCODE);
			resp.setErrorMsg(String.format(
					ErrorCodes.MESSAGE_PERSISTENCE_ERRMSG, e.getMessage(),
					followeesReq.toString()));

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
	
	@GET
	@Path("followers/{followee}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetFollowers(@Context HttpServletRequest request, @PathParam("followee") String followee) {
		
		FollowersRequest followersReq = new FollowersRequest();
		followersReq.setFollowee(followee);
		
		FollowersResponse resp = new FollowersResponse();

		if (null == followersReq
				|| StringUtil.isEmptyOrNull(followersReq.getFollowee())) {
			resp.setErrorCode(ErrorCodes.FOLLOWSHIP_FOLLOWEENULL_ERRCODE);
			resp.setErrorMsg(ErrorCodes.FOLLOWSHIP_FOLLOWEENULL_ERRMSG);

			return Response.status(Status.BAD_REQUEST).entity(resp).build();
		}

		resp.setFollowee(followersReq.getFollowee());

		try {
			List<String> followers = followshipService
					.GetFollowers(followersReq);
			resp.setFollowers(followers);
			return Response.status(Status.OK).entity(resp).build();
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.MESSAGE_PERSISTENCE_ERRCODE);
			resp.setErrorMsg(String.format(
					ErrorCodes.MESSAGE_PERSISTENCE_ERRMSG, e.getMessage(),
					followersReq.toString()));

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
