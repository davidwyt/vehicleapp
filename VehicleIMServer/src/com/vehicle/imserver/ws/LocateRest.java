package com.vehicle.imserver.ws;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.vehicle.imserver.utils.LocateUtil;
import com.vehicle.service.bean.AddLocateRequest;
import com.vehicle.service.bean.AddLocateResponse;
import com.vehicle.service.bean.LocateInfo;
import com.vehicle.service.bean.OfflineAckRequest;
import com.vehicle.service.bean.RangeRequest;
import com.vehicle.service.bean.RangeResponse;

@Path("locate")
public class LocateRest {
	
	@POST
	@Path("range")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response range(@Context HttpServletRequest request,
			RangeRequest rangeReq) {
		List<LocateInfo> list=LocateUtil.getInstance().getByRange(rangeReq.getStartX(), rangeReq.getStartY(), rangeReq.getEndX(), rangeReq.getEndY());
		RangeResponse resp=new RangeResponse();
		resp.setList(list);
		return Response.status(Status.OK).entity(resp).build();
	}
	
	@POST
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(@Context HttpServletRequest request,
			AddLocateRequest addReq) {
		AddLocateResponse resp=new AddLocateResponse();
		LocateInfo info=new LocateInfo();
		info.setLocateX(addReq.getLocateX());
		info.setLocateY(addReq.getLocateY());
		info.setOwnerId(addReq.getId());
		info.setTime(new Date());
		LocateUtil.getInstance().add(info);
		return Response.status(Status.OK).entity(resp).build();
	}

}