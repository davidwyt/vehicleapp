package com.vehicle.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vehicle.service.bean.IResponse;

public class JerseyUtil {

	public static <T> T HttpGet(String url, Class<T> t) {

		Client client = Client.create();

		// ServiceFinder.setIteratorProvider(new
		// VehicleServiceIteratorProvider());

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		return (T) response.getEntity(t);
	}

	public static <T> T HttpPost(String url, Object entity, Class<T> t) {

		// ServiceFinder.setIteratorProvider(new
		// VehicleServiceIteratorProvider());

		Client client = Client.create();

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, entity);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		return response.getEntity(t);

	}

	public static IResponse UploadFile(String url, String filePath) {

		File file = new File(filePath);

		InputStream fileInStream = null;
		try {
			fileInStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sContentDisposition = "attachment; filename=\"" + file.getName()
				+ "\"";

		Client client = Client.create();
		// ServiceFinder.setIteratorProvider(new
		// VehicleServiceIteratorProvider());

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource
				.type(MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", sContentDisposition)
				.post(ClientResponse.class, fileInStream);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		return response.getEntity(IResponse.class);
	}

	public static void DownloadFile(String url, String filePath) {

		Client client = Client.create();

		// ServiceFinder.setIteratorProvider(new
		// VehicleServiceIteratorProvider());

		WebResource webResource = client.resource(url);

		ClientResponse response = webResource.type(
				MediaType.APPLICATION_OCTET_STREAM).get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
		}

		InputStream input = response.getEntityInputStream();

		OutputStream outStream = null;
		try {

			outStream = new FileOutputStream(filePath);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			IOUtils.copy(input, outStream);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
