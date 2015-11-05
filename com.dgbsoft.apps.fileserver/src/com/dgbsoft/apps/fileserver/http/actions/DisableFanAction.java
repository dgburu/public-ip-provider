package com.dgbsoft.apps.fileserver.http.actions;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.logging.Logger;

import com.dgbsoft.apps.fileserver.IAction;
import com.dgbsoft.apps.fileserver.http.IStreamProvider;
import com.dgbsoft.core.services.IRpiGpioService;
import com.dgbsoft.core.services.ServicesUtil;

public class DisableFanAction extends BaseAction {

	private final static Logger LOG = Logger.getLogger(DisableFanAction.class.getName());

	public DisableFanAction(IStreamProvider streamProvider) {
		super(streamProvider);
	}

	@Override
	public boolean perform() {
		IRpiGpioService service = ServicesUtil.getService(IRpiGpioService.class);
		if (service != null) {
			service.pinToLow(IRpiGpioService.PIN_40);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
			try {
			    writer.append("HTTP/1.0 200 OK\r\n");
		    	writer.append("Content-Type: text/plain\r\n");
			    writer.append("Date: " + new Date() + "\r\n" + "Server: DgbSoft File server 1.0\r\n\r\n");
				writer.append("OK\r\n");
				writer.flush();
			} catch (IOException e) {
				LOG.severe("Cannot send ok message , msg = " + e.getMessage());
			}
		} else {
			LOG.severe("No rpi provider service available");
		}
		return true;
	}

	@Override
	public void stop() {
	}

}
