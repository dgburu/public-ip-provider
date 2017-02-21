package com.dgbsoft.apps.fileserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.dgbsoft.apps.fileserver.Protocol;
import com.dgbsoft.core.services.IFileServerAction;
import com.dgbsoft.core.services.ServicesUtil;
import com.dgbsoft.core.services.misc.IStreamProvider;

public class HttpProtocol extends Protocol implements IStreamProvider {

	private final static Logger LOG = Logger.getLogger(HttpProtocol.class.getName());

	private InputStream inputStream = null;
	private OutputStream outputStream = null;

	public HttpProtocol(InetAddress clientAddress, InputStream inputStream, OutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	@Override
	public IFileServerAction getAction() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		try {
			line = reader.readLine();
			LOG.fine(() -> "Http request = " + line);
			if (line != null) {
				List<IFileServerAction> services = ServicesUtil.getServices(IFileServerAction.class);
				Optional<IFileServerAction> service = services.stream().filter(s -> line.contains(s.getActionId()))
						.findFirst();
				if (service.isPresent()) {
					LOG.fine(() -> "Action=" + service.get().getActionId());
					service.get().setStreamProvider(this);
					return service.get();
				} else {
					LOG.severe("Bad request");
				}
			}
		} catch (IOException e) {
			LOG.severe(() -> "Cannot get line, msg = " + e.getMessage());
		}
		return null;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

}
