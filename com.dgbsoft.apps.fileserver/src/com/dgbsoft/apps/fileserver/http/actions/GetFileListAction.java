package com.dgbsoft.apps.fileserver.http.actions;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.logging.Logger;

import com.dgbsoft.apps.fileserver.IAction;
import com.dgbsoft.apps.fileserver.http.IStreamProvider;
import com.dgbsoft.core.services.IFileProviderService;
import com.dgbsoft.core.services.ServicesUtil;

public class GetFileListAction extends BaseAction {

	private final static Logger LOG = Logger.getLogger(GetFileListAction.class.getName());

	public GetFileListAction(IStreamProvider streamProvider) {
		super(streamProvider);
	}

	@Override
	public boolean perform() {
		StringBuffer message = new StringBuffer();
		IFileProviderService service = ServicesUtil.getService(IFileProviderService.class);
		if (service != null) {
			Set<String> fileList = service.getFileList(false);
			for (String fileName : fileList) {
				message.append(fileName + "\r\n");
			}
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getOutputStream()));
			try {
				writer.append(message);
				LOG.finest(message.toString());
				writer.flush();
			} catch (IOException e) {
				LOG.severe("Cannot send file list, msg = " + e.getMessage());
			}
		} else {
			LOG.severe("No file provider service available");
		}
		return true;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
