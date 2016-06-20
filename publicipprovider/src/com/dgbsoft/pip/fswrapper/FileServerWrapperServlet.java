package com.dgbsoft.pip.fswrapper;

import java.io.IOException;
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

/**
 * File format for /WEB-INF/server.properties port=<port number>
 * 
 * 
 * @author dgbsoft
 *
 */
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
			ServerConnection connection = ServerConnection.connectToServer("GETFILELIST", getServletContext());
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
							resp.getWriter()
									.println("<li><a href=\"ftp://invitado:" + req.getSession().getAttribute("password")
											+ "@" + data.getIp() + "/" + line + "\">" + name + "</a></li>\n");
						} else {
							resp.getWriter().println(
									"<li><a href=\"ftp://" + data.getIp() + "/" + line + "\">" + name + "</a></li>\n");
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
			ServerConnection connection = ServerConnection.connectToServer("UPDATEFILELIST", getServletContext());
			String message = connection.read();
			connection.close();
			resp.getWriter().append(message);
		} else if (operation.equals("sda")) {
			ServerConnection connection = ServerConnection.connectToServer("SHUTDOWNALL", getServletContext());
			connection.close();
			resp.getWriter().append("OK");
		} else if (operation.equals("df")) {
			ServerConnection connection = ServerConnection.connectToServer("DISABLEFAN", getServletContext());
			String message = connection.read();
			connection.close();
			resp.getWriter().append(message);
		} else if (operation.equals("ef")) {
			ServerConnection connection = ServerConnection.connectToServer("ENABLEFAN", getServletContext());
			String message = connection.read();
			connection.close();
			resp.getWriter().append(message);
		} else if (operation.equals("as")) {
			ServerConnection connection = ServerConnection.connectToServer("STARTAMULE", getServletContext());
			String message = connection.read();
			connection.close();
			resp.getWriter().append(message);
		} else if (operation.equals("ap")) {
			ServerConnection connection = ServerConnection.connectToServer("STOPAMULE", getServletContext());
			String message = connection.read();
			connection.close();
			resp.getWriter().append(message);
		} else if (operation.equals("tp")) {
			ServerConnection connection = ServerConnection.connectToServer("GETTEMPERATURE", getServletContext());
			String message = connection.read();
			connection.close();
			resp.getWriter().append(message);
		} else {
			logger.warning("nop");
			resp.getWriter().println("UNKNOWN OP");
		}

		logger.info("exit doGet");
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
