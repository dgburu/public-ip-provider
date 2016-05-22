package com.dgbsoft.pip.provider.user;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.dgbsoft.pip.provider.data.BlockedIp;
import com.dgbsoft.pip.provider.data.DataStoreFactoryService;

public class UserCheck {

	public static Logger logger = Logger.getLogger(UserCheck.class.getName());

	private static UserCheck INSTANCE = new UserCheck();
	
	public static UserCheck getInstance() {
		return INSTANCE;
	}
	
	public boolean checkUser(HttpServletRequest req, ServletContext context) {
		EntityManager em = DataStoreFactoryService.get().createEntityManager();

		String user = req.getParameter("usr");
        String remoteAddress = req.getRemoteAddr();

        if (user == null) {
			try {
				BlockedIp blockedIp = em.find(BlockedIp.class, remoteAddress);
				if (blockedIp == null) {	
					blockedIp = new BlockedIp(remoteAddress);
					em.persist(blockedIp);
				}
				logger.info("nok, bocked ip " + remoteAddress);
			} catch (Exception e) {
				logger.info("nok");
				logger.warning(e.toString());
			}

            return false;
        } else {
			try {
				BlockedIp blockedIp = em.find(BlockedIp.class, remoteAddress);
				if (blockedIp != null) {	
					logger.info("nok, bocked ip " + remoteAddress);
					return false;
				}
			} catch (Exception e) {
				logger.info("nok, cannot check ip");
				logger.warning(e.toString());
				return false;
			}

			InputStream usersStream = context.getResourceAsStream("/WEB-INF/users.properties");
        	Properties allowedUsers = new Properties();
        	try {
        		allowedUsers.load(usersStream);
        	} catch (IOException e) {
        		logger.severe("error loading users" + e.getMessage());
        		return false;
        	}
        	if (!allowedUsers.containsKey(user)) {
    			try {
    				BlockedIp blockedIp = em.find(BlockedIp.class, remoteAddress);
    				if (blockedIp == null) {	
    					blockedIp = new BlockedIp(remoteAddress);
    					em.persist(blockedIp);
    				}
        			logger.warning("invalid user = " + user + ", rip = " + remoteAddress);
    			} catch (Exception e) {
    				logger.info("nok cannot check ip");
    				logger.warning(e.toString());
    			}

    			return false;
        	} else {
        		logger.info("logged user = " + user);
        		String type = allowedUsers.getProperty(user);
        		req.getSession().setAttribute("login", type);
        		String pass = req.getParameter("password");
        		if ("ipsetter".equals(type)) {
            		logger.info("user " + user + "is ipsetter type");
            		return true;
        		}
            	if (!allowedUsers.containsKey(user + ".pwd")) {
    				logger.severe("no passwd set for this user");
            		return false;
            	} else {
            		if (allowedUsers.getProperty(user + ".pwd", "").equals(pass)) {
                		req.getSession().setAttribute("password", pass);
                		return true;
            		} else {
        				logger.severe("passwd does not match for user " + user);
                		return false;
            		}
            	}
        	}
        }
	}
	
}
