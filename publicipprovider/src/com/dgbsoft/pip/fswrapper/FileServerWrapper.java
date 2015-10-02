package com.dgbsoft.pip.fswrapper;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgbsoft.pip.provider.PublicipproviderServlet;
import com.dgbsoft.pip.provider.user.UserCheck;

public class FileServerWrapper extends HttpServlet {

	public static Logger logger = Logger.getLogger(PublicipproviderServlet.class.getName());

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
			
		} else if (operation.equals("ufl")) {
				
		} else {
			logger.warning("nop");
			resp.getWriter().println("UNKNOWN OP");
		}
		
        logger.info("exit doGet");
	}
	
}
