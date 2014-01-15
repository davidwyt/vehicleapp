package com.vehicle.imserver.service.api;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.vehicle.imserver.service.bean.FileTransmissionRequest;
import com.vehicle.imserver.service.bean.FileTransmissionResponse;
import com.vehicle.imserver.service.handler.FileTransmissionServiceHandler;
import com.vehicle.imserver.utils.ErrorCodes;
import com.vehicle.imserver.utils.StringUtil;

@Path("fileTransmission")
public class FileTransmissionRest {
	
	@Path("send?source={source}&&target={target}&&fileName={fileName}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response TransmissionFile(@Context HttpServletRequest request, InputStream input, @PathParam("source") String source, @PathParam("target") String target, @PathParam("fileName") String fileName)
	{
		FileTransmissionResponse resp = new FileTransmissionResponse();
		
		if(StringUtil.isEmptyOrNull(source) || StringUtil.isEmptyOrNull(target) || StringUtil.isEmptyOrNull(fileName) || null == input)
		{
			resp.setErrorCode(ErrorCodes.FILETRAN_INVALID_ERRCODE);
			resp.setErrorMsg(ErrorCodes.FILETRAN_INVALID_ERRMSG);
			
			return Response.status(Status.BAD_REQUEST).entity(resp).build();
		}
		
		FileTransmissionRequest fileRequest = new FileTransmissionRequest();
		fileRequest.setSource(source);
		fileRequest.setTarget(fileName);
		fileRequest.setFileName(fileName);
		
		try {
			FileTransmissionServiceHandler.SendFile(fileRequest, input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			resp.setErrorCode(ErrorCodes.FILETRAN_FILESAVE_ERRCODE);
			resp.setErrorMsg(String.format(ErrorCodes.FILETRAN_FILESAVE_ERRMSG, fileName));
			
			Response.status(Status.INTERNAL_SERVER_ERROR).entity(resp).build();
		}catch (Exception e) {
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.UNKNOWN_ERROR_ERRCODE);
			resp.setErrorMsg(String.format(ErrorCodes.UNKNOWN_ERROR_ERRMSG,
					e.getMessage()));

			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(resp)
					.build();
		}
		
		return Response.status(Status.OK).entity(resp).build();
	}
}
