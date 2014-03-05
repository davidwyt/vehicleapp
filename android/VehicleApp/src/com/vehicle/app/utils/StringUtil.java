package com.vehicle.app.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static boolean IsNullOrEmpty(String str) {
		return null == str || str.isEmpty();
	}

	public static boolean IsEmail(String str) {
		return str.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9_-]+.[a-z]+");
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
	
	public static int getWordCount(String string) {
		Pattern pattern = Pattern.compile("[\\w']+|[\\u3400-\\u4DB5\\u4E00-\\u9FCC]");
		Matcher matcher = pattern.matcher(string);
		int count = 0;
		while (matcher.find())
			count++;
		return count;
	}

	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	public static boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	private final static String regxpForHtml = "<([^>]*)>";

	public static String filterHtml(String str) {
		if (null == str)
			return null;

		Pattern pattern = Pattern.compile(regxpForHtml);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public static String formatNumber(String str) {
		if (null == str)
			return str;

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c >= '0' && c <= '9') {
				sb.append(c);
			}
		}

		return sb.toString();
	}
}
