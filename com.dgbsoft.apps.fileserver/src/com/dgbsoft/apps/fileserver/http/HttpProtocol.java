package com.dgbsoft.apps.fileserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.logging.Logger;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;

import com.dgbsoft.apps.fileserver.Activator;
import com.dgbsoft.apps.fileserver.IAction;
import com.dgbsoft.apps.fileserver.Protocol;
import com.dgbsoft.apps.fileserver.http.actions.GetFileListAction;
import com.dgbsoft.apps.fileserver.http.actions.UpdateFileListAction;

public class HttpProtocol extends Protocol implements IStreamProvider {

	private final static Logger LOG = Logger.getLogger(HttpProtocol.class.getName());

	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	
	public HttpProtocol(InetAddress clientAddress, InputStream inputStream, OutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	@Override
	public IAction getAction() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		try {
			line = reader.readLine();
			LOG.fine("Http request = " + line);
			if (line != null) {
				/*
				if (line.startsWith("GET") && line.endsWith("HTTP/1.1") && (line.charAt(4) == '/') && (line.length() >= 14)) {
					String fileName = line.substring(5, line.length() - 9).trim();
					LOG.finest("GET file = " + fileName);
					return new GetFileAction(this, fileName);
				} else */
				if (line.contains("GETFILELIST")) {
					LOG.finest("GETFILELIST");
					return new GetFileListAction(this);
				} else if (line.contains("UPDATEFILELIST")) {
					LOG.finest("UPDATEFILELIST");
					return new UpdateFileListAction(this);
				/*
				} else if (line.contains("ENABLEFAN")) {
					LOG.finest("ENABLEFAN");
					return new EnableFanAction(this);
				} else if (line.contains("DISABLEFAN")) {
					LOG.finest("DISABLEFAN");
					return new DisableFanAction(this);
				*/
				} else if (line.contains("SHUTDOWNALL")) {
					LOG.finest("SHUTDOWNALL");
					if (FrameworkUtil.getBundle(Activator.class) != null) {
						BundleContext context = FrameworkUtil.getBundle(Activator.class).getBundleContext();
						if (context != null) {
							if (context.getBundle(0) != null) {
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
				} else {
					LOG.severe("Bad request");
				}
			}
		} catch (IOException e) {
			LOG.severe("Cannot get line, msg = " + e.getMessage());
		}
		return null;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	public OutputStream getOutputStream() {
		return outputStream;
	}

}
