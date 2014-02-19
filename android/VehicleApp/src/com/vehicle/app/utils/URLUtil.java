package com.vehicle.app.utils;

public class URLUtil {
	
	public static String UrlAppend(String ...paths)
	{
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < paths.length; i ++)
		{
			String path = paths[i];
			
			if(!path.isEmpty())
			{
				if(0 != i && !path.startsWith("/"))
				{
					sb.append("/");
				}
				
				sb.append(path);
			}
			
			if(i != paths.length-1 && sb.toString().endsWith("/"))
			{
				sb.deleteCharAt(sb.length()-1);
			}
		}
		
		return sb.toString();
	}
}
