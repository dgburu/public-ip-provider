package com.dgbsoft.apps.fileserver.http.actions;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.logging.Logger;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;

import com.dgbsoft.apps.fileserver.Activator;

public class ShutDownAction extends BaseAction {

	private final static Logger LOG = Logger.getLogger(ShutDownAction.class.getName());

	@Override
	public boolean perform() {
		if (FrameworkUtil.getBundle(Activator.class) != null) {
			BundleContext context = FrameworkUtil.getBundle(Activator.class).getBundleContext();
			if (context != null) {
				if (context.getBundle(0) != null) {
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
					try {
						writer.append("HTTP/1.0 200 OK\r\n");
						writer.append("Content-Type: text/plain\r\n");
						writer.append("Date: " + new Date() + "\r\n" + "Server: DgbSoft File server 1.0\r\n\r\n");
						writer.append("OK\r\n");
						writer.flush();
					} catch (IOException e) {
						LOG.severe("Cannot send ok message , msg = " + e.getMessage());
					}
					try {
						context.getBundle(0).stop();
					} catch (BundleException e) {
						LOG.severe("Error stoping bundle 0" + e.getMessage());
					}
				} else {
					LOG.severe("No bundle 0");
				}
			} else {
				LOG.severe("No context");
			}
		} else {
			LOG.severe("No activator bundle");
		}
		return true;
	}

	@Override
	public void stop() {
	}

	@Override
	public String getActionId() {
		return "SHUTDOWNALL";
	}

}
