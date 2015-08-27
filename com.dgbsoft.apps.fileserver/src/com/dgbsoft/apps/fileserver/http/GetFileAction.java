package com.dgbsoft.apps.fileserver.http;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URLConnection;
import java.util.Date;
import java.util.logging.Logger;

import com.dgbsoft.apps.fileserver.IAction;

public class GetFileAction implements IAction {
	
	private final static Logger LOG = Logger.getLogger(GetFileAction.class.getName());

	private HttpProtocol protocol = null;
	private InputStream fileToSend = null;
	
	public GetFileAction(HttpProtocol protocol, InputStream fileToSend) {
		this.protocol = protocol;
		this.fileToSend = fileToSend;
	}

	@Override
	public boolean perform() {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(protocol.getOutputStream()));
		try {
			 String contentType = URLConnection.guessContentTypeFromStream(fileToSend);
		     writer.append("HTTP/1.0 200 OK\r\n");
		     if (contentType!=null) {
		    	 writer.append("Content-Type: " + contentType + "\r\n");
		     }
		     writer.append("Date: " + new Date() + "\r\n" + "Server: DgbSoft File server 1.0\r\n\r\n");
		     sendFile(fileToSend); // send raw file 
		} catch (IOException e) {
			LOG.severe("Cannot send file, msg = " + e.getMessage());
		}
		return true;
	}

	private void sendFile(InputStream file) throws IOException {
		byte[] buffer = new byte[4096];
		while (file.available()>0) {
			protocol.getOutputStream().write(buffer, 0, file.read(buffer));
			protocol.getOutputStream().flush();
		}
	}

}
