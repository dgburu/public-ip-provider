package com.dgbsoft.apps.fileserver.http.actions;

import com.dgbsoft.apps.fileserver.IAction;
import com.dgbsoft.core.services.IRpiGpioService;
import com.dgbsoft.core.services.ServicesUtil;

public class EnableFanAction implements IAction {

	@Override
	public boolean perform() {
		IRpiGpioService service = ServicesUtil.getService(IRpiGpioService.class);
		if (service != null) {
			service.pinToHigh(IRpiGpioService.PIN_40);
		}
		return true;
	}

	@Override
	public void stop() {
	}

}
