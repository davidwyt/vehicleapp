package com.vehicle.imserver.ws;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.activation.MimetypesFileTypeMap;
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

import com.vehicle.imserver.service.bean.FileFetchRequest;
import com.vehicle.imserver.service.bean.FileFetchResponse;
import com.vehicle.imserver.service.bean.FileTransmissionRequest;
import com.vehicle.imserver.service.bean.FileTransmissionResponse;
import com.vehicle.imserver.service.exception.FileTransmissionNotFoundException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.exception.PushNotificationFailedException;
import com.vehicle.imserver.service.interfaces.FileTransmissionService;
import com.vehicle.imserver.utils.ErrorCodes;
import com.vehicle.imserver.utils.StringUtil;

@Path("fileTransmission")
public class FileTransmissionRest {
	
	FileTransmissionService fileTransmissionService;
	
	public FileTransmissionService getFileTransmissionService(){
		return this.fileTransmissionService;
	}
	
	public void setFileTransmissionService(FileTransmissionService fileTransmissionService){
		this.fileTransmissionService=fileTransmissionService;
	}

	@POST
	@Path("/send/source={source}&&target={target}&&fileName={fileName}")
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	@Produces(MediaType.APPLICATION_JSON)
	public Response TransmitFile(@Context HttpServletRequest request,
			InputStream input, @PathParam("source") String source,
			@PathParam("target") String target,
			@PathParam("fileName") String fileName) {
		FileTransmissionResponse resp = new FileTransmissionResponse();
		
		if (StringUtil.isEmptyOrNull(source)
				|| StringUtil.isEmptyOrNull(target)
				|| StringUtil.isEmptyOrNull(fileName) || null == input) {
			resp.setErrorCode(ErrorCodes.FILETRAN_INVALID_ERRCODE);
			resp.setErrorMsg(ErrorCodes.FILETRAN_INVALID_ERRMSG);

			return Response.status(Status.BAD_REQUEST).entity(resp).build();
		}

		FileTransmissionRequest fileRequest = new FileTransmissionRequest();
		fileRequest.setSource(source);
		fileRequest.setTarget(target);
		fileRequest.setFileName(fileName);

		try {
			fileTransmissionService.SendFile(fileRequest, input);
			
			return Response.status(Status.OK).entity(resp).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.FILETRAN_FILESAVE_ERRCODE);
			resp.setErrorMsg(String.format(ErrorCodes.FILETRAN_FILESAVE_ERRMSG,
					fileName));

			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(resp).build();
		} catch (PushNotificationFailedException e) {
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.NOTIFICATION_PUSHFAILED_ERRCODE);
			resp.setErrorMsg(String.format(
					ErrorCodes.NOTIFICATION_PUSHFAILED_ERRMSG, "new file",
					e.getMessage()));

			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(resp)
					.build();
		} catch (PersistenceException e) {
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.MESSAGE_PERSISTENCE_ERRCODE);
			resp.setErrorMsg(String.format(
					ErrorCodes.MESSAGE_PERSISTENCE_ERRMSG, e.getMessage(),
					fileRequest.toString()));

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
	@Path("/fetch/{token}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response FetchFile(@Context HttpServletRequest request,
			@PathParam("token") String token) {
		FileFetchResponse resp = new FileFetchResponse();

		if (StringUtil.isEmptyOrNull(token)) {
			resp.setErrorCode(ErrorCodes.FILEFETCH_TOKENNULL_ERRCODE);
			resp.setErrorMsg(ErrorCodes.FILEFETCH_TOKENNULL_ERRMSG);

			return Response.status(Status.BAD_REQUEST).entity(resp).build();
		}
		
		FileFetchRequest fileReq = new FileFetchRequest();
		fileReq.setToken(token);

		try {
			String path = fileTransmissionService.FetchFile(fileReq);

			File file = new File(path);
			if (file.exists()) {
				String mt = new MimetypesFileTypeMap().getContentType(file);
				
				return Response.ok(file, mt).build();
			} else {
				resp.setErrorCode(ErrorCodes.FILEFETCH_NOTFOUND_ERRCODE);
				resp.setErrorMsg(String.format(
						ErrorCodes.FILEFETCH_NOTFOUND_ERRMSG, path));
				
				return Response.status(Status.NOT_FOUND).entity(resp).build();
			}
		} catch (FileTransmissionNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.FILEFETCH_TOKENINVALID_ERRCODE);
			resp.setErrorMsg(String.format(
					ErrorCodes.FILEFETCH_TOKENINVALID_ERRMSG, token));

			return Response.status(Status.NOT_FOUND).entity(resp).build();
		} catch (PersistenceException e) {
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.MESSAGE_PERSISTENCE_ERRCODE);
			resp.setErrorMsg(String.format(
					ErrorCodes.MESSAGE_PERSISTENCE_ERRMSG, e.getMessage(),
					"FileTransmission"));

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
