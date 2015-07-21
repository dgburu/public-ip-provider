package com.dgbsoft.pip.provider.data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ServerIPData {

	public final static String KEY_IP_DATA = "ipKey";
	
	@Id
	private String key = KEY_IP_DATA;

	private String ip = null;
	
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
