package com.dgbsoft.core.services;

public interface ITimerService {

	public void addTimerListener(ITimerListener listener);
	
	public void removeTimerListener(ITimerListener listener);
	
}
