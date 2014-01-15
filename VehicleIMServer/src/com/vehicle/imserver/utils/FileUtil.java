package com.vehicle.imserver.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

public class FileUtil {

	public static String GenPathForFileTransmission(String root, String fileName)
	{
		return AppendPath(AppendPath(root, Contants.FILE_TRANSMISSION_ROOTPATH), fileName);
	}
	
	public static String AppendPath(String part1, String part2)
	{
		String path = part1;
		if(!path.endsWith(File.separator))
		{
			path += File.separator;
		}
		
		path += part2;
		
		return part1;
	}
	
	public static void SaveFile(String path, InputStream input) throws IOException
	{
		OutputStream outStream = new FileOutputStream(path);
		IOUtils.copy(input, outStream);
	}
}
