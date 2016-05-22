package com.dgbsoft.core.services;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Set;

public interface IFileProviderService {

	public InputStream getFile(String fileName);
	
	public Set<String> getFileList(boolean update);
	
	public Path getFilePath(String fileName);
	
	public MovieInfo getFilmInfo(String fileName);

}
