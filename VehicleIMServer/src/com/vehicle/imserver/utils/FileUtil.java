package com.vehicle.imserver.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

public class FileUtil {

	public static String GenPathForFileTransmission(String root,
			String fileName, String token, int type) {
		String[] temps = fileName.split("\\.");
		String name = token + "." + temps[temps.length - 1];

		return AppendPath(AppendPath(root, Contants.getFileRootPath(type)),
				name);
	}

	public static String GenPathForCommentFile(String root, String fileName,
			String token, int type) {
		String[] temps = fileName.split("\\.");
		String name = token + "." + temps[temps.length - 1];

		return AppendPath(AppendPath(root, Contants.getImgSourcePath(type)),
				name);
	}

	public static String AppendPath(String part1, String part2) {
		String path = part1;
		if (!path.isEmpty() && !path.endsWith(File.separator)) {
			path += File.separator;
		}

		path += part2;

		return path;
	}

	public static void SaveFile(String path, InputStream input)
			throws IOException {
		File file = new File(path);
		File parent = file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		OutputStream outStream = new FileOutputStream(path);
		IOUtils.copy(input, outStream);
	}
}
