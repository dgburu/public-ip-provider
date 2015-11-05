package com.dgbsoft.pip.fswrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLConnection;
import java.util.logging.Logger;

public class ServerConnection {

	public static Logger logger = Logger.getLogger(ServerConnection.class.getName());

	private URLConnection con = null;
	private PrintWriter writer = null;
	private BufferedReader reader = null;
	private int port = 15551;
	
	public ServerConnection(URLConnection con) {
		this.con = con;
		initialize();
	}
	
	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
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
		if (con != null) {
			try {
				writer = new PrintWriter(con.getOutputStream());
				logger.info("output stream created successfully");
			} catch (IOException e) {
				logger.severe("error getting outputstream " + e.getMessage());
				FileServerWrapperServlet.logStackTrace(e);
			}
			try {
				reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				logger.info("input streams created successfully");
			} catch (IOException e) {
				logger.severe("error getting inputstream " + e.getMessage());
				FileServerWrapperServlet.logStackTrace(e);
			}
		} else {
			logger.severe("connection is null");
		}
	}
	
	public void close() {
		if (writer != null) {
			writer.close();
		}
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				logger.severe("cannot close output stream");
			}
		}
	}
}
