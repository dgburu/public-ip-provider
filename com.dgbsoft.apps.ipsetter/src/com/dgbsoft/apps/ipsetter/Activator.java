package com.dgbsoft.apps.ipsetter;

import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.dgbsoft.core.services.ITimerService;
import com.dgbsoft.core.services.ServicesUtil;

public class Activator implements BundleActivator {

	private final static Logger LOG = Logger.getLogger(Activator.class.getName());

	private static BundleContext context;
	
	private IpSetter ipSetter = null;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		ITimerService service = ServicesUtil.getService(ITimerService.class);
		if (service != null) {
			if (ipSetter == null) {
				ipSetter = new IpSetter();
				service.addTimerListener(ipSetter);
			} else {
				LOG.warning("ipsetter alredy created");
			}
			
		} else {
			LOG.severe("Cannot find timer service");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		ITimerService service = ServicesUtil.getService(ITimerService.class);
		if (service != null) {
			service.removeTimerListener(ipSetter);
		}
		Activator.context = null;
	}

}
