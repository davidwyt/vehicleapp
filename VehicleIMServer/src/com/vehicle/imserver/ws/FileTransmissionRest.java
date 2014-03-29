package com.vehicle.imserver.ws;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

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

import com.vehicle.imserver.dao.bean.FileTransmission;
import com.vehicle.imserver.dao.bean.MessageType;
import com.vehicle.imserver.service.exception.FileTransmissionNotFoundException;
import com.vehicle.imserver.service.exception.PersistenceException;
import com.vehicle.imserver.service.exception.PushNotificationFailedException;
import com.vehicle.imserver.service.interfaces.FileTransmissionService;
import com.vehicle.imserver.utils.ErrorCodes;
import com.vehicle.imserver.utils.StringUtil;
import com.vehicle.service.bean.CommentFileResponse;
import com.vehicle.service.bean.FileAckRequest;
import com.vehicle.service.bean.FileAckResponse;
import com.vehicle.service.bean.FileFetchRequest;
import com.vehicle.service.bean.FileFetchResponse;
import com.vehicle.service.bean.FileMultiTransmissionRequest;
import com.vehicle.service.bean.FileMultiTransmissionResponse;
import com.vehicle.service.bean.FileTransmissionRequest;
import com.vehicle.service.bean.FileTransmissionResponse;

@Path("fileTransmission")
public class FileTransmissionRest {

	FileTransmissionService fileTransmissionService;

	public FileTransmissionService getFileTransmissionService() {
		return this.fileTransmissionService;
	}

	public void setFileTransmissionService(
			FileTransmissionService fileTransmissionService) {
		this.fileTransmissionService = fileTransmissionService;
	}

