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
			resp.getWriter().append("<dl>");
			resp.getWriter().append("<dt><b>IP</b></dt>");
			resp.getWriter().append("<ul>");
			resp.getWriter().append("<li type=\"disc\"><a href=\"/publicipprovider?op=get\">Get Public IP</a></li>");
			resp.getWriter().append("</ul>");
			resp.getWriter().append("<dt><b>Download</b></dt>");
			resp.getWriter().append("<ul>");
			resp.getWriter().append("<li type=\"disc\"><a href=\"/fileserverWrapper?op=as\">Start Downloads</a></li>");
			resp.getWriter().append("<li type=\"disc\"><a href=\"/fileserverWrapper?op=ap\">Stop Downloads</a></li>");
			resp.getWriter().append("</ul>");
			resp.getWriter().append("<dt><b>System</b></dt>");
			resp.getWriter().append("<ul>");
			resp.getWriter().append("<li type=\"disc\"><a href=\"/fileserverWrapper?op=tp\">Get Temperature</a></li>");
			resp.getWriter().append("<li type=\"disc\"><a href=\"/fileserverWrapper?op=sda\">Stop FileServer</a></li>");
			resp.getWriter().append("</ul>");
			/*
			resp.getWriter().append("<dt><b>Film</b></dt>");
			resp.getWriter().append("<ul>");
			resp.getWriter().append("<li type=\"disc\"><a href=\"/fileserverWrapper?op=fl\">See Film List</a></li>");
			resp.getWriter().append("</ul>");
			*/
			resp.getWriter().append("</body></html>");
		} else if (req.getSession().getAttribute("login").equals("user")) {
			resp.getWriter().append("<html><body>");
			resp.getWriter().append("<ul>");
			resp.getWriter().append("<li type=\"disc\"><a href=\"/fileserverWrapper?op=fl\">See Film List</a></li>");
			resp.getWriter().append("</ul>");
			resp.getWriter().append("</body></html>");
		} else {
			resp.getWriter().append("<html><body>");
			resp.getWriter().append("User has no rights");
			resp.getWriter().append("</body></html>");			
		}
		
		logger.info("exit doGet");
	}
	
}
