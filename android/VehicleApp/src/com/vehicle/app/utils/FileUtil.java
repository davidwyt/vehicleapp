package com.vehicle.app.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import android.content.Context;
import android.os.Environment;

public class FileUtil {

	public static String getTempImgPath() {

		String root = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
				+ Environment.DIRECTORY_DCIM;
		File path = new File(root + File.separator + "Image");
		if (!path.exists()) {
			try {
				path.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String filePath = path.getAbsolutePath() + File.separator + UUID.randomUUID().toString() + "."
				+ Constants.POSTFIX_DEFAULT_IMAGE;
		return filePath;
	}

	public static String AppendPath(String part1, String part2) {
		String path = part1;
		if (!path.isEmpty() && !path.endsWith(File.separator)) {
			path += File.separator;
		}

		path += part2;

		return path;
	}

	public static void SaveFile(String path, InputStream input) throws IOException {
		createParentDirs(path);
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
		createParentDirs(path);

		try {
			IOUtils.write(bytes, new FileOutputStream(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void CopyFile(String src, String dest) {
		try {
			FileInputStream input = new FileInputStream(src);
			FileOutputStream output = new FileOutputStream(dest);

			createParentDirs(dest);
			IOUtils.copy(input, output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getPostFix(String filePath) {
		File f = new File(filePath);
		String fileName = f.getName();
		int index = fileName.lastIndexOf(".");
		if (index >= 0 && index < fileName.length() - 1) {
			return fileName.substring(index + 1);
		} else {
			return "";
		}
	}

	public static String AppendTempFilePath(String root, String subPath, String postFix) {
		String path = root + File.separator + subPath + File.separator + UUID.randomUUID().toString() + "." + postFix;
		createParentDirs(path);
		return path;
	}

	public static void createParentDirs(String path) {
		File file = new File(path);
		File parent = file.getParentFile();
		System.out.println("ppppppppppppp:" + parent.getAbsolutePath());
		if (null != parent && !parent.exists()) {
			try {
				System.out.println("mkkkkkkkkkkkkkkkkkk");
				parent.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String genPathForApk(Context context, int majorVersion, int minorVersion) {
		String root = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
				+ Environment.DIRECTORY_DCIM;

		String path = root + File.separator + "apk" + File.separator + "VehicleApp" + File.separator + (majorVersion + "." + minorVersion)
				+ File.separator + "VehicleApp.apk";

		createParentDirs(path);
		return path;
	}

	public static String genPathForImage(Context context, String postFix) {
		File file = context.getFilesDir();
		String root = file.getAbsolutePath();

		return AppendTempFilePath(root, "Image", postFix);
	}

	public static String genPathForAudio(Context context, String postFix) {
		File file = context.getFilesDir();
		String root = file.getAbsolutePath();

		return AppendTempFilePath(root, "Audio", postFix);
	}
}
