package com.vehicle.sdk.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.sun.jersey.spi.service.ServiceFinder.ServiceIteratorProvider;

public class VehicleServiceIteratorProvider<T> extends
		ServiceIteratorProvider<T> {
	private static final HashMap<String, String[]> SERVICES = new HashMap<String, String[]>();

	private static final String[] com_sun_jersey_server_impl_model_method_dispatch_ResourceMethodDispatchProvider = new String[] {};

	private static final String[] com_sun_jersey_spi_container_ContainerProvider = new String[] {
			"com.sun.jersey.server.impl.container.httpserver.HttpHandlerContainerProvider",
			"com.sun.jersey.server.impl.container.grizzly.GrizzlyContainerProvider" };

	private static final String[] com_sun_jersey_spi_container_ContainerRequestFilter = new String[] { "com.sun.jersey.server.impl.container.filter.NormalizeFilter" };

	private static final String[] com_sun_jersey_spi_container_ContainerResponseFilter = new String[] {};

	private static final String[] com_sun_jersey_spi_container_ResourceMethodCustomInvokerDispatchProvider = new String[] {
			"com.sun.jersey.server.impl.model.method.dispatch.VoidVoidDispatchProvider",
			"com.sun.jersey.server.impl.model.method.dispatch.HttpReqResDispatchProvider",
			"com.sun.jersey.server.impl.model.method.dispatch.MultipartFormDispatchProvider",
			"com.sun.jersey.server.impl.model.method.dispatch.FormDispatchProvider",
			"com.sun.jersey.server.impl.model.method.dispatch.EntityParamDispatchProvider" };

	private static final String[] com_sun_jersey_spi_container_ResourceMethodDispatchProvider = new String[] {
			"com.sun.jersey.server.impl.model.method.dispatch.VoidVoidDispatchProvider",
			"com.sun.jersey.server.impl.model.method.dispatch.HttpReqResDispatchProvider",
			"com.sun.jersey.server.impl.model.method.dispatch.MultipartFormDispatchProvider",
			"com.sun.jersey.server.impl.model.method.dispatch.FormDispatchProvider",
			"com.sun.jersey.server.impl.model.method.dispatch.EntityParamDispatchProvider" };

	private static final String[] com_sun_jersey_spi_container_WebApplicationProvider = new String[] { "com.sun.jersey.server.impl.container.WebApplicationProviderImpl" };

	private static final String[] com_sun_jersey_spi_HeaderDelegateProvider = new String[] {
			"com.sun.jersey.core.impl.provider.header.LocaleProvider",
			"com.sun.jersey.core.impl.provider.header.EntityTagProvider",
			"com.sun.jersey.core.impl.provider.header.MediaTypeProvider",
			"com.sun.jersey.core.impl.provider.header.CacheControlProvider",
			"com.sun.jersey.core.impl.provider.header.NewCookieProvider",
			"com.sun.jersey.core.impl.provider.header.CookieProvider",
			"com.sun.jersey.core.impl.provider.header.URIProvider",
			"com.sun.jersey.core.impl.provider.header.DateProvider",
			"com.sun.jersey.core.impl.provider.header.StringProvider" };

	private static final String[] com_sun_jersey_spi_inject_InjectableProvider = new String[] {
			"com.sun.jersey.core.impl.provider.xml.SAXParserContextProvider",
			"com.sun.jersey.core.impl.provider.xml.XMLStreamReaderContextProvider",
			"com.sun.jersey.core.impl.provider.xml.DocumentBuilderFactoryProvider",
			"com.sun.jersey.core.impl.provider.xml.TransformerFactoryProvider" };

	private static final String[] com_sun_jersey_spi_StringReaderProvider = new String[] {
			"com.sun.jersey.server.impl.model.parameter.multivalued.StringReaderProviders$TypeFromStringEnum",
			"com.sun.jersey.server.impl.model.parameter.multivalued.StringReaderProviders$TypeValueOf",
			"com.sun.jersey.server.impl.model.parameter.multivalued.StringReaderProviders$TypeFromString",
			"com.sun.jersey.server.impl.model.parameter.multivalued.StringReaderProviders$StringConstructor",
			"com.sun.jersey.server.impl.model.parameter.multivalued.StringReaderProviders$DateProvider",
			"com.sun.jersey.server.impl.model.parameter.multivalued.JAXBStringReaderProviders$RootElementProvider" };

	private static final String[] javax_enterprise_inject_spi_Extension = new String[] { "com.sun.jersey.server.impl.cdi.CDIExtension" };

	private static final String[] javax_servlet_ServletContainerInitializer = new String[] { "com.sun.jersey.server.impl.container.servlet.JerseyServletContainerInitializer" };

	private static final String[] javax_ws_rs_ext_MessageBodyReader = new String[] {
			"com.sun.jersey.core.impl.provider.entity.StringProvider",
			"com.sun.jersey.core.impl.provider.entity.ByteArrayProvider",
			"com.sun.jersey.core.impl.provider.entity.FileProvider",
			"com.sun.jersey.core.impl.provider.entity.InputStreamProvider",
			"com.sun.jersey.core.impl.provider.entity.DataSourceProvider",
			"com.sun.jersey.core.impl.provider.entity.RenderedImageProvider",
			"com.sun.jersey.core.impl.provider.entity.MimeMultipartProvider",
			"com.sun.jersey.core.impl.provider.entity.FormProvider",
			"com.sun.jersey.core.impl.provider.entity.FormMultivaluedMapProvider",
			"com.sun.jersey.core.impl.provider.entity.XMLRootElementProvider$App",
			"com.sun.jersey.core.impl.provider.entity.XMLRootElementProvider$Text",
			"com.sun.jersey.core.impl.provider.entity.XMLRootElementProvider$General",
			"com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider$App",
			"com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider$Text",
			"com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider$General",
			"com.sun.jersey.core.impl.provider.entity.XMLListElementProvider$App",
			"com.sun.jersey.core.impl.provider.entity.XMLListElementProvider$Text",
			"com.sun.jersey.core.impl.provider.entity.XMLListElementProvider$General",
			"com.sun.jersey.core.impl.provider.entity.ReaderProvider",
			"com.sun.jersey.core.impl.provider.entity.DocumentProvider",
			"com.sun.jersey.core.impl.provider.entity.SourceProvider$StreamSourceReader",
			"com.sun.jersey.core.impl.provider.entity.SourceProvider$SAXSourceReader",
			"com.sun.jersey.core.impl.provider.entity.SourceProvider$DOMSourceReader",
			"com.sun.jersey.core.impl.provider.entity.XMLRootObjectProvider$App",
			"com.sun.jersey.core.impl.provider.entity.XMLRootObjectProvider$Text",
			"com.sun.jersey.core.impl.provider.entity.XMLRootObjectProvider$General",
			"com.sun.jersey.core.impl.provider.entity.EntityHolderReader",
			"com.sun.jersey.atom.rome.impl.provider.entity.AtomFeedProvider",
			"com.sun.jersey.atom.rome.impl.provider.entity.AtomEntryProvider",
			"com.sun.jersey.json.impl.provider.entity.JSONRootElementProvider$App",
			"com.sun.jersey.json.impl.provider.entity.JSONRootElementProvider$General",
			"com.sun.jersey.json.impl.provider.entity.JSONJAXBElementProvider$App",
			"com.sun.jersey.json.impl.provider.entity.JSONJAXBElementProvider$General",
			"com.sun.jersey.json.impl.provider.entity.JSONListElementProvider$App",
			"com.sun.jersey.json.impl.provider.entity.JSONListElementProvider$General",
			"com.sun.jersey.json.impl.provider.entity.JSONArrayProvider$App",
			"com.sun.jersey.json.impl.provider.entity.JSONArrayProvider$General",
			"com.sun.jersey.json.impl.provider.entity.JSONObjectProvider$App",
			"com.sun.jersey.json.impl.provider.entity.JSONObjectProvider$General",
			"com.sun.jersey.json.impl.provider.entity.JacksonProviderProxy",
			"com.sun.jersey.fastinfoset.impl.provider.entity.FastInfosetRootElementProvider",
			"com.sun.jersey.fastinfoset.impl.provider.entity.FastInfosetJAXBElementProvider",
			"com.sun.jersey.fastinfoset.impl.provider.entity.FastInfosetListElementProvider" };

	private static final String[] javax_ws_rs_ext_MessageBodyWriter = new String[] {
			"com.sun.jersey.core.impl.provider.entity.StringProvider",
			"com.sun.jersey.core.impl.provider.entity.ByteArrayProvider",
			"com.sun.jersey.core.impl.provider.entity.FileProvider",
			"com.sun.jersey.core.impl.provider.entity.InputStreamProvider",
			"com.sun.jersey.core.impl.provider.entity.DataSourceProvider",
			"com.sun.jersey.core.impl.provider.entity.RenderedImageProvider",
			"com.sun.jersey.core.impl.provider.entity.MimeMultipartProvider",
			"com.sun.jersey.core.impl.provider.entity.FormProvider",
			"com.sun.jersey.core.impl.provider.entity.FormMultivaluedMapProvider",
			"com.sun.jersey.core.impl.provider.entity.XMLRootElementProvider$App",
			"com.sun.jersey.core.impl.provider.entity.XMLRootElementProvider$Text",
			"com.sun.jersey.core.impl.provider.entity.XMLRootElementProvider$General",
			"com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider$App",
			"com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider$Text",
			"com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider$General",
			"com.sun.jersey.core.impl.provider.entity.XMLListElementProvider$App",
			"com.sun.jersey.core.impl.provider.entity.XMLListElementProvider$Text",
			"com.sun.jersey.core.impl.provider.entity.XMLListElementProvider$General",
			"com.sun.jersey.core.impl.provider.entity.ReaderProvider",
			"com.sun.jersey.core.impl.provider.entity.DocumentProvider",
			"com.sun.jersey.core.impl.provider.entity.StreamingOutputProvider",
			"com.sun.jersey.core.impl.provider.entity.SourceProvider$SourceWriter",
			"com.sun.jersey.server.impl.template.ViewableMessageBodyWriter",
			"com.sun.jersey.atom.rome.impl.provider.entity.AtomFeedProvider",
			"com.sun.jersey.atom.rome.impl.provider.entity.AtomEntryProvider",
			"com.sun.jersey.json.impl.provider.entity.JSONRootElementProvider$App",
			"com.sun.jersey.json.impl.provider.entity.JSONRootElementProvider$General",
			"com.sun.jersey.json.impl.provider.entity.JSONJAXBElementProvider$App",
			"com.sun.jersey.json.impl.provider.entity.JSONJAXBElementProvider$General",
			"com.sun.jersey.json.impl.provider.entity.JSONListElementProvider$App",
			"com.sun.jersey.json.impl.provider.entity.JSONListElementProvider$General",
			"com.sun.jersey.json.impl.provider.entity.JSONArrayProvider$App",
			"com.sun.jersey.json.impl.provider.entity.JSONArrayProvider$General",
			"com.sun.jersey.json.impl.provider.entity.JSONObjectProvider$App",
			"com.sun.jersey.json.impl.provider.entity.JSONObjectProvider$General",
			"com.sun.jersey.json.impl.provider.entity.JSONWithPaddingProvider",
			"com.sun.jersey.json.impl.provider.entity.JacksonProviderProxy",
			"com.sun.jersey.fastinfoset.impl.provider.entity.FastInfosetRootElementProvider",
			"com.sun.jersey.fastinfoset.impl.provider.entity.FastInfosetJAXBElementProvider",
			"com.sun.jersey.fastinfoset.impl.provider.entity.FastInfosetListElementProvider" };

	private static final String[] javax_ws_rs_ext_RuntimeDelegate = new String[] { "com.sun.jersey.server.impl.provider.RuntimeDelegateImpl" };

	static {
		SERVICES.put(
				"com.sun.jersey.server.impl.model.method.dispatch.ResourceMethodDispatchProvider",
				com_sun_jersey_server_impl_model_method_dispatch_ResourceMethodDispatchProvider);
		SERVICES.put("com.sun.jersey.spi.container.ContainerProvider",
				com_sun_jersey_spi_container_ContainerProvider);
		SERVICES.put("com.sun.jersey.spi.container.ContainerRequestFilter",
				com_sun_jersey_spi_container_ContainerRequestFilter);
		SERVICES.put("com.sun.jersey.spi.container.ContainerResponseFilter",
				com_sun_jersey_spi_container_ContainerResponseFilter);
		SERVICES.put(
				"com.sun.jersey.spi.container.ResourceMethodCustomInvokerDispatchProvider",
				com_sun_jersey_spi_container_ResourceMethodCustomInvokerDispatchProvider);
		SERVICES.put(
				"com.sun.jersey.spi.container.ResourceMethodDispatchProvider",
				com_sun_jersey_spi_container_ResourceMethodDispatchProvider);
		SERVICES.put("com.sun.jersey.spi.container.WebApplicationProvider",
				com_sun_jersey_spi_container_WebApplicationProvider);

		SERVICES.put("com.sun.jersey.spi.HeaderDelegateProvider",
				com_sun_jersey_spi_HeaderDelegateProvider);
		SERVICES.put("com.sun.jersey.spi.inject.InjectableProvider",
				com_sun_jersey_spi_inject_InjectableProvider);

		SERVICES.put("com.sun.jersey.spi.StringReaderProvider",
				com_sun_jersey_spi_StringReaderProvider);
		SERVICES.put("javax.enterprise.inject.spi.Extension",
				javax_enterprise_inject_spi_Extension);
		SERVICES.put("javax.servlet.ServletContainerInitializer",
				javax_servlet_ServletContainerInitializer);

		SERVICES.put("javax.ws.rs.ext.MessageBodyReader",
				javax_ws_rs_ext_MessageBodyReader);
		SERVICES.put("javax.ws.rs.ext.MessageBodyWriter",
				javax_ws_rs_ext_MessageBodyWriter);

		SERVICES.put("javax.ws.rs.ext.RuntimeDelegate",
				javax_ws_rs_ext_RuntimeDelegate);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Class<T>> createClassIterator(Class<T> service,
			String serviceName, ClassLoader loader,
			boolean ignoreOnClassNotFound) {
		
		String[] classesNames = SERVICES.get(serviceName);
		
		if(null == classesNames)
		{
			System.out.println("nullllllllllllllllllllllll:" + serviceName);
			return (new ArrayList<Class<T>>()).iterator();
		}
		
		int length = classesNames.length;
		ArrayList<Class<T>> classes = new ArrayList<Class<T>>(length);
		for (int i = 0; i < length; i++) {
			try {
				
				classes.add((Class<T>) loader.loadClass(classesNames[i]));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return classes.iterator();
	}

	@Override
	public Iterator<T> createIterator(Class<T> service, String serviceName,
			ClassLoader loader, boolean ignoreOnClassNotFound) {
		
		String[] classesNames = SERVICES.get(serviceName);
		int length = classesNames.length;
		ArrayList<T> classes = new ArrayList<T>(length);
		for (int i = 0; i < length; i++) {
			try {
				if(classesNames[i].isEmpty())
					continue;
				
				classes.add(service.cast(Class.forName(classesNames[i])
						.newInstance()));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return classes.iterator();
	}
}