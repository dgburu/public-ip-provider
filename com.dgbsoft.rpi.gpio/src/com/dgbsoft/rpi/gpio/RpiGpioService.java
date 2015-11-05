package com.dgbsoft.rpi.gpio;

import java.util.HashMap;
import java.util.Map;

import com.dgbsoft.core.services.IRpiGpioService;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class RpiGpioService implements IRpiGpioService {

	private final GpioController gpio;
	private Map<String, GpioPinDigitalOutput> pins = null;
	
	public RpiGpioService() {
		gpio = GpioFactory.getInstance();
		pins = new HashMap<String, GpioPinDigitalOutput>();
	}
	
	@Override
	public void pinToHigh(String pinNumber) {
		GpioPinDigitalOutput pin = pins.get(pinNumber);
		if (pin == null) {
	        pin = gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(pinNumber),   // PIN NUMBER
	                pinNumber,           // PIN FRIENDLY NAME (optional)
	                PinState.LOW);      // PIN STARTUP STATE (optional)
			pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
			pins.put(pinNumber, pin);
		}
		pin.high();
	}

	@Override
	public void pinToLow(String pinNumber) {
		GpioPinDigitalOutput pin = pins.get(pinNumber);
		if (pin == null) {
	        pin = gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(pinNumber),   // PIN NUMBER
	                pinNumber,           // PIN FRIENDLY NAME (optional)
	                PinState.LOW);      // PIN STARTUP STATE (optional)
			pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
			pins.put(pinNumber, pin);
		}
		pin.low();		
	}
	
	@Override
	public void shutdown() {
		pins.clear();
		gpio.shutdown();
	}

}
