package com.dgbsoft.apps.fileserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import com.dgbsoft.core.services.IFileServerService;

public class FileServerService implements IFileServerService {

	private final static Logger LOG = Logger.getLogger(FileServerService.class.getName());

	private Map<String,Path> filesDB = new HashMap<String, Path>();
	private boolean serverRunning = false;
	private Map<String, FileServerProcessor> processors = new HashMap<String, FileServerProcessor>();
	
	@Override
	public boolean start() {
		Properties config = new Properties();
		try (FileInputStream configIS = new FileInputStream("fileserver.properties")) {
			config.load(configIS);
		} catch (IOException e) {
			LOG.severe("Cannot read configuration file, msg =" + e);
		}

		int port = 15551;
		String portStr = config.getProperty("port");
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
		serverRunning = false;
		for (FileServerProcessor processor : processors.values()) {
			processor.stop();
		}
		return true;
	}

	@Override
	public InputStream getFile(String fileName) {
		Path path = filesDB.get(fileName);
		if (path != null) {
			try {
				return Files.newInputStream(path);
			} catch (IOException e) {
				LOG.severe("Cannot get file " + fileName + ", msg =" + e);
			}
		} else {
			LOG.severe("File " + fileName + " does not exists in file database");
		}
		return null;
	}

	@Override
	public Set<String> getFileList(boolean update) {
		Properties config = new Properties();
		try (FileInputStream configIS = new FileInputStream("fileserver.properties")) {
			config.load(configIS);
		} catch (IOException e) {
			LOG.severe("Cannot read configuration file, msg =" + e);
		}
		String pathsStr = config.getProperty("paths", "");
		
		if (update || filesDB.isEmpty()) {
			filesDB.clear();
	
			StringTokenizer st = new StringTokenizer(pathsStr, ";");
			while (st.hasMoreTokens()) {
				String pathStr = st.nextToken();
				Path path = Paths.get(pathStr);
				if (Files.exists(path) && Files.isDirectory(path)) {
					File folder = path.toFile();
					File [] files = folder.listFiles();
					if (files != null) {
						for (File file : files) {
							if (file.isFile()) {
								filesDB.put(file.getName(), file.toPath());
							}
						}
					}
				}
			}
		}
		
		return filesDB.keySet();
	}

}
