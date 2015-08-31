package com.dgbsoft.apps.fileserver.starter;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.dgbsoft.core.services.IFileServerService;
import com.dgbsoft.core.services.ServicesUtil;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private IFileServerService service = null;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		IFileServerService service = ServicesUtil.getService(IFileServerService.class);
		if (service != null) {
			service.start();
		}
	}

	public void stop(BundleContext bundleContext) throws Exception {
		if (service != null) {
			service.stop();
		}
		Activator.context = null;
	}

}
