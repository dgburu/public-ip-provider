package com.dgbsoft.apps.fileserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

import com.dgbsoft.core.services.IFileServerAction;

public class FileServerProcessor implements Runnable {

	private final static Logger LOG = Logger.getLogger(FileServerProcessor.class.getName());

	private Socket socket = null;
	private List<FileServerProcessor> processors = null;
	private boolean stopProcess = false;
	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	private IFileServerAction currentAction = null;
	
	@Override
	public void run() {
		if (socket != null) {
			String remoteIp = socket.getInetAddress().getHostAddress();
			processors.add(this);
			LOG.fine("Processing orders from remote ip " + remoteIp);
			
			try {
				inputStream = socket.getInputStream();
			} catch (IOException e1) {
				LOG.severe("Cannot create input stream");
			}
			
			try {
				outputStream = socket.getOutputStream();
			} catch (IOException e1) {
				LOG.severe("Cannot create output stream");
			}

			if (outputStream != null && inputStream != null) {
				Protocol protocol = Protocol.getProtocol(socket.getInetAddress(), inputStream, outputStream);
				while (!stopProcess) {
					currentAction = protocol.getAction();
					if (currentAction != null) {
						stopProcess = currentAction.perform();
					} else {
						stopProcess = true;
					}
				}
			}
			
			processors.remove(this);
			processors = null;
			try {
				socket.close();
			} catch (IOException e) {
				LOG.severe("Error closing socket of ip " + remoteIp + " , msg = " + e.getMessage());
			}
			socket = null;
		}
	}

	public void setSocket(Socket socket) {
		this.socket = socket;		
	}

	public void setProcessors(List<FileServerProcessor> processors) {
		this.processors = processors;
	}

	public void stop() {
		stopProcess = true;
		if (currentAction != null) {
			currentAction.stop();
		}
	}

}
