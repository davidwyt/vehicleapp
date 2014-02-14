package com.vehicle.app.utils;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;

public class HttpRequestRetryPolicy implements HttpRequestRetryHandler{

	private static final int COUNT_MAXRETRY = 5;
	
	@Override
	public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
		if(executionCount > COUNT_MAXRETRY)
		{
			return false;
		}
		
		if(exception instanceof InterruptedIOException)
		{
			return false;
		}
		
		if(exception instanceof ConnectTimeoutException)
		{
			return true;
		}
		
		if(exception instanceof UnknownHostException)
		{
			return true;
		}
		
		if(exception instanceof SSLException)
		{
			return false;
		}
		
		/**
		HttpRequest request = context.getRequest();
		boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
		
		if(idempotent)
		{
			return true;
		}
		*/
		
		return false;
	}
}
