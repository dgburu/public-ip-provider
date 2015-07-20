package ipsetter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class IpSetter {

	public static void setIP() {
		System.out.println("BEGIN at " + SimpleDateFormat.getDateTimeInstance().format(Calendar.getInstance()));
		URL url;
		InputStream in = null;
		boolean repeat = true;
		while (repeat) {
			try {
				System.out.println("Connecting");
				url = new URL("http://pipprovider.appspot.com/publicipprovider?op=set");
				in = url.openStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String resp = "";
				String tempResp = "";
				while (tempResp != null) {
					resp += tempResp;
					tempResp = reader.readLine();
				}
				if (resp.equals("OK")) {
					repeat = false;
				} else {
					Thread.sleep(30000);
					repeat = true;
				}
				
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("END at " + SimpleDateFormat.getDateTimeInstance().format(Calendar.getInstance()));
	}

}
