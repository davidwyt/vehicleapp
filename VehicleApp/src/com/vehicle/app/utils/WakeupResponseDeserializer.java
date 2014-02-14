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

				System.out.println("in list deserializer objecttttttttttttt");

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

		return resp;
	}
}
