package com.vehicle.imserver.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	public static <T> T PostJson(String url, Object entity, Class<T> t) {

		CloseableHttpClient httpclient = HttpClients.custom()
				.setRetryHandler(new HttpRequestRetryPolicy()).build();

		String resp = "";
		CloseableHttpResponse response = null;

		try {
			HttpPost httppost = new HttpPost(url);

			StringEntity strBody = new StringEntity(
					JsonUtil.toJsonString(entity));

			strBody.setContentType("application/json");
			strBody.setContentEncoding("UTF-8");

			httppost.setEntity(strBody);

			System.out
					.println("executing request " + httppost.getRequestLine());

			response = httpclient.execute(httppost);

			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				System.out.println("Response content length: "
						+ resEntity.getContentLength());
				resp = EntityUtils.toString(resEntity, "UTF-8");
				// EntityUtils.consume(resEntity);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (null != response) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		System.out.println(resp);
		return JsonUtil.fromJson(resp, t);
	}

	public static <T> T GetJson(String url, Class<T> t) {

		CloseableHttpClient httpclient = HttpClients.custom()
				.setRetryHandler(new HttpRequestRetryPolicy()).build();

		String resp = "";
		CloseableHttpResponse response = null;

		try {
			HttpGet httpget = new HttpGet(url);

			System.out.println("executing request " + httpget.getRequestLine());

			response = httpclient.execute(httpget);

			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				System.out.println("Response content length: "
						+ resEntity.getContentLength());
				resp = EntityUtils.toString(resEntity, "UTF-8");
				// EntityUtils.consume(resEntity);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (null != response) {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return JsonUtil.fromJson(resp, t);
	}
}
