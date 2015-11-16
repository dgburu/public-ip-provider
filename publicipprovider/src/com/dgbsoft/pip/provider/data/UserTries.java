package com.dgbsoft.pip.provider.data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserTries {

	@Id
	private String user;
	
	private Integer tries;
	
	public UserTries(String user, Integer tries) {
		this.user = user;
		this.tries = tries;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Integer getTries() {
		return tries;
	}
	
	public void setTries(Integer tries) {
		this.tries = tries;
	}
	
}
