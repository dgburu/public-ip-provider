package com.dgbsoft.apps.fileserver.http.actions;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URLConnection;
import java.util.Date;
import java.util.logging.Logger;

import com.dgbsoft.apps.fileserver.http.IStreamProvider;
import com.dgbsoft.core.services.IFileProviderService;
import com.dgbsoft.core.services.ServicesUtil;

public class GetFileAction extends BaseAction {
	
	private final static Logger LOG = Logger.getLogger(GetFileAction.class.getName());

	private boolean stop = false;
	private String fileName = null;
	
	public GetFileAction(IStreamProvider streamProvider, String fileName) {
		super(streamProvider);
		this.fileName = fileName;
	}

	@Override
	public boolean perform() {
		stop = false;
		IFileProviderService service = ServicesUtil.getService(IFileProviderService.class);
		if (service != null) {
			InputStream fileToRead = service.getFile(fileName);
			if (fileToRead != null) {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getOutputStream()));
				try {
					 String contentType = URLConnection.guessContentTypeFromStream(fileToRead);
				     writer.append("HTTP/1.0 200 OK\r\n");
				     if (contentType!=null) {
				    	 writer.append("Content-Type: " + contentType + "\r\n");
				     }
				     writer.append("Date: " + new Date() + "\r\n" + "Server: DgbSoft File server 1.0\r\n\r\n");
				     writer.flush();
				     sendFile(fileToRead); // send raw file 
				} catch (IOException e) {
					LOG.severe("Cannot send file, msg = " + e.getMessage());
				}
			} else {
				LOG.severe("No available file = " + fileName);
			}
		} else {
			LOG.severe("No file provider service available");
		}
		return true;
	}

	private void sendFile(InputStream file) throws IOException {
		byte[] buffer = new byte[4096];
		while ((file.available() > 0) && !stop) {
			getOutputStream().write(buffer, 0, file.read(buffer));
			getOutputStream().flush();
		}
	}

	@Override
	public void stop() {
		stop = true;		
	}

}
