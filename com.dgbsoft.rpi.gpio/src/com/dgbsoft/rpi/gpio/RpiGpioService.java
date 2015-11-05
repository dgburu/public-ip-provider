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
	private Map<Integer, GpioPinDigitalOutput> pins = null;
	
	public RpiGpioService() {
		gpio = GpioFactory.getInstance();
		pins = new HashMap<Integer, GpioPinDigitalOutput>();
	}
	
	@Override
	public void pinToHigh(int pinNumber) {
		
        GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04,   // PIN NUMBER
                "My LED",           // PIN FRIENDLY NAME (optional)
                PinState.LOW);      // PIN STARTUP STATE (optional)
		// END SNIPPET: usage-provision-output-pin-snippet
		
		// START SNIPPET: usage-shutdown-pin-snippet
		// configure the pin shutdown behavior; these settings will be 
		// automatically applied to the pin when the application is terminated
		// ensure that the LED is turned OFF when the application is shutdown
		pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
		
	}

	@Override
	public void pinToLow(int pinNumber) {
		// TODO Auto-generated method stub
		
	}

}
