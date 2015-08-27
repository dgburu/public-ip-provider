package com.dgbsoft.core.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.Set;

public interface IFileServerService {

	boolean start();
	
	boolean stop();
	
	InputStream getFile(String fileName);
	
	Set<String> getFileList(boolean update);
	
}
