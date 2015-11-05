package com.dgbsoft.rpi.gpio;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.dgbsoft.core.services.IRpiGpioService;
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
		IRpiGpioService service = ServicesUtil.getService(IRpiGpioService.class);
		if (service != null) {
			service.shutdown();
		}
	}

}
