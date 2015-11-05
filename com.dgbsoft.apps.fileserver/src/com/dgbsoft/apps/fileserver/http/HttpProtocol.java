package com.dgbsoft.apps.fileserver.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.util.Set;
import java.util.logging.Logger;

import com.dgbsoft.apps.fileserver.IAction;
import com.dgbsoft.apps.fileserver.Protocol;
import com.dgbsoft.apps.fileserver.http.actions.GetFileAction;
import com.dgbsoft.apps.fileserver.http.actions.GetFileListAction;
import com.dgbsoft.apps.fileserver.http.actions.UpdateFileListAction;
import com.dgbsoft.core.services.IFileProviderService;
import com.dgbsoft.core.services.ServicesUtil;

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
