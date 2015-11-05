package com.dgbsoft.apps.fileprovider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.dgbsoft.core.services.IFileProviderService;

public class FileProviderService implements IFileProviderService {

	private final static Logger LOG = Logger.getLogger(FileProviderService.class.getName());

	private Map<String,Path> filesDB = new HashMap<String, Path>();
	
	@Override
	public InputStream getFile(String fileName) {
		LOG.finer("Getting file = " + fileName);
		Path path = filesDB.get(fileName);
		if (path != null) {
			try {
				LOG.finest("File located at " + path.toString());
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
		LOG.finer("Getting file list, update = " + update);
		Properties config = new Properties();
		try (FileInputStream configIS = new FileInputStream("fileserver.properties")) {
			config.load(configIS);
		} catch (IOException e) {
			LOG.severe("Cannot read configuration file, msg =" + e);
		}
		String pathsStr = config.getProperty("PATHS", "");
		
		if (update || filesDB.isEmpty()) {
			LOG.finest("updating file list");
			filesDB.clear();
	
			StringTokenizer st = new StringTokenizer(pathsStr, ";");
			while (st.hasMoreTokens()) {
				String pathStr = st.nextToken();
				Path path = Paths.get(pathStr);
				getFiles(path, path);
			}
		}
		Map<String,Path> ordered = new TreeMap<String,Path>(filesDB);
		return ordered.keySet();
	}

	private void getFiles(Path path, Path basePath) {
		if (Files.exists(path) && Files.isDirectory(path)) {
			File folder = path.toFile();
			File [] files = folder.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						Path relativePath = basePath.relativize(file.toPath());
						filesDB.put(relativePath.toString(), file.toPath());
						LOG.finest("added file " + relativePath.toString() + " at " + file.toPath().toString());
					} else if (file.isDirectory()) {
						getFiles(file.toPath(), basePath);
					}
				}
			}
		}
	}
	
	@Override
	public Path getFilePath(String fileName) {
		return filesDB.get(fileName);
	}

}
