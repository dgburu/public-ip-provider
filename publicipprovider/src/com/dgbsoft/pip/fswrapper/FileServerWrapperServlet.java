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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgbsoft.pip.provider.data.DataStoreFactoryService;
import com.dgbsoft.pip.provider.data.ServerIPData;
import com.dgbsoft.pip.provider.user.UserCheck;

public class FileServerWrapperServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static Logger logger = Logger.getLogger(FileServerWrapperServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		logger.info("doGet");

		if (!UserCheck.getInstance().checkUser(req, getServletContext())) {
			resp.getWriter().append("NOK usr");
			return;
		}
		
		String operation = req.getParameter("op");
		if (operation == null) {
			logger.warning("no operation");
			resp.getWriter().println("NOK");
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
					resp.getWriter().println("<ul>");
					while (st.hasMoreTokens()) {
						String line = st.nextToken();
						String name = line;
						if (line.lastIndexOf('/') != -1) {
							name = name.substring(line.lastIndexOf('/'));
						}
						resp.getWriter().println("<li><a href=\"ftp://" + data.getIp() + "/" + line + "\">" + name + "</a></li>\n");
					}
					resp.getWriter().println("</ul>");
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
