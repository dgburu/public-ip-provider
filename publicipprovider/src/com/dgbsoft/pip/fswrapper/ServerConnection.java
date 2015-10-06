package com.dgbsoft.pip.fswrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerConnection {

	public static Logger logger = Logger.getLogger(ServerConnection.class.getName());

	private Socket socket = null;
	private PrintWriter writer = null;
	private BufferedReader reader = null;
	
	public ServerConnection(Socket socket) {
		this.socket = socket;
		initialize();
	}
	
	public void write(String message) {
		if (writer != null) {
			writer.append(message);
			writer.flush();
		}
	}
	
	public String read() {
		String message = "";
		if (reader != null) {
			try {
				String temp = null;
				while((temp = reader.readLine()) != null) {
					message += temp;
				}
			} catch (IOException e) {
				logger.severe("error reading inputstream " + e.getMessage());
			}
		}
		return message;
	}
	
	public void initialize() {
		if (socket != null) {
			try {
				writer = new PrintWriter(socket.getOutputStream());
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				logger.severe("error getting outputstream " + e.getMessage());
			}
		} 
	}
	
	public void close() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			logger.severe("error closing socket " + e.getMessage());
		}
	}
}
