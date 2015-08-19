package com.dgbsoft.core.services;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public final class ServicesUtil {
	
	/**
	 * No instance.
	 */
	private ServicesUtil() {
	}
	
	/**
	 * Gets the service for the class.
	 * @param clazz the class to get the service for.
	 * @return the service object.
	 * @param <T> the generic type
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

}
