package com.dgbsoft.apps.ipsetter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Logger;

import com.dgbsoft.core.services.ITimerListener;

public class IpSetter implements ITimerListener {

	private final static Logger LOG = Logger.getLogger(IpSetter.class.getName());

	@Override
	public int getPeriod() {
		return 240_000; //4 minutes
	}

	@Override
	public void timerEvent() {
		LOG.info("BEGIN at " + SimpleDateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
		URL url;
		InputStream in = null;
		try {
			Properties config = new Properties();
			config.load(new FileInputStream("ipsetterconfig.properties"));
			LOG.fine("Connecting to " + config.getProperty("URL"));
			url = new URL(config.getProperty("URL") + "&usr=" + config.getProperty("USER"));
			config.clear();
			in = url.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String resp = "";
			String tempResp = "";
			while (tempResp != null) {
				resp += tempResp;
				tempResp = reader.readLine();
			}
			LOG.fine("Response=" + resp);
		} catch (IOException e) {
			LOG.severe("Error setting ip " + e.getMessage());
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					LOG.severe("Error closing file");
				}
			}
		}
		LOG.info("END at " + SimpleDateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
	}

}
