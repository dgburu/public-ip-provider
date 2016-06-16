package com.dgbsoft.apps.fileserver.http.actions;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.logging.Logger;

import com.dgbsoft.core.services.IFileProviderService;
import com.dgbsoft.core.services.ServicesUtil;

public class UpdateFileListAction extends BaseAction {

	private final static Logger LOG = Logger.getLogger(UpdateFileListAction.class.getName());

	@Override
	public boolean perform() {
		IFileProviderService service = ServicesUtil.getService(IFileProviderService.class);
		if (service != null) {
			service.getFileList(true);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
			try {
				writer.append("HTTP/1.0 200 OK\r\n");
				writer.append("Content-Type: text/plain\r\n");
				writer.append("Date: " + new Date() + "\r\n" + "Server: DgbSoft File server 1.0\r\n\r\n");
				writer.append("OK\r\n");
				writer.flush();
			} catch (IOException e) {
				LOG.severe("Cannot update file list, msg = " + e.getMessage());
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

	@Override
	public String getActionId() {
		return "UPDATEFILELIST";
	}

}
