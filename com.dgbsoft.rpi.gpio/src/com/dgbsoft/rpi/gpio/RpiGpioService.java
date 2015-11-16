package com.dgbsoft.rpi.gpio;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.dgbsoft.core.services.IRpiGpioService;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.system.SystemInfo;

public class RpiGpioService implements IRpiGpioService {

	private final static Logger LOG = Logger.getLogger(RpiGpioService.class.getName());

	private final GpioController gpio;
	private Map<String, GpioPinDigitalOutput> pins = null;
	
	public RpiGpioService() {
		LOG.finest("getting instance of gpio");
		gpio = GpioFactory.getInstance();
		pins = new HashMap<String, GpioPinDigitalOutput>();
		LOG.finest("end getting instance of gpio");
	}
	
	@Override
	public void pinToHigh(String pinNumber) {
		GpioPinDigitalOutput pin = pins.get(pinNumber);
		if (pin == null) {
			LOG.finest("creating pin " + pinNumber);
	        pin = gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(pinNumber),   // PIN NUMBER
	                pinNumber,           // PIN FRIENDLY NAME (optional)
	                PinState.LOW);      // PIN STARTUP STATE (optional)
			pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
			pins.put(pinNumber, pin);
		}
		LOG.fine("pin " + pinNumber + " to high");
		pin.high();
	}

	@Override
	public void pinToLow(String pinNumber) {
		GpioPinDigitalOutput pin = pins.get(pinNumber);
		if (pin == null) {
			LOG.finest("creating pin " + pinNumber);
	        pin = gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(pinNumber),   // PIN NUMBER
	                pinNumber,           // PIN FRIENDLY NAME (optional)
	                PinState.LOW);      // PIN STARTUP STATE (optional)
			pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
			pins.put(pinNumber, pin);
		}
		LOG.fine("pin " + pinNumber + " to low");
		pin.low();		
	}
	
	@Override
	public void shutdown() {
		LOG.finest("shutting down gpio");
		pins.clear();
		gpio.shutdown();
		LOG.finest("end shutting");
    }

    @Override
    public float getTemperature() {
    	try {
			return SystemInfo.getCpuTemperature();
		} catch (NumberFormatException | IOException | InterruptedException e) {
			LOG.severe("Cannot retrieve cpu temperature");
		}
    	return -1;
    }
    
}
