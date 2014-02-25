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
import com.vehicle.service.bean.OfflineMessageResponse;
import com.vehicle.service.bean.RespMessage;

public class OffMessageResponseDeserializer implements JsonDeserializer<OfflineMessageResponse> {

	@Override
	public OfflineMessageResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
			throws JsonParseException {
		// TODO Auto-generated method stub

		System.out.println("in list deserializer");

		OfflineMessageResponse resp = new OfflineMessageResponse();

		JsonObject jsonObject = je.getAsJsonObject();

		JsonElement errCodeElement = jsonObject.get("errorCode");
		if (null != errCodeElement) {
			resp.setErrorCode(errCodeElement.getAsInt());
		}

		JsonElement errMsgElement = jsonObject.get("errorMsg");
		if (null != errMsgElement) {
			resp.setErrorMsg(errMsgElement.getAsString());
		}

		JsonElement infoListElement = jsonObject.get("messages");

		List<RespMessage> messages = null;

		if (null != infoListElement) {
			if (infoListElement.isJsonObject()) {

				System.out.println("in list deserializer objecttttttttttttt");

				messages = new ArrayList<RespMessage>();

				RespMessage inf = jdc.deserialize(infoListElement.getAsJsonObject(), RespMessage.class);

				messages.add(inf);

			} else if (infoListElement.isJsonArray()) {

				Type listType = new TypeToken<List<RespMessage>>() {
				}.getType();
				messages = jdc.deserialize(infoListElement, listType);
			}
		}

		resp.setMessages(messages);

		return resp;
	}
}
