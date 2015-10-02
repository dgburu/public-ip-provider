package com.dgbsoft.apps.fileserver.http.actions;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;

import com.dgbsoft.apps.fileserver.http.IStreamProvider;
import com.dgbsoft.core.services.IFileProviderService;
import com.dgbsoft.core.services.ServicesUtil;

public class UpdateFileListAction extends BaseAction {

	private final static Logger LOG = Logger.getLogger(UpdateFileListAction.class.getName());

	public UpdateFileListAction(IStreamProvider streamProvider) {
		super(streamProvider);
	}

	@Override
	public boolean perform() {
		IFileProviderService service = ServicesUtil.getService(IFileProviderService.class);
		if (service != null) {
			service.getFileList(true);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
			try {
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

}
