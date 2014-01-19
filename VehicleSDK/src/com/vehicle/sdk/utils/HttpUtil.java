package com.vehicle.sdk.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

public class HttpUtil {

	public static <T> T PostJson(String url, Object entity, Class<T> t) {
		StringRequestEntity requestEntity = null;
		try {
			requestEntity = new StringRequestEntity(
					JsonUtil.toJsonString(entity), "application/json", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestEntity(requestEntity);

		HttpClient httpClient = new HttpClient();
		int statusCode = 0;
		String resp = "";

		try {
			statusCode = httpClient.executeMethod(postMethod);

			if (statusCode != HttpStatus.SC_OK) {
				System.out.println(String.format(
						"http post failed, url: %s, entity: %s!!!", url,
						JsonUtil.toJsonString(entity)));
			}

			System.out.println(" status code:" + statusCode);

			resp = postMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

		return JsonUtil.fromJson(resp, t);
	}

	public static <T> T GetJson(String url, Class<T> t) {

		GetMethod getMethod = new GetMethod(url);

		HttpClient httpClient = new HttpClient();
		int statusCode = 0;
		String resp = "";

		try {
			statusCode = httpClient.executeMethod(getMethod);

			if (statusCode != HttpStatus.SC_OK) {
				System.out.println(String.format("http get failed, url: %s!!!",
						url));
			}

			resp = getMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}

		return JsonUtil.fromJson(resp, t);
	}

	public static <T> T UploadFile(String url, String filePath, Class<T> t) {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);

		String resp = "";

		try {
			File file = new File(filePath);
			Part[] parts = new Part[1];
			parts[0] = new FilePart(file.getName(), file);

			postMethod.setRequestEntity(new MultipartRequestEntity(parts,
					postMethod.getParams()));

			int statusCode = httpClient.executeMethod(postMethod);

			if (statusCode != HttpStatus.SC_OK) {
				System.out.println(String.format(
						"http upload file failed, url: %s!!!", url));
			}

			try {
				resp = postMethod.getResponseBodyAsString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();

		}

		return JsonUtil.fromJson(resp, t);
	}

	public static InputStream DownloadFile(String url) {
		InputStream input = null;
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		try {
			int statusCode = client.executeMethod(getMethod);

			if (statusCode != HttpStatus.SC_OK) {
				System.out.println(String.format(
						"http upload file failed, url: %s!!!", url));
			} else {
				input = getMethod.getResponseBodyAsStream();
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}

		return input;
	}
}
