package com.dgbsoft.pip.fswrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;

import com.dgbsoft.pip.provider.data.DataStoreFactoryService;
import com.dgbsoft.pip.provider.data.ServerIPData;

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

	public static ServerConnection connectToServer(String requestData, ServletContext context) {
		InputStream serverStream = context.getResourceAsStream("/WEB-INF/server.properties");
		Properties serverProps = new Properties();
		int port = 15551;
		try {
			serverProps.load(serverStream);
			port = Integer.parseInt(serverProps.getProperty("port", "15551"));
		} catch (Exception e) {
			logger.warning("error loading server properties" + e.getMessage());
		}
		EntityManager em = DataStoreFactoryService.get().createEntityManager();

		ServerIPData data = null;
		ServerConnection serverConnection = null;
		try {
			data = em.find(ServerIPData.class, ServerIPData.KEY_IP_DATA);
			InetSocketAddress address = InetSocketAddress.createUnresolved(data.getIp(), port);

			logger.info("connecting to address = " + address.toString());
			URL url = new URL("http://" + address.toString() + "/" + requestData);
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.connect();
			connection.setReadTimeout(30000);
			serverConnection = new ServerConnection(connection);
			serverConnection.setPort(port);
		} catch (Exception e) {
			logger.severe("Problems getting socket input/output " + e.getMessage());
			serverConnection = null;
		} finally {
			em.close();
		}
		return serverConnection;
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
				while ((temp = reader.readLine()) != null) {
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
