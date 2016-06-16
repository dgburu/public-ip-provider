package com.dgbsoft.core.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

public final class ServicesUtil {

	/**
	 * No instance.
	 */
	private ServicesUtil() {
	}

	/**
	 * Gets the service for the class.
	 * 
	 * @param clazz
	 *            the class to get the service for.
	 * @return the service object.
	 * @param <T>
	 *            the generic type
	 */
	public static <T> T getService(Class<T> clazz) {
		BundleContext context = FrameworkUtil.getBundle(ServicesUtil.class).getBundleContext();
		if (context != null) {
			ServiceReference<T> serviceRef = null;
			try {
				serviceRef = context.getServiceReference(clazz);
			} catch (IllegalStateException | ExceptionInInitializerError | SecurityException e) {
				e.printStackTrace();
			}
			if (serviceRef != null) {
				return context.getService(serviceRef);
			}
		}
		return null;
	}

	public static <T> List<T> getServices(Class<T> clazz) {
		BundleContext context = FrameworkUtil.getBundle(ServicesUtil.class).getBundleContext();
		if (context != null) {
			Collection<ServiceReference<T>> serviceRefs = null;
			try {
				serviceRefs = context.getServiceReferences(clazz, null);
			} catch (IllegalStateException | ExceptionInInitializerError | SecurityException
					| InvalidSyntaxException e) {
				e.printStackTrace();
			}
			if (serviceRefs != null) {
				List<T> services = new ArrayList<>(serviceRefs.size());
				for (ServiceReference<T> serviceRef : serviceRefs) {
					services.add(context.getService(serviceRef));
				}
				return services;
			}
		}

		return Collections.emptyList();
	}

}
