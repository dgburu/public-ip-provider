package com.dgbsoft.pip.provider.data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BlockedIp {

	@Id
	private String remoteIp = null;

	public BlockedIp(String remoteAddress) {
		this.remoteIp = remoteAddress;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	
	
}
