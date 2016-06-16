package com.dgbsoft.core.services.misc;

import java.io.InputStream;
import java.io.OutputStream;

public interface IStreamProvider {

	public InputStream getInputStream();
	public OutputStream getOutputStream();
	
}
