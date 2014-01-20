package com.vehicle.sdk.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
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
					JsonUtil.toJsonString(entity), ContentType.create(
							"application/json", "UTF-8"));

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

	public static <T> T UploadFile(String url, String filePath, Class<T> t) {

		CloseableHttpClient httpclient = HttpClients.custom()
				.setRetryHandler(new HttpRequestRetryPolicy()).build();

		String resp = "";
		CloseableHttpResponse response = null;

		try {
			HttpPost httppost = new HttpPost(url);

			FileEntity fileBody = new FileEntity(new File(filePath),
					ContentType.APPLICATION_OCTET_STREAM);

			httppost.setEntity(fileBody);

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

		return JsonUtil.fromJson(resp, t);
	}

	public static InputStream DownloadFile(String url) {

		CloseableHttpClient httpclient = HttpClients.custom()
				.setRetryHandler(new HttpRequestRetryPolicy()).build();

		InputStream input = null;

		CloseableHttpResponse response = null;

		try {
			HttpGet httpget = new HttpGet(url);

			System.out.println("executing request " + httpget.getRequestLine());

			response = httpclient.execute(httpget);

			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());
			input = response.getEntity().getContent();
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

		return input;
	}
}
