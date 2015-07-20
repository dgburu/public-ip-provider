package com.dgbsoft.pip.provider;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class PublicipproviderServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String remoteAddress = req.getRemoteAddr();
		String operation = req.getParameter("op");
		if (operation.equals("set")) {
			// store in DB
			Key key = KeyFactory.createKey("ipKey", 1); 
			Entity entity = new Entity(key);
			entity.setProperty("ip", remoteAddress);
			entity.setProperty("time", System.currentTimeMillis());
			DatastoreService dataService =  DatastoreServiceFactory.getDatastoreService();
			dataService.put(entity);
			resp.getWriter().println("OK");
		} else if (operation.equals("get")) {
			// TODO get from DB
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
		} else {
			resp.getWriter().println("UNKNOWN OP");
		}
	}
	
}
