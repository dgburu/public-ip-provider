package com.dgbsoft.core.services;

import java.util.Date;

public interface ITimerListener {

	public int getPeriod();
	
	public void timerEvent();
	
	public Date getDate();
	
}
