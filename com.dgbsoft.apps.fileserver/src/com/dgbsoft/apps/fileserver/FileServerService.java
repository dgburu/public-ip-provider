package com.dgbsoft.apps.fileserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import com.dgbsoft.core.services.IFileServerService;

public class FileServerService implements IFileServerService {

	private final static Logger LOG = Logger.getLogger(FileServerService.class.getName());

	private boolean serverRunning = false;
	private Map<String, FileServerProcessor> processors = new HashMap<String, FileServerProcessor>();
	
	@Override
	public boolean start() {
		LOG.info("Starting File server");
		
		Properties config = new Properties();
		try (FileInputStream configIS = new FileInputStream("fileserver.properties")) {
			config.load(configIS);
		} catch (IOException e) {
			LOG.severe("Cannot read configuration file, msg =" + e);
		}

		int port = 15551;
		String portStr = config.getProperty("PORT");
		if ((portStr != null) && portStr.matches("\\d+")) {
			port = Integer.parseInt(portStr);
		}
		
		try (ServerSocket server = new ServerSocket(port)) {
			LOG.fine("Server initialized on port =" + port);
			serverRunning = true;
			while (serverRunning) {
				Socket socket = server.accept();
				String remoteIp = socket.getInetAddress().getHostAddress();
				
				if (processors.containsKey(remoteIp)) {
					LOG.warning("The remote ip " + remoteIp + " is already processing");
				} else {
					FileServerProcessor processor = new FileServerProcessor();
					processor.setSocket(socket);
					processor.setProcessors(processors);
					(new Thread(processor)).start();
				}
			}
			
		} catch (IOException e) {
			LOG.severe("Error initializing server on port " + port + ", msg =" + e);
			stop();
			return false;
		}

		return true;
	}

	@Override
	public boolean stop() {
		LOG.info("Stopping File server");
		serverRunning = false;
		for (FileServerProcessor processor : processors.values()) {
			processor.stop();
		}
		return true;
	}

}
