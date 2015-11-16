package com.dgbsoft.pip.fswrapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgbsoft.pip.provider.data.DataStoreFactoryService;
import com.dgbsoft.pip.provider.data.ServerIPData;

public class FileServerWrapperServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static Logger logger = Logger.getLogger(FileServerWrapperServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("doGet");

		if (req.getSession().getAttribute("login") == null) {
	        logger.info("No session usr");
	        RequestDispatcher rs = req.getRequestDispatcher("/index.html");
	        rs.include(req, resp);
	        logger.info("exit doGet");
			return;
		}
		
		String operation = req.getParameter("op");
		if (operation == null) {
			logger.warning("no operation");
			resp.getWriter().println("NOK");
	        logger.info("exit doGet");
			return;
		}

		if (operation.equals("fl")) {
			ServerConnection connection = connectToServer("GETFILELIST");
			if (connection == null) {
				resp.getWriter().println("NOK");
				logger.info("nok");
		        logger.info("exit doGet");
				return;
			}

			String message = connection.read();

			connection.close();
			
			EntityManager em = DataStoreFactoryService.get().createEntityManager();
			try {
				ServerIPData data = em.find(ServerIPData.class, ServerIPData.KEY_IP_DATA);
				if (data == null) {
					resp.getWriter().append(message);
					resp.getWriter().println("NOK");
					logger.info("nok");
			        logger.info("exit doGet");
					return;
				} else {
					resp.getWriter().println("<html><body>\n");
					StringTokenizer st = new StringTokenizer(message, ";");
					resp.getWriter().println("<ol>");
					while (st.hasMoreTokens()) {
						String line = st.nextToken();
						String name = line;
						if (req.getSession().getAttribute("password") != null) {
							resp.getWriter().println("<li><a href=\"ftp://invitado:" + req.getSession().getAttribute("password") + "@" + data.getIp() + "/" + line + "\">" + name + "</a></li>\n");
						} else {
							resp.getWriter().println("<li><a href=\"ftp://" + data.getIp() + "/" + line + "\">" + name + "</a></li>\n");
						}
					}
					resp.getWriter().println("</ol>");
					resp.getWriter().println("</body></html>");
				}
			} catch (Exception e) {
				logger.warning(e.toString());
				resp.getWriter().println("NOK");
			} finally {
				em.close();
			}
		} else if (operation.equals("ufl")) {
			ServerConnection connection = connectToServer("UPDATEFILELIST");
			String message = connection.read();
			connection.close();
			resp.getWriter().append(message);
		} else if (operation.equals("sda")) {
			ServerConnection connection = connectToServer("SHUTDOWNALL");
			connection.close();
			resp.getWriter().append("OK");
		} else if (operation.equals("df")) {
			ServerConnection connection = connectToServer("DISABLEFAN");
			String message = connection.read();
			connection.close();
			resp.getWriter().append(message);
		} else if (operation.equals("ef")) {
			ServerConnection connection = connectToServer("ENABLEFAN");
			String message = connection.read();
			connection.close();
			resp.getWriter().append(message);
		} else if (operation.equals("as")) {
			ServerConnection connection = connectToServer("STARTAMULE");
			String message = connection.read();
			connection.close();
			resp.getWriter().append(message);
		} else if (operation.equals("ap")) {
			ServerConnection connection = connectToServer("STOPAMULE");
			String message = connection.read();
			connection.close();
			resp.getWriter().append(message);
		} else if (operation.equals("tp")) {
			ServerConnection connection = connectToServer("GETTEMPERATURE");
			String message = connection.read();
			connection.close();
			resp.getWriter().append(message);
		} else {
			logger.warning("nop");
			resp.getWriter().println("UNKNOWN OP");
		}
		
        logger.info("exit doGet");
	}
	
	private ServerConnection connectToServer(String requestData) {
		InputStream serverStream = getServletContext().getResourceAsStream("/WEB-INF/server.properties");
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
			serverConnection = new ServerConnection(connection);
			serverConnection.setPort(port);
		} catch(Exception e) {
			logger.severe("Problems getting socket input/output " + e.getMessage());
			logStackTrace(e);
			serverConnection = null;
		} finally {
			em.close();
		}
		return serverConnection;
	}
	
	public static void logStackTrace(Throwable e) {
		String stackTrace = "";
		StackTraceElement[] traces = e.getStackTrace();
		for (StackTraceElement ele : traces) {
			stackTrace += ele.getClassName() + "." + ele.getMethodName() + ":" + ele.getLineNumber() + "\n";
		}
		logger.severe(stackTrace);
	}
}
