package com.vehicle.app.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

public class FileUtil {

	public static String AppendPath(String part1, String part2) {
		String path = part1;
		if (!path.isEmpty() && !path.endsWith(File.separator)) {
			path += File.separator;
		}

		path += part2;

		return path;
	}

	public static void SaveFile(String path, InputStream input) throws IOException {
		OutputStream outStream = new FileOutputStream(path);
		IOUtils.copy(input, outStream);
	}

	public static byte[] ReadFile(String path) {
		BufferedInputStream input = null;
		try {

			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] cache = new byte[1024];
			input = new BufferedInputStream(new FileInputStream(path));

			int count = -1;
			while ((count = input.read(cache, 0, 1024)) != -1) {
				output.write(cache, 0, count);
			}
			
			return output.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (null != input) {
				try {
					input.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static byte[] ReadInputStream(InputStream input) {
		try {
			return IOUtils.toByteArray(input);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void WriteBytes(byte[] bytes, String path) {
		File file = new File(path);
		File parent = file.getParentFile();
		if (null != parent && !parent.exists()) {
			try {
				parent.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			IOUtils.write(bytes, new FileOutputStream(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
