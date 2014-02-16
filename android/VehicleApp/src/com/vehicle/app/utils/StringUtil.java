package com.vehicle.app.utils;

import java.util.List;

public class StringUtil {

	public static boolean IsNullOrEmpty(String str) {
		return null == str || str.isEmpty();
	}

	public static boolean IsEmail(String str) {
		return str.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+");
	}

	public static String JointString(List<String> strs, String seperator) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < strs.size(); i++) {
			sb.append(strs.get(i));
			if (i != strs.size() - 1) {
				sb.append(seperator);
			}
		}

		return sb.toString();
	}
}
