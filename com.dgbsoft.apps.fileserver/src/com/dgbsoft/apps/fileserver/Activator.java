package com.dgbsoft.apps.fileserver;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.dgbsoft.core.services.IFileServerService;
import com.dgbsoft.core.services.ServicesUtil;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
