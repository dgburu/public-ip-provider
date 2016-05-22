package com.dgbsoft.apps.fileprovider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.dgbsoft.core.services.IFileProviderService;
import com.dgbsoft.core.services.ITimerListener;
import com.dgbsoft.core.services.ITimerService;
import com.dgbsoft.core.services.MovieInfo;
import com.dgbsoft.core.services.ServicesUtil;

public class FileProviderService implements IFileProviderService {

	private final static Logger LOG = Logger.getLogger(FileProviderService.class.getName());

	private Map<String,Path> filesDB = new HashMap<String, Path>();

	public FileProviderService() {
		ITimerService timer = ServicesUtil.getService(ITimerService.class);
		if (timer != null) {
			timer.addTimerListener(new ITimerListener() {
				@Override
				public int getPeriod() {
					return 43200000; //5 hours
				}
				@Override
				public void timerEvent() {
					getFileList(true);
				}
				@Override
				public Date getDate() {
					LocalTime localTime = LocalTime.of(0, 0);
					LocalDateTime localDate = localTime.atDate(LocalDate.now());
					Instant instant = localDate.atZone(ZoneId.systemDefault()).toInstant();
					return Date.from(instant);
				}
			});
		}
	}
	
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

	@Override
	public MovieInfo getFilmInfo(String fileName) {
		return null;
	}

}
