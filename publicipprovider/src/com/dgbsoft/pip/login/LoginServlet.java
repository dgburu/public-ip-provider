package com.dgbsoft.pip.login;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgbsoft.pip.provider.user.UserCheck;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = -5988394688963478165L;

	public static Logger logger = Logger.getLogger(LoginServlet.class.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		logger.info("doPost");
		if ((req.getParameter("password") != null)  && UserCheck.getInstance().checkUser(req, getServletContext())) {
			logger.info("usr type " + req.getSession().getAttribute("login"));
			resp.sendRedirect("/initial");
		} else {
			logger.info("no usr");
			req.getSession().setAttribute("login", null);
			RequestDispatcher rs = req.getRequestDispatcher("/index.html");
			rs.include(req, resp);
		}

		logger.info("exit doPost");
	}
}
