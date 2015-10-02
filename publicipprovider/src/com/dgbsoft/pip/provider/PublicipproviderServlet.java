package com.dgbsoft.pip.provider;

import java.io.IOException;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgbsoft.pip.provider.data.DataStoreFactoryService;
import com.dgbsoft.pip.provider.data.ServerIPData;
import com.dgbsoft.pip.provider.user.UserCheck;

@SuppressWarnings("serial")
public class PublicipproviderServlet extends HttpServlet {
	
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

		EntityManager em = DataStoreFactoryService.get().createEntityManager();
		String remoteAddress = req.getRemoteAddr();
		
		if (operation.equals("set")) {
			// store in DB
			logger.info("set operation");

			ServerIPData data = null;
			
			try {
				data = em.find(ServerIPData.class, ServerIPData.KEY_IP_DATA);
				if (data != null) {	
					data.setIp(remoteAddress);
					data.setTime(System.currentTimeMillis());
				} else {
					data = new ServerIPData(remoteAddress, System.currentTimeMillis());
					em.persist(data);
				}
				logger.info("ok");
				resp.getWriter().println("OK");
			} catch (Exception e) {
				logger.warning(e.toString());
				resp.getWriter().println("NOK");
			} finally {
				em.close();
			}
		} else if (operation.equals("get")) {
			// get from DB
			logger.info("get operation");

			ServerIPData data = null;
			
			try {
				data = em.find(ServerIPData.class, ServerIPData.KEY_IP_DATA);
				if (data == null) {
					resp.getWriter().println("NOK");
					logger.info("nok");
				} else {
					resp.getWriter().println(data.getIp() + "\n" + data.getTime());
					logger.info("ok");
				}
			} catch (Exception e) {
				logger.warning(e.toString());
				resp.getWriter().println("NOK");
			} finally {
				em.close();
			}
		} else {
			logger.warning("nop");
			resp.getWriter().println("UNKNOWN OP");
		}
        
        logger.info("exit doGet");
	}
	
}
