package com.vehicle.sdk.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	public static <T> T PostJson(String url, Object entity, Class<T> t) {

		HttpClient httpclient = new DefaultHttpClient();

		String resp = "";
		HttpResponse response = null;

		try {
			HttpPost httppost = new HttpPost(url);

			if (null != entity) {
				StringEntity strBody = new StringEntity(JsonUtil.toJsonString(entity), "UTF-8");
				strBody.setContentType("application/json");

				httppost.setEntity(strBody);
			}

			System.out.println("executing request " + httppost.getRequestLine());

			response = httpclient.execute(httppost);

			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());

			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					System.out.println("Response content length: " + resEntity.getContentLength());

					resp = EntityUtils.toString(resEntity, "UTF-8");

					System.out.println(resp);
					return JsonUtil.fromJson(resp, t);
					// EntityUtils.consume(resEntity);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static <T> T GetJson(String url, Class<T> t) {

		HttpClient httpclient = new DefaultHttpClient();

		String resp = "";
		HttpResponse response = null;

		try {
			HttpGet httpget = new HttpGet(url);

			System.out.println("executing request " + httpget.getRequestLine());

			response = httpclient.execute(httpget);

			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());

			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					System.out.println("Response content length: " + resEntity.getContentLength());
					resp = EntityUtils.toString(resEntity, "UTF-8");

					return JsonUtil.fromJson(resp, t);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static <T> T UploadFile(String url, String filePath, Class<T> t) {

		HttpClient httpclient = new DefaultHttpClient();

		String resp = "";
		HttpResponse response = null;

		try {
			HttpPost httppost = new HttpPost(url);

			FileEntity fileBody = new FileEntity(new File(filePath), "application/octet-stream");

			httppost.setEntity(fileBody);

			System.out.println("executing request " + httppost.getRequestLine());

			response = httpclient.execute(httppost);

			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());

			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					System.out.println("Response content length: " + resEntity.getContentLength());
					resp = EntityUtils.toString(resEntity, "UTF-8");
					System.out.println("upload resp:" + resp);
					return JsonUtil.fromJson(resp, t);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static InputStream DownloadFile(String url) {

		HttpClient httpclient = new DefaultHttpClient();

		InputStream input = null;

		HttpResponse response = null;

		try {
			HttpGet httpget = new HttpGet(url);

			System.out.println("executing request " + httpget.getRequestLine());

			response = httpclient.execute(httpget);

			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());

			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				input = response.getEntity().getContent();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return input;
	}
}
