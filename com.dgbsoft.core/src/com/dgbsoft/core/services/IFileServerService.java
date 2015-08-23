package com.dgbsoft.core.services;

import java.io.BufferedReader;
import java.util.Set;

public interface IFileServerService {

	boolean start();
	
	boolean stop();
	
	BufferedReader getFile(String fileName);
	
	Set<String> getFileList(boolean update);
	
}
