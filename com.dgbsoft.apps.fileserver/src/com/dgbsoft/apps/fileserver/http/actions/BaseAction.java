package com.dgbsoft.apps.fileserver.http.actions;

import java.io.InputStream;
import java.io.OutputStream;

import com.dgbsoft.apps.fileserver.IAction;
import com.dgbsoft.apps.fileserver.http.IStreamProvider;

public abstract class BaseAction implements IAction {

	protected InputStream inputStream;
	protected OutputStream outputStream;
	
	public BaseAction(IStreamProvider streamProvider) {
		inputStream = streamProvider.getInputStream();
		outputStream = streamProvider.getOutputStream();
	}
	
	protected InputStream getInputStream() {
		return inputStream;
	}
	
	protected OutputStream getOutputStream() {
		return outputStream;
	}

}
