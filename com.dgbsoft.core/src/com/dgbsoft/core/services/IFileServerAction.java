package com.dgbsoft.core.services;

import java.util.Map;

import com.dgbsoft.core.services.misc.IStreamProvider;

public interface IFileServerAction {

	/**
	 * Performs the action.
	 * 
	 * @return true if should stop processing commands.
	 */
	public boolean perform();

	public void stop();

	public String getActionId();

	public void setStreamProvider(IStreamProvider streamProvider);

	public void setData(Map<String, String> data);

}
