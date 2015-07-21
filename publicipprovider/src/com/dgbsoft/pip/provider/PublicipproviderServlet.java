package com.dgbsoft.pip.provider;

import java.io.IOException;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgbsoft.pip.provider.data.ServerIPData;

@SuppressWarnings("serial")
public class PublicipproviderServlet extends HttpServlet {
	
	public static Logger logger = Logger.getLogger(PublicipproviderServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		logger.entering(getServletName(), "doGet");
		String remoteAddress = req.getRemoteAddr();
		String operation = req.getParameter("op");
		if (operation.equals("set")) {
			// store in DB
			logger.info("set operation");
			PersistenceManager pm = JDOHelper.getPersistenceManagerFactory("transactions-optional").getPersistenceManager();

			ServerIPData data = null;
			
			try {
				try {
					data = pm.getObjectById(ServerIPData.class, ServerIPData.KEY_IP_DATA);
					data.setIp(remoteAddress);
					data.setTime(System.currentTimeMillis());
				} catch (JDOObjectNotFoundException e) {
					data = new ServerIPData(remoteAddress, System.currentTimeMillis());
					pm.makePersistent(data);
				}
				logger.fine("ok");
				resp.getWriter().println("OK");
			} catch (Exception e) {
				logger.severe(e.toString());
				resp.getWriter().println("NOK");
			} finally {
				pm.close();
			}
			
			/*
			Key key = KeyFactory.createKey("ipKey", 1); 
			Entity entity = new Entity(key);
			entity.setProperty("ip", remoteAddress);
			entity.setProperty("time", System.currentTimeMillis());
			DatastoreService dataService =  DatastoreServiceFactory.getDatastoreService();
			dataService.put(entity);
			resp.getWriter().println("OK");
			*/
		} else if (operation.equals("get")) {
			// get from DB
			logger.info("get operation");
			PersistenceManager pm = JDOHelper.getPersistenceManagerFactory("transactions-optional").getPersistenceManager();

			ServerIPData data = null;
			
			try {
				data = pm.getObjectById(ServerIPData.class, ServerIPData.KEY_IP_DATA);
				resp.getWriter().println(data.getIp() + "\n" + data.getTime());
				logger.fine("ok");
			} catch (Exception e) {
				logger.severe(e.toString());
				resp.getWriter().println("NOK");
			} finally {
				pm.close();
			}
			
			/*
			Key key = KeyFactory.createKey("ipKey", 1); 
			DatastoreService dataService =  DatastoreServiceFactory.getDatastoreService();
			try {
				Entity entity = dataService.get(key);
				String ip = (String) entity.getProperty("ip");
				Long time = (Long) entity.getProperty("time");
				resp.getWriter().println(ip + "\n" + time.toString());
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
				resp.getWriter().println("NOK");
			}
			*/
		} else {
			logger.warning("nop");
			resp.getWriter().println("UNKNOWN OP");
		}
		logger.exiting(getServletName(), "doGet");
	}
	
}
