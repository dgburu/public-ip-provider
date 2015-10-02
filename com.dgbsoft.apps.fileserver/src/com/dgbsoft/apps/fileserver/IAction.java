package com.dgbsoft.apps.fileserver;

public interface IAction {

	/**
	 * Performs the action.
	 * @return true if should stop processing commands.
	 */
	public boolean perform();

	public void stop();
	
}
