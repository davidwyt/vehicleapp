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
import com.vehicle.service.bean.RangeInfo;
import com.vehicle.service.bean.RangeResponse;

public class RangeResponseDeserializer implements JsonDeserializer<RangeResponse> {

	@Override
	public RangeResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
			throws JsonParseException {
		// TODO Auto-generated method stub

		System.out.println("in list deserializer");

		RangeResponse resp = new RangeResponse();

		JsonObject jsonObject = je.getAsJsonObject();

		JsonElement errCodeElement = jsonObject.get("errorCode");
		if (null != errCodeElement) {
			resp.setErrorCode(errCodeElement.getAsInt());
		}

		JsonElement errMsgElement = jsonObject.get("errorMsg");
		if (null != errMsgElement) {
			resp.setErrorMsg(errMsgElement.getAsString());
		}

		JsonElement infoListElement = jsonObject.get("list");

		List<RangeInfo> rangInfoList = null;

		if (null != infoListElement) {
			if (infoListElement.isJsonObject()) {

				System.out.println("in list deserializer objecttttttttttttt");

				rangInfoList = new ArrayList<RangeInfo>();

				RangeInfo inf = jdc.deserialize(infoListElement.getAsJsonObject(), RangeInfo.class);

				rangInfoList.add(inf);

			} else if (infoListElement.isJsonArray()) {

				Type listType = new TypeToken<List<RangeInfo>>() {
				}.getType();
				rangInfoList = jdc.deserialize(infoListElement, listType);
			}
		}

		resp.setList(rangInfoList);

		return resp;
	}
}
