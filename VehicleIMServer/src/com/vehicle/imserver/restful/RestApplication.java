package com.vehicle.imserver.restful;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class RestApplication extends Application {
	HashSet<Object> singletons = new HashSet<Object>();

	public RestApplication() {
		singletons.add(new MessageRest());
	}

	@Override
	public Set<Class<?>> getClasses() {
		HashSet<Class<?>> set = new HashSet<Class<?>>();
		return set;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
