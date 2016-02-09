package com.dgbsoft.apps.fileserver.http.actions;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import com.dgbsoft.apps.fileserver.http.IStreamProvider;

public class StartMuleAction extends BaseAction {

	private final static Logger LOG = Logger.getLogger(StartMuleAction.class.getName());

	public StartMuleAction(IStreamProvider streamProvider) {
		super(streamProvider);
	}

	@Override
	public boolean perform() {
		Properties config = new Properties();
		try (FileInputStream configIS = new FileInputStream("fileserver.properties")) {
			config.load(configIS);
		} catch (IOException e) {
			LOG.severe("Cannot read configuration file, msg =" + e);
		}
		String cmd = config.getProperty("startMule", "");
		if (!cmd.isEmpty()) {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
			try {
				ProcessBuilder pb = new ProcessBuilder();
				pb.command(cmd);
				Process proc = pb.start();
				proc.waitFor();

				writer.append("HTTP/1.0 200 OK\r\n");
		    	writer.append("Content-Type: text/plain\r\n");
			    writer.append("Date: " + new Date() + "\r\n" + "Server: DgbSoft File server 1.0\r\n\r\n");
			    writer.append("OK\r\n");
				writer.flush();
			} catch (IOException | InterruptedException e) {
				LOG.severe("Cannot start amule, msg = " + e.getMessage());
			}
		} else {
			LOG.warning("No start mule command");
		}
		return true;
	}

	@Override
	public void stop() {
		// nothing to do
	}

}