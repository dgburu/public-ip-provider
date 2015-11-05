package com.dgbsoft.apps.fileserver.http.actions;

import com.dgbsoft.apps.fileserver.IAction;
import com.dgbsoft.core.services.IRpiGpioService;
import com.dgbsoft.core.services.ServicesUtil;

public class DisableFanAction implements IAction {

	@Override
	public boolean perform() {
		IRpiGpioService service = ServicesUtil.getService(IRpiGpioService.class);
		if (service != null) {
			service.pinToLow(IRpiGpioService.PIN_40);
		}
		return true;
	}

	@Override
	public void stop() {
	}

}
