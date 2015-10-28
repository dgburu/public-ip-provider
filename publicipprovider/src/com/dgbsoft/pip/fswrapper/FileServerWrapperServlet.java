package com.dgbsoft.pip.fswrapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
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
			ServerConnection connection = connectToServer();
			connection.write("GETFILELIST");

			String message = connection.read();

			EntityManager em = DataStoreFactoryService.get().createEntityManager();
			try {
				ServerIPData data = em.find(ServerIPData.class, ServerIPData.KEY_IP_DATA);
				if (data == null) {
					resp.getWriter().append(message);
					resp.getWriter().println("NOK");
					logger.info("nok");
				} else {
					resp.getWriter().println("<html><body>");
					StringTokenizer st = new StringTokenizer(message, "\n");
					while (st.hasMoreTokens()) {
						String line = st.nextToken();
						resp.getWriter().println("<a href=\"http://" + data.getIp() + ":" +connection.getPort() + "/" + line + "\">" + line + "</a><br>");
					}
					resp.getWriter().println("</body></html>");
				}
			} catch (Exception e) {
				logger.warning(e.toString());
				resp.getWriter().println("NOK");
			} finally {
				em.close();
			}
		} else if (operation.equals("ufl")) {
			ServerConnection connection = connectToServer();
			connection.write("UPDATEFILELIST");
			String message = connection.read();
			resp.getWriter().append(message);
		} else {
			logger.warning("nop");
			resp.getWriter().println("UNKNOWN OP");
		}
		
        logger.info("exit doGet");
	}
	
	private ServerConnection connectToServer() {
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
		Socket socket = null;
		ServerConnection serverConnection = null;
		try {
			data = em.find(ServerIPData.class, ServerIPData.KEY_IP_DATA);
			socket = new Socket();
			socket.connect(InetSocketAddress.createUnresolved(data.getIp(), port));
			serverConnection = new ServerConnection(socket);
			serverConnection.setPort(port);
		} catch(Exception e) {
			logger.severe("Problems getting socket input/output " + e.getMessage());
			socket = null;
			serverConnection = null;
		} finally {
			em.close();
		}
		return serverConnection;
	}
}
