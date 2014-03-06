package com.vehicle.app.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.vehicle.imserver.dao.bean.FollowshipInvitation;
import com.vehicle.imserver.dao.bean.Message;
import com.vehicle.service.bean.NewFileNotification;
import com.vehicle.service.bean.WakeupResponse;

public class WakeupResponseDeserializer implements JsonDeserializer<WakeupResponse> {

	@Override
	public WakeupResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
			throws JsonParseException {
		// TODO Auto-generated method stub

		System.out.println("in list deserializer");

		WakeupResponse resp = new WakeupResponse();

		JsonObject jsonObject = je.getAsJsonObject();

		JsonElement errCodeElement = jsonObject.get("errorCode");
		if (null != errCodeElement) {
			resp.setErrorCode(errCodeElement.getAsInt());
		}

		JsonElement errMsgElement = jsonObject.get("errorMsg");
		if (null != errMsgElement) {
			resp.setErrorMsg(errMsgElement.getAsString());
		}

		JsonElement invitationsElement = jsonObject.get("newInvitations");

		List<FollowshipInvitation> invitationList = null;

		if (null != invitationsElement) {
			if (invitationsElement.isJsonObject()) {

				invitationList = new ArrayList<FollowshipInvitation>();

				FollowshipInvitation invitation = jdc.deserialize(invitationsElement.getAsJsonObject(),
						FollowshipInvitation.class);

				invitationList.add(invitation);

			} else if (invitationsElement.isJsonArray()) {

				Type listType = new TypeToken<List<FollowshipInvitation>>() {
				}.getType();
				invitationList = jdc.deserialize(invitationsElement, listType);
			}
		}

		resp.setNewInvitations(invitationList);

		JsonElement msgsElement = jsonObject.get("newMessages");

		List<Message> msgList = null;

		if (null != msgsElement) {
			if (msgsElement.isJsonObject()) {

				msgList = new ArrayList<Message>();

				Message msg = jdc.deserialize(msgsElement.getAsJsonObject(), Message.class);

				msgList.add(msg);

			} else if (msgsElement.isJsonArray()) {

				Type listType = new TypeToken<List<Message>>() {
				}.getType();
				msgList = jdc.deserialize(msgsElement, listType);
			}
		}

		resp.setNewMessages(msgList);

		JsonElement filesElement = jsonObject.get("newFiles");

		List<NewFileNotification> fileList = null;

		if (null != filesElement) {
			if (filesElement.isJsonObject()) {

				fileList = new ArrayList<NewFileNotification>();

				NewFileNotification file = jdc.deserialize(filesElement.getAsJsonObject(), NewFileNotification.class);

				fileList.add(file);

			} else if (filesElement.isJsonArray()) {

				Type listType = new TypeToken<List<NewFileNotification>>() {
				}.getType();
				fileList = jdc.deserialize(filesElement, listType);
			}
		}

		resp.setNewFiles(fileList);

		return resp;
	}
}
