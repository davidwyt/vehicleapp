package com.vehicle.imserver.service.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.vehicle.imserver.service.bean.MessageSendingRequest;
import com.vehicle.imserver.service.bean.MessageSendingResponse;
import com.vehicle.imserver.service.handler.MessageServiceHandler;
import com.vehicle.imserver.utils.StringUtil;

@Path("message")
public class MessageRest {

	@POST
	@Path("send")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response SendMessage(@Context HttpServletRequest request,
			MessageSendingRequest msgRequest) {
		if (null == msgRequest
				|| StringUtil.isEmptyOrNull(msgRequest.getSource())
				|| StringUtil.isEmptyOrNull(msgRequest.getTarget())) {
			return Response.status(Status.NOT_FOUND).build();
		}

		try {
			MessageSendingResponse msgResp = MessageServiceHandler
					.SendMessage(msgRequest);
			return Response.status(Status.OK).entity(msgResp).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
}
