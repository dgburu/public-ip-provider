package com.dgbsoft.pip.login;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InitialServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static Logger logger = Logger.getLogger(InitialServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		logger.info("doGet");
		
		if (req.getSession().getAttribute("login") == null) {
			logger.info("not logged");
			RequestDispatcher rs = req.getRequestDispatcher("Login");
			rs.include(req, resp);
		} else if (req.getSession().getAttribute("login").equals("admin")) {
			resp.getWriter().append("<html><body>");
			resp.getWriter().append("<ul>");
			resp.getWriter().append("<li><a href=\"/publicipprovider?op=get\">Get Public IP</a></li>");
			resp.getWriter().append("<li><a href=\"/publicipprovider?op=set\">Set public IP</a></li>");
			resp.getWriter().append("<li><a href=\"/fileserverWrapper?op=fl\">See Film List</a></li>");
			resp.getWriter().append("</ul>");
			resp.getWriter().append("</body></html>");
		} else if (req.getSession().getAttribute("login").equals("user")) {
			resp.getWriter().append("<html><body>");
			resp.getWriter().append("<ul>");
			resp.getWriter().append("<li><a href=\"/fileserverWrapper?op=fl\">See Film List</a></li>");
			resp.getWriter().append("</ul>");
			resp.getWriter().append("</body></html>");
		}
		
		logger.info("exit doGet");
	}
	
}