	@POST
	@Path("send/source={source}&&target={target}&&fileName={fileName}&&fileType={fileType}")
	@Consumes({ MediaType.APPLICATION_OCTET_STREAM,
			MediaType.MULTIPART_FORM_DATA })
	@Produces(MediaType.APPLICATION_JSON)
	public Response TransmitFile(@Context HttpServletRequest request,
			InputStream input, @PathParam("source") String source,
			@PathParam("target") String target,
			@PathParam("fileName") String fileName,
			@PathParam("fileType") int fileType) {

		FileTransmissionResponse resp = new FileTransmissionResponse();

		if (StringUtil.isEmptyOrNull(source)
				|| StringUtil.isEmptyOrNull(target)
				|| StringUtil.isEmptyOrNull(fileName) || null == input) {
			resp.setErrorCode(ErrorCodes.FILETRAN_INVALID_ERRCODE);
			resp.setErrorMsg(ErrorCodes.FILETRAN_INVALID_ERRMSG);

			return Response.status(Status.BAD_REQUEST).entity(resp).build();
		}

		if (fileType != MessageType.AUDIO.ordinal()
				&& fileType != MessageType.IMAGE.ordinal()) {
			resp.setErrorCode(ErrorCodes.FILETRAN_INVALIDTYPE_ERRCODE);
			resp.setErrorMsg(String.format(
					ErrorCodes.FILETRAN_INVALIDFILETYPE_ERRMSG, fileType));

			return Response.status(Status.BAD_REQUEST).entity(resp).build();
		}

		FileTransmissionRequest fileRequest = new FileTransmissionRequest();
		fileRequest.setSource(source);
		fileRequest.setTarget(target);
		fileRequest.setFileName(fileName);
		fileRequest.setMsgType(fileType);

		try {
			FileTransmission fileTran = fileTransmissionService.SendFile(
					fileRequest, input);
			resp.setSentTime(fileTran.getTransmissionTime());
			resp.setToken(fileTran.getToken());

			return Response.status(Status.OK).entity(resp).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.FILETRAN_FILESAVE_ERRCODE);
			resp.setErrorMsg(String.format(ErrorCodes.FILETRAN_FILESAVE_ERRMSG,
					fileName));

			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(resp)
					.build();
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

	@POST
	@Path("sendtomulti/source={source}&&targets={targets}&&fileName={fileName}&&fileType={fileType}")
	@Consumes({ MediaType.APPLICATION_OCTET_STREAM,
			MediaType.MULTIPART_FORM_DATA })
	@Produces(MediaType.APPLICATION_JSON)
	public Response TransmitFileToMulti(@Context HttpServletRequest request,
			InputStream input, @PathParam("source") String source,
			@PathParam("targets") String targets,
			@PathParam("fileName") String fileName,
			@PathParam("fileType") int fileType) {
		FileMultiTransmissionResponse resp = new FileMultiTransmissionResponse();

		if (StringUtil.isEmptyOrNull(source)
				|| StringUtil.isEmptyOrNull(targets)
				|| StringUtil.isEmptyOrNull(fileName) || null == input) {
			resp.setErrorCode(ErrorCodes.FILETRAN_INVALID_ERRCODE);
			resp.setErrorMsg(ErrorCodes.FILETRAN_INVALID_ERRMSG);

			return Response.status(Status.BAD_REQUEST).entity(resp).build();
		}

		if (fileType != MessageType.AUDIO.ordinal()
				&& fileType != MessageType.IMAGE.ordinal()) {
			resp.setErrorCode(ErrorCodes.FILETRAN_INVALIDTYPE_ERRCODE);
			resp.setErrorMsg(String.format(
					ErrorCodes.FILETRAN_INVALIDFILETYPE_ERRMSG, fileType));

			return Response.status(Status.BAD_REQUEST).entity(resp).build();
		}

		FileMultiTransmissionRequest fileRequest = new FileMultiTransmissionRequest();
		fileRequest.setSource(source);
		fileRequest.setTargets(targets);
		fileRequest.setFileName(fileName);
		fileRequest.setMsgType(fileType);

		try {
			fileTransmissionService.SendFile2Multi(fileRequest, input);
			resp.setSentTime(new Date().getTime());
			resp.setToken(UUID.randomUUID().toString());

			return Response.status(Status.OK).entity(resp).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.FILETRAN_FILESAVE_ERRCODE);
			resp.setErrorMsg(String.format(ErrorCodes.FILETRAN_FILESAVE_ERRMSG,
					fileName));

			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(resp)
					.build();
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
	@Path("fetch/{token}")
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

	@GET
	@Path("ack/{token}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response AckFile(@Context HttpServletRequest request,
			@PathParam("token") String token) {
		FileAckResponse resp = new FileAckResponse();

		if (StringUtil.isEmptyOrNull(token)) {
			resp.setErrorCode(ErrorCodes.FILEFETCH_TOKENNULL_ERRCODE);
			resp.setErrorMsg(ErrorCodes.FILEFETCH_TOKENNULL_ERRMSG);

			return Response.status(Status.BAD_REQUEST).entity(resp).build();
		}

		FileAckRequest fileReq = new FileAckRequest();
		fileReq.setToken(token);

		try {
			fileTransmissionService.AckFile(fileReq);
			return Response.status(Status.OK).entity(resp).build();
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
	
	@POST
	@Path("commentfile/fileName={fileName}")
	@Consumes({ MediaType.APPLICATION_OCTET_STREAM,
			MediaType.MULTIPART_FORM_DATA })
	@Produces(MediaType.APPLICATION_JSON)
	public Response TransmitCommentFile(@Context HttpServletRequest request,
			InputStream input, @PathParam("fileName") String fileName) {

		CommentFileResponse resp = new CommentFileResponse();

		if (StringUtil.isEmptyOrNull(fileName) || null == input) {
			resp.setErrorCode(ErrorCodes.FILETRAN_INVALID_ERRCODE);
			resp.setErrorMsg(ErrorCodes.FILETRAN_INVALID_ERRMSG);

			return Response.status(Status.BAD_REQUEST).entity(resp).build();
		}

		try {
			resp.setFileName(fileTransmissionService.SendCommentFile(input,
					fileName));
			return Response.status(Status.OK).entity(resp).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			resp.setErrorCode(ErrorCodes.UNKNOWN_ERROR_ERRCODE);
			resp.setErrorMsg(String.format(ErrorCodes.UNKNOWN_ERROR_ERRMSG,
					e.getMessage()));

			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(resp)
					.build();
		}
	}
}
