package com.dgbsoft.core.services;

public interface IRpiGpioService {

	public String PIN_3 = "GPIO 08"; //equivalence to pi4j pin numbers
	public String PIN_5 = "GPIO 09"; //equivalence to pi4j pin numbers
	public String PIN_7 = "GPIO 07"; //equivalence to pi4j pin numbers
	public String PIN_8 = "GPIO 15"; //equivalence to pi4j pin numbers
	public String PIN_10 = "GPIO 16"; //equivalence to pi4j pin numbers
	public String PIN_11 = "GPIO 00"; //equivalence to pi4j pin numbers
	public String PIN_12 = "GPIO 01"; //equivalence to pi4j pin numbers
	public String PIN_13 = "GPIO 01"; //equivalence to pi4j pin numbers
	public String PIN_15 = "GPIO 03"; //equivalence to pi4j pin numbers
	public String PIN_16 = "GPIO 04"; //equivalence to pi4j pin numbers
	public String PIN_18 = "GPIO 05"; //equivalence to pi4j pin numbers
	public String PIN_19 = "GPIO 12"; //equivalence to pi4j pin numbers
	public String PIN_21 = "GPIO 13"; //equivalence to pi4j pin numbers
	public String PIN_22 = "GPIO 06"; //equivalence to pi4j pin numbers
	public String PIN_23 = "GPIO 14"; //equivalence to pi4j pin numbers
	public String PIN_24 = "GPIO 10"; //equivalence to pi4j pin numbers
	public String PIN_26 = "GPIO 11"; //equivalence to pi4j pin numbers
	public String PIN_29 = "GPIO 21"; //equivalence to pi4j pin numbers
	public String PIN_31 = "GPIO 11"; //equivalence to pi4j pin numbers
	public String PIN_32 = "GPIO 26"; //equivalence to pi4j pin numbers
	public String PIN_33 = "GPIO 23"; //equivalence to pi4j pin numbers
	public String PIN_35 = "GPIO 24"; //equivalence to pi4j pin numbers
	public String PIN_36 = "GPIO 27"; //equivalence to pi4j pin numbers
	public String PIN_37 = "GPIO 25"; //equivalence to pi4j pin numbers
	public String PIN_38 = "GPIO 28"; //equivalence to pi4j pin numbers
	public String PIN_40 = "GPIO 29"; //equivalence to pi4j pin numbers
	
	public void pinToHigh(String pinNumber);
	public void pinToLow(String pinNumber);
	public void shutdown();
    public float getTemperature();
}
