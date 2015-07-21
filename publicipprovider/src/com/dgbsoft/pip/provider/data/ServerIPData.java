package com.dgbsoft.pip.provider.data;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class ServerIPData {

	public final static String KEY_IP_DATA = "ipKey";
	
	@PrimaryKey
	private String key = KEY_IP_DATA;

	@Persistent
	private String ip = null;
	
	@Persistent
	private Long setTime = null;
	
	public ServerIPData(String ip, Long time) {
		this.ip = ip;
		setTime = time;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getTime() {
		return setTime;
	}
	
	public void setTime(Long time) {
		this.setTime = time;
	}
	
	public String getKey() {
		return key;
	}
			
}
