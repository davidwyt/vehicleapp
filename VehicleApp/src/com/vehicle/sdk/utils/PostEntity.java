package com.vehicle.sdk.utils;

public class PostEntity{
	public PostEntity(String url2, Object entity2, Class<?> t) {
		// TODO Auto-generated constructor stub
		this.url = url2;
		this.entity = entity2;
		this.respC = t;
	}
	public String url;
	public Object entity;
	public Class<?> respC;
}
