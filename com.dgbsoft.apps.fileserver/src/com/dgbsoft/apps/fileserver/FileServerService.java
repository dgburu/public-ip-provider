package com.dgbsoft.apps.fileserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import com.dgbsoft.core.services.IFileServerService;

public class FileServerService implements IFileServerService {

	private final static Logger LOG = Logger.getLogger(FileServerService.class.getName());

	private boolean serverRunning = false;
	private List<FileServerProcessor> processors = new ArrayList<FileServerProcessor>();
	private ServerSocket server = null; 
	
	@Override
	public boolean start() {
		LOG.info("Starting File server");
		if (server != null) {
			LOG.info("Server already started");
			return false;
		}
		
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
		
		try {
			server = new ServerSocket(port);
			LOG.fine("Server initialized on port =" + port);
			serverRunning = true;
			
			(new Thread(new ServerThread())).start();
			
		} catch (IOException e) {
			LOG.severe("Error initializing server on port " + port + ", msg =" + e);
			stop();
			return false;
		}

		return true;
	}

	final class ServerThread implements Runnable {

		@Override
		public void run() {
			while (serverRunning) {
				try {
					Socket socket = server.accept();
					//String remoteIp = socket.getInetAddress().getHostAddress();
					
					FileServerProcessor processor = new FileServerProcessor();
					processor.setSocket(socket);
					processor.setProcessors(processors);
					(new Thread(processor)).start();
				} catch (IOException e) {
					LOG.severe("Server socket exception interrupted probably stopping" + e);
				}
			}
		}
		
	}
	
	@Override
	public boolean stop() {
		LOG.info("Stopping File server");
		serverRunning = false;
		for (FileServerProcessor processor : processors) {
			processor.stop();
		}
		if (server != null) {
			try {
				server.close();
			} catch (IOException e) {
				LOG.severe("Error closing server " + e);
			}
			server = null;
		}
		return true;
	}

}
