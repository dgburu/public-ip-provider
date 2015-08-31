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
import com.dgbsoft.core.services.IFileProviderService;
import com.dgbsoft.core.services.ServicesUtil;

public class HttpProtocol extends Protocol {

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
			if (line.startsWith("GET") && line.endsWith("HTTP/1.1") && (line.charAt(4) == '/') && (line.length() >= 14)) {
				String fileName = line.substring(4, line.length() - 9).trim();
				LOG.finest("GET file = " + fileName);
				IFileProviderService service = ServicesUtil.getService(IFileProviderService.class);
				if (service != null) {
					InputStream fileToRead = service.getFile(fileName);
					if (fileToRead != null) {
						return new GetFileAction(this, fileToRead);
					} else {
						LOG.severe("No available file = " + fileName);
					}
				} else {
					LOG.severe("No file provider service available");
				}
			} else if (line.startsWith("GETFILELIST")) {
				LOG.finest("GETFILELIST");
				StringBuffer message = new StringBuffer();
				IFileProviderService service = ServicesUtil.getService(IFileProviderService.class);
				if (service != null) {
					Set<String> fileList = service.getFileList(false);
					for (String fileName : fileList) {
						message.append(fileName + "\r\n");
					}
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
					writer.append(message);
					LOG.finest(message.toString());
					writer.flush();
				} else {
					LOG.severe("No file provider service available");
				}
			} else if (line.startsWith("UPDATEFILELIST")) {
				LOG.finest("UPDATEFILELIST");
				IFileProviderService service = ServicesUtil.getService(IFileProviderService.class);
				if (service != null) {
					service.getFileList(true);
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
					writer.append("OK\r\n");
					writer.flush();
				} else {
					LOG.severe("No file provider service available");
				}
			} else {
				LOG.severe("Bad request");
			}
		} catch (IOException e) {
			LOG.severe("Cannot get line, msg = " + e.getMessage());
		}
		return null;
	}
	
	protected InputStream getInputStream() {
		return inputStream;
	}
	
	protected OutputStream getOutputStream() {
		return outputStream;
	}

}
