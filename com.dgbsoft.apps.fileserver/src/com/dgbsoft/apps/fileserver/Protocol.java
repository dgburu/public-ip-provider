package com.dgbsoft.apps.fileserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.logging.Logger;

import com.dgbsoft.apps.fileserver.http.HttpProtocol;

public abstract class Protocol {

	private final static Logger LOG = Logger.getLogger(Protocol.class.getName());

	public abstract IAction getAction();

	public static Protocol getProtocol(InetAddress inetAddress, InputStream inputStream, OutputStream outputStream) {
		LOG.finest("Getting protocol");
		return new HttpProtocol(inetAddress, inputStream, outputStream);
	}

}
