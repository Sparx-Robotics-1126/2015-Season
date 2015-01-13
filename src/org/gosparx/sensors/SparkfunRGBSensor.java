package org.gosparx.sensors;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalOutput;

/* Implement Sparkfun type RGB sensor
 * Author: Raza Ahmed, Reizwan Chowdhury
 * version 1.0 season 2015
 */

public class SparkfunRGBSensor{

	private AnalogInput redAnalogInput;
	private AnalogInput greenAnalogInput;
	private AnalogInput blueAnalogInput; 
	private DigitalOutput ledDigitalOutput;
	// the max value we read testing was 327 for red the documentation on the web scales it by 10
	// so we decided to set our max at 3500
	static final int MAX_VALUE = 3500;
	// from the web we found the scaling factor of red, 10
	static final int RED_SCALING_FACTOR = 10;
	// from the web we found the scaling factor of green, 14
	static final int GREEN_SCALING_FACTOR = 14;
	// from the web we found the scaling factor of blue, 17
	static final int BLUE_SCALING_FACTOR = 17;

	public SparkfunRGBSensor(int redChannel, int greenChannel, int blueChannel, int ledChannel){
		redAnalogInput = new AnalogInput(redChannel);
		greenAnalogInput = new AnalogInput(greenChannel);
		blueAnalogInput = new AnalogInput(blueChannel);
		ledDigitalOutput = new DigitalOutput (ledChannel);
		// turning on LED is default
		ledDigitalOutput.set(true);	
	}

	//standardizing into a value from 0-255
	public int getRed(){
		int redValue = redAnalogInput.getValue() * RED_SCALING_FACTOR;
		return (int)((255/MAX_VALUE)* redValue);
	}
	// standardizing into a value from 0-255
	public int getGreen(){
		int greenValue = greenAnalogInput.getValue() * GREEN_SCALING_FACTOR;
		return (int)((255/MAX_VALUE)* greenValue);
	}
	// standardizing into a value from 0-255
	public int getBlue(){
		int blueValue = blueAnalogInput.getValue() * BLUE_SCALING_FACTOR;
		return (int)((255/MAX_VALUE)* blueValue);
	}
	public void setLED(boolean on){
			ledDigitalOutput.set(on);	
	}
}

