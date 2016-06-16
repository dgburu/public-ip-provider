package com.dgbsoft.apps.fileserver.http.actions;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.dgbsoft.core.services.IFileServerAction;
import com.dgbsoft.core.services.misc.IStreamProvider;

public abstract class BaseAction implements IFileServerAction {

	protected InputStream inputStream;
	protected OutputStream outputStream;
	protected Map<String, String> data = new HashMap<>();

	@Override
	public void setData(Map<String, String> dataMap) {
		data = dataMap;
	}

	@Override
	public void setStreamProvider(IStreamProvider streamProvider) {
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
