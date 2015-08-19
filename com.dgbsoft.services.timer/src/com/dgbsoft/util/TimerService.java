package com.dgbsoft.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.dgbsoft.core.services.ITimerListener;
import com.dgbsoft.core.services.ITimerService;

public class TimerService implements ITimerService {

	private Map<ITimerListener, Timer> timerMap = null;
	
	public TimerService() {
		timerMap = new HashMap<ITimerListener, Timer>();
	}
	
	@Override
	public void addTimerListener(final ITimerListener listener) {
		if (timerMap.containsKey(listener)) {
			removeTimerListener(listener);
		}
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				listener.timerEvent();
			}
		}, 0, listener.getPeriod());
		timerMap.put(listener, timer);
	}

	@Override
	public void removeTimerListener(ITimerListener listener) {
		Timer timer = timerMap.get(listener);
		timer.cancel();
		timerMap.remove(listener);
	}

}
