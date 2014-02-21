package com.vehicle.app.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vehicle.service.bean.RangeResponse;
import com.vehicle.service.bean.WakeupResponse;

public class JsonUtil {

	private static Gson getCustomGson() {

		Gson gson = new GsonBuilder().registerTypeAdapter(WakeupResponse.class, new WakeupResponseDeserializer())
				.registerTypeAdapter(RangeResponse.class, new RangeResponseDeserializer()).create();
		return gson;
	}

	public static String toJsonString(Object o) {
		if (null == o)
			throw new NullPointerException();

		Gson gson = getCustomGson();

		return gson.toJson(o);
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromJson(String str, Class<?> T) {
		Gson gson = getCustomGson();
		return (T) gson.fromJson(str, T);
	}

	public static <T> T getSomeValue(String str, String key, Class<?> T) {
		Map<String, T> map = toKVMap(str, String.class, T);
		return map.get(key);
	}

	public static <T> List<T> getSomeValueList(String str, String key, Class<?> T) {
		String value = getSomeValue(str, key, String.class);
		return toList(value, T);
	}

	public static <T> Collection<T> getValueList(String str, Class<?> T) {
		Map<String, T> map = toKVMap(str, String.class, T);
		return map.values();
	}

	public static <K, V> Map<K, V> toKVMap(String str, Class<?> K, Class<?> V) {
		Gson g = getCustomGson();
		Map<K, V> map = g.fromJson(str, new TypeToken<Map<K, V>>() {
		}.getType());

		return map;
	}

	public static <T> List<T> toList(String str, Class<?> T) {
		Gson g = getCustomGson();
		List<T> objs = g.fromJson(str, new TypeToken<List<T>>() {
		}.getType());

		return objs;
	}
}
